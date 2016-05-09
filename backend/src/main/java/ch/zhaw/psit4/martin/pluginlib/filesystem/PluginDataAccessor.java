package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.Extension.Parameter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.db.*;

public class PluginDataAccessor {

    /**
     * File name of the plugin keywords JSON.
     */
    public static final String PLUGIN_FUNCTIONS = "functions.json";
    /*
     * Log from the common logging api
     */
    private static final Log LOG = LogFactory.getLog(PluginDataAccessor.class);

    @Autowired
    private AuthorService authorService;

    @Autowired
    private PluginService pluginService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private KeywordService keywordService;

    public PluginDataAccessor() {
        // empty
    }

    public void savePluginInDB(Extension extension, ClassLoader classLoader)
            throws KeywordsJSONMissingException {
        LOG.info("add new Plugin to Database =?==============================================================");
        // get JSON
        URL jsonUrl = classLoader.getResource(PLUGIN_FUNCTIONS);
        JSONObject jsonPluginSource = parseJSON(jsonUrl);

        // check if there is a JSON file
        if (jsonPluginSource == null)
            throw new KeywordsJSONMissingException(
                    "keywords.json missing for " + extension.getParameter("name").valueAsString());


        // get author
        Author author = getAuthorData(extension);
        List<Author> possibleAuthors = authorService.getAuthorsByName(author.getName());
        LOG.info("add author to Database : "+author.getName());
        if (possibleAuthors.isEmpty()) {
            authorService.addAuthor(author);
        } else {
            author = possibleAuthors.get(0);
        }

        // get plugin
        Plugin dbPlugin = getPluginMetadata(extension);
        List<Plugin> possiblePlugins = pluginService.getPluginsByUUID(dbPlugin.getUuid());
        LOG.info("add Plugin to Database: "+dbPlugin.getName());
        if (possiblePlugins.isEmpty()) {
            dbPlugin.setAuthor(author);
            pluginService.addPlugin(dbPlugin);
        } else {
            dbPlugin = possiblePlugins.get(0);
        }

        // get Functions in the Plugin
        List<Function> functionsFromJson = parsePluginFunctions(jsonPluginSource, dbPlugin);

        LOG.info("funktionen die zur DB hinzugefügt werden:"+functionsFromJson.size());
        for (Function function : functionsFromJson) {
                Set<ch.zhaw.psit4.martin.db.Parameter> parameter = function.getParameter();
                
            if (!functionExistsInDB(function, dbPlugin)) {
                LOG.info("INSERT Function " + function.getName() + " into DB");
                functionService.addFunction(function);
            } else {
                LOG.warn("Function " + function.getName() + " allready in Database");
                // replace function with the function from the database
                function = getExistingFunctionFromDB(function, dbPlugin);
                //add parameter from newly loaded function
                function.addParameter(parameter);
                functionService.updateFunction(function);
            }
                //Persist parameter
                //parameter.stream().forEach(p -> parameterService.addParameter(p));

        }
        /**
         * for each function!
         * 
         * // parse function parameters
         **/
    }


    /**
     * Get the plugin JSON file for the keywords and functions.
     * 
     * @param keywordsUrl The URL of the file on the filesystem.
     * @return The loaded JSON-file or null if an error occurred.
     */
    public JSONObject parseJSON(URL url) {
        // keywords JSON loading
        JSONObject json = null;
        try {
            // Get JSON
            InputStream is = url.openStream();
            json = new JSONObject(IOUtils.toString(is));
            is.close();
        } catch (JSONException | IOException e) {
            LOG.error("keywords.json could not be accessed.", e);
        }

        return json;
    }


    /**
     * Gets the plugin metadata from the plugin.xml and parses it to a Java object.
     * 
     * @param extension The extension to get the plugins from.
     * @return The java plugin object.
     */
    public Plugin getPluginMetadata(Extension extension) {
        Plugin plugin = new Plugin();

        // metadata-parsing (mandatory)
        Parameter pluginName = extension.getParameter("name");

        // metadata-parsing (optional)
        Parameter pluginDesctibtion = extension.getParameter("description");
        Parameter pluginDate = extension.getParameter("date");
        String uuid = extension.getId();

        if (uuid == null) {
            LOG.error("Extension ID not accessible, generating new UUID");
            uuid = UUID.randomUUID().toString();
        }

        // update DB-object
        plugin.setName(pluginName.valueAsString());
        if (pluginDesctibtion != null)
            plugin.setDescription(pluginDesctibtion.valueAsString());
        else
            plugin.setDescription("No description provided.");
        if (pluginDate != null) {
            String date = pluginDate.valueAsString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsed = null;
            try {
                parsed = format.parse(date);
                java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
                plugin.setDate(sqlDate);
            } catch (ParseException e) {
                LOG.warn("Could not parse date.", e);
            }
        }
        plugin.setUuid(uuid);

        return plugin;
    }

    /**
     * Get the author Metadata from plugins.xml
     * 
     * @param extension The extension to get the data from.
     * @return The author java type.
     */
    public Author getAuthorData(Extension extension) {
        Author author = new Author();

        // get data
        Parameter pluginAuthor = extension.getParameter("author");
        Parameter pluginMail = extension.getParameter("e-mail");

        // update DB-object
        author.setName(pluginAuthor.valueAsString());
        author.setEmail(pluginMail.valueAsString());

        return author;
    }

    /**
     * Gets a set of function attributes from the JSON file.
     * 
     * @param json The JSON file.
     * @return A set of function objects.
     */
    public List<Function> parsePluginFunctions(JSONObject json, Plugin plugin) {

        ArrayList<Function> functions = new ArrayList<>();
        JSONArray jsonFunctions = json.getJSONArray("Functions");
        for (int numFuncts = 0; numFuncts < jsonFunctions.length(); numFuncts++) {
            // get function attributes
            JSONObject jsonFunction = jsonFunctions.getJSONObject(numFuncts);

            LOG.info("create Function: " + jsonFunction.getString("Name") + "with Plugin ID = "
                    + plugin.getId());
            Function function = new Function();
            function.setName(jsonFunction.getString("Name"));
            function.setDescription(jsonFunction.getString("Describtion"));
            function.setPlugin(plugin);


            List<ch.zhaw.psit4.martin.db.Parameter> functionParameter = parseFunctionParameters(jsonFunction, function);

            function.setParameter(new HashSet<ch.zhaw.psit4.martin.db.Parameter>(functionParameter));
            
            functions.add(function);

        }
        return functions;
    }

    /**
     * @param function
     * @param plugin
     * @return true if functionExists in Database
     */
    private boolean functionExistsInDB(Function function, Plugin plugin) {
        return (getExistingFunctionFromDB(function, plugin) != null) ? true : false;
    }

    /**
     * Checks if a function exists in the DB and returns the given function form the database
     * 
     * @param function
     * @param plugin
     * @return null if the function does not exist in the Database or the function
     */
    private Function getExistingFunctionFromDB(Function function, Plugin plugin) {
        Set<Function> functions = plugin.getFunctions();
        if (functions != null)
            for (Function f : functions) {
                if (f.getName().equals(function.getName())
                        && f.getPlugin().getUuid().equals(plugin.getUuid())) {
                    return f;
                }
            }
        return null;
    }

    /**
     * Gets a set of parameters from the JSON file for a specific function.
     * 
     * @param jsonFunction The array of JSON function elements.
     * @return A set of Parameter objects.
     */
    public List<ch.zhaw.psit4.martin.db.Parameter> parseFunctionParameters(JSONObject jsonFunction, Function function) {
        List<ch.zhaw.psit4.martin.db.Parameter> functionParameter = new ArrayList<>();

        JSONArray jsonParameter = jsonFunction.getJSONArray("Parameter");
        
        for (int i = 0; i < jsonParameter.length(); i++) {
            // get parameter
            JSONObject jsonparam = jsonParameter.getJSONObject(i);

            ch.zhaw.psit4.martin.db.Parameter parameter =
                    new ch.zhaw.psit4.martin.db.Parameter();
            parameter.setName(jsonparam.getString("Name"));
            parameter.setRequired(jsonparam.getBoolean("Required"));
            parameter.setType(jsonparam.getString("Type"));
            parameter.setFunction(function);
            
            functionParameter.add(parameter);

            /*
            // check if param is allready in DB
            Set<ch.zhaw.psit4.martin.db.parameter.Parameter> parameter = function.getParameter();
            boolean paramExisting = false;
            if (parameter != null)
                for (ch.zhaw.psit4.martin.db.parameter.Parameter p : parameter) {
                    if (p.getName().equals(param.getName())
                            && p.getFunction().getName().equals(function.getName())
                            && p.getFunction().getPlugin().getUuid()
                                    .equals(function.getPlugin().getUuid())) {
                        paramExisting = true;
                        param = p;
                        break;
                    }
                }
            if (!paramExisting) {
                parameterService.addParameter(param);
            }

            parseKeywords(jsonFunction, param);
            */
        }
        
        return functionParameter;
    }

    /**
     * Gets a set of keywords from the JSON file for a specific function.
     * 
     * @param jsonFunct The array of JSON function elements.
     * @return A set of keyword objects.
     */
    public void parseKeywords(JSONObject jsonFunct,
            ch.zhaw.psit4.martin.db.Parameter param) {
        JSONArray jsonKeywords = jsonFunct.getJSONArray("Examples");
        for (int keyWordNum = 0; keyWordNum < jsonKeywords.length(); keyWordNum++) {
            Keyword keyword = new Keyword();
            keyword.setKeyword(jsonKeywords.getString(keyWordNum));

            List<Keyword> keywords = keywordService.getMatchingKeywords(keyword.getKeyword());
            if (keywords.isEmpty()) {
                Set<ch.zhaw.psit4.martin.db.Parameter> params = new HashSet<>();
                params.add(param);
                keyword.setParameter(params);
                keywordService.addKeyword(keyword);
            } else {
                keyword = keywords.get(0);

                // check keyword parameter
                Set<ch.zhaw.psit4.martin.db.Parameter> parameter = keyword.getParameter();
                boolean paramExisting = false;
                for (ch.zhaw.psit4.martin.db.Parameter p : parameter) {
                    if (p.getName().equals(param.getName())
                            && p.getFunction().getName().equals(param.getFunction().getName())
                            && p.getFunction().getPlugin().getUuid()
                                    .equals(param.getFunction().getPlugin().getUuid())) {
                        paramExisting = true;
                        break;
                    }
                }
                if (!paramExisting) {
                    keyword.getParentParameter().add(param);
                    keywordService.updateKeyword(keyword);
                }
            }
        }
    }
}
