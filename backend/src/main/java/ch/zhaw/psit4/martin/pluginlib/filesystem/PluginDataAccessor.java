package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import ch.zhaw.psit4.martin.db.author.Author;
import ch.zhaw.psit4.martin.db.author.AuthorService;
import ch.zhaw.psit4.martin.db.function.Function;
import ch.zhaw.psit4.martin.db.function.FunctionService;
import ch.zhaw.psit4.martin.db.keyword.Keyword;
import ch.zhaw.psit4.martin.db.keyword.KeywordService;
import ch.zhaw.psit4.martin.db.parameter.ParameterService;
import ch.zhaw.psit4.martin.db.plugin.Plugin;
import ch.zhaw.psit4.martin.db.plugin.PluginService;

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

    public void putPluginInDB(Extension extension, ClassLoader classLoader)
            throws KeywordsJSONMissingException {
        // get JSON
        URL jsonUrl = classLoader.getResource(PLUGIN_FUNCTIONS);
        JSONObject jsonKeywords = parseFunctionsJSON(jsonUrl);

        // check if there is a JSON file
        if (jsonKeywords == null)
            throw new KeywordsJSONMissingException(
                    "keywords.json missing for " + extension.getParameter("name").valueAsString());

        // get author
        Author author = getAuthorData(extension);
        List<Author> possibleAuthors = authorService.getAuthorsByName(author.getName());
        if (possibleAuthors.isEmpty()) {
            authorService.addAuthor(author);
        } else {
            author = possibleAuthors.get(0);
        }

        // get plugin
        Plugin dbPlugin = getPluginMetadata(extension);
        List<Plugin> possiblePlugins = pluginService.getPluginsByUUID(dbPlugin.getUuid());
        if (possiblePlugins.isEmpty()) {
            dbPlugin.setAuthor(author);
            pluginService.addPlugin(dbPlugin);
        } else {
            dbPlugin = possiblePlugins.get(0);
        }

        // parse JSON arguments
        parsePluginFunctions(jsonKeywords, dbPlugin);
    }

    /**
     * Get the plugin JSON file for the keywords and functions.
     * 
     * @param keywordsUrl The URL of the file on the filesystem.
     * @return The loaded JSON-file or null if an error occurred.
     */
    public JSONObject parseFunctionsJSON(URL url) {
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
            LOG.error("Extension ID not accessible");
            uuid = UUID.randomUUID().toString();
        }

        // update DB-object
        plugin.setName(pluginName.valueAsString());
        if (pluginDesctibtion != null)
            plugin.setDescription(pluginDesctibtion.valueAsString());
        else
            plugin.setDescription("No Describtion provided.");
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
    public void parsePluginFunctions(JSONObject json, Plugin plugin) {
        JSONArray jsonFunctions = json.getJSONArray("Functions");
        for (int numFuncts = 0; numFuncts < jsonFunctions.length(); numFuncts++) {
            // get function attributes
            JSONObject jsonFunct = jsonFunctions.getJSONObject(numFuncts);
            String functName = jsonFunct.getString("Name");
            String description = jsonFunct.getString("Describtion");

            Function function = new Function();
            function.setName(functName);
            function.setDescription(description);
            function.setPlugin(plugin);

            // check if function is allready in DB
            Set<Function> functions = plugin.getFunctions();
            boolean funcExisting = false;
            if (functions != null)
                for (Function f : functions) {
                    if (f.getName().equals(function.getName())
                            && f.getPlugin().getUuid().equals(plugin.getUuid())) {
                        funcExisting = true;
                        function = f;
                        break;
                    }
                }
            if (!funcExisting) {
                functionService.addFunction(function);
            }

            // parse function parameters
            parseParameters(jsonFunct, function);
        }
    }

    /**
     * Gets a set of parameters from the JSON file for a specific function.
     * 
     * @param jsonFunct The array of JSON function elements.
     * @return A set of Parameter objects.
     */
    public void parseParameters(JSONObject jsonFunct, Function functtion) {
        JSONArray jsonParameter = jsonFunct.getJSONArray("Parameter");
        for (int paramNum = 0; paramNum < jsonParameter.length(); paramNum++) {
            // get parameter
            JSONObject jsonparam = jsonParameter.getJSONObject(paramNum);
            String paramName = jsonparam.getString("Name");
            boolean required = jsonparam.getBoolean("Required");
            String type = jsonparam.getString("Type");

            ch.zhaw.psit4.martin.db.parameter.Parameter param =
                    new ch.zhaw.psit4.martin.db.parameter.Parameter();
            param.setName(paramName);
            param.setRequired(required);
            param.setType(type);
            param.setFunction(functtion);

            // check if param is allready in DB
            Set<ch.zhaw.psit4.martin.db.parameter.Parameter> parameter = functtion.getParameter();
            boolean paramExisting = false;
            if (parameter != null)
                for (ch.zhaw.psit4.martin.db.parameter.Parameter p : parameter) {
                    if (p.getName().equals(param.getName())
                            && p.getFunction().getName().equals(functtion.getName())
                            && p.getFunction().getPlugin().getUuid()
                                    .equals(functtion.getPlugin().getUuid())) {
                        paramExisting = true;
                        param = p;
                        break;
                    }
                }
            if (!paramExisting) {
                parameterService.addParameter(param);
            }

            parseKeywords(jsonFunct, param);
        }
    }

    /**
     * Gets a set of keywords from the JSON file for a specific function.
     * 
     * @param jsonFunct The array of JSON function elements.
     * @return A set of keyword objects.
     */
    public void parseKeywords(JSONObject jsonFunct,
            ch.zhaw.psit4.martin.db.parameter.Parameter param) {
        JSONArray jsonKeywords = jsonFunct.getJSONArray("Examples");
        for (int keyWordNum = 0; keyWordNum < jsonKeywords.length(); keyWordNum++) {
            Keyword keyword = new Keyword();
            keyword.setKeyword(jsonKeywords.getString(keyWordNum));

            List<Keyword> keywords = keywordService.getMatchingKeywords(keyword.getKeyword());
            if (keywords.isEmpty()) {
                Set<ch.zhaw.psit4.martin.db.parameter.Parameter> params = new HashSet<>();
                params.add(param);
                keyword.setParameter(params);
                keywordService.addKeyword(keyword);
            } else {
                keyword = keywords.get(0);

                // check keyword parameter
                Set<ch.zhaw.psit4.martin.db.parameter.Parameter> parameter = keyword.getParameter();
                boolean paramExisting = false;
                for (ch.zhaw.psit4.martin.db.parameter.Parameter p : parameter) {
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
