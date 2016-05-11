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

import ch.zhaw.psit4.martin.models.*;
import ch.zhaw.psit4.martin.models.repositories.MPluginRepository;

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
    private MPluginRepository pluginRepository;


    public PluginDataAccessor() {
        // empty
    }

    public void savePluginInDB(Extension extension, ClassLoader classLoader)
            throws KeywordsJSONMissingException {
        LOG.info(
                "add new Plugin to Database ===============================================================");
        // get JSON
        URL jsonUrl = classLoader.getResource(PLUGIN_FUNCTIONS);
        JSONObject jsonPluginSource = parseJSON(jsonUrl);

        // check if there is a JSON file
        if (jsonPluginSource == null) {
            throw new KeywordsJSONMissingException(
                    "keywords.json missing for " + extension.getParameter("name").valueAsString());
        }

        // get author
        MAuthor author = getAuthorData(extension);

        // get plugin
        MPlugin dbPlugin = getPluginMetadata(extension);

        if(pluginRepository.findByUuid(dbPlugin.getUuid()) == null) {
            /*
             * Plugin possiblePlugin = pluginEntityManager.byUUID(dbPlugin.getUuid()); LOG.info(
             * "add Plugin to Database: " + dbPlugin.getName());
             * 
             * if (possiblePlugin != null) { dbPlugin.setAuthor(author); } else { dbPlugin =
             * possiblePlugin; }
             */
            dbPlugin.setAuthor(author);
            // get Functions in the Plugin
            Set<MFunction> functionsFromJson = parsePluginFunctions(jsonPluginSource, dbPlugin);

            LOG.info("funktionen die zur DB hinzugefügt werden:" + functionsFromJson.size());
            for (MFunction function : functionsFromJson) {
                Set<ch.zhaw.psit4.martin.models.MParameter> parameter = function.getParameters();

                if (!functionExistsInDB(function, dbPlugin)) {
                    LOG.info("INSERT Function " + function.getName() + " into DB");
                } else {
                    LOG.warn("Function " + function.getName() + " allready in Database");
                    // replace function with the function from the database
                    function = getExistingFunctionFromDB(function, dbPlugin);
                    // add parameter from newly loaded function
                    function.addParameters(parameter);
                }

                dbPlugin.setFunctions(functionsFromJson);
                // Persist parameter
                // parameter.stream().forEach(p ->
                // parameterService.addParameter(p));

            }
            pluginRepository.save(dbPlugin);
        } else {
            LOG.info("Plugin "+dbPlugin.getName()+" already exists in DB");
        }
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
    public MPlugin getPluginMetadata(Extension extension) {
        MPlugin plugin = new MPlugin();

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
    public MAuthor getAuthorData(Extension extension) {
        MAuthor author = new MAuthor();

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
    public Set<MFunction> parsePluginFunctions(JSONObject json, MPlugin plugin) {

        ArrayList<MFunction> functions = new ArrayList<>();
        JSONArray jsonFunctions = json.getJSONArray("Functions");
        for (int numFuncts = 0; numFuncts < jsonFunctions.length(); numFuncts++) {
            // get function attributes
            JSONObject jsonFunction = jsonFunctions.getJSONObject(numFuncts);

            LOG.info("create Function: " + jsonFunction.getString("Name") + " with Plugin ID = "
                    + plugin.getId());
            MFunction function = new MFunction();
            function.setName(jsonFunction.getString("Name"));
            function.setDescription(jsonFunction.getString("Describtion"));
            function.setPlugin(plugin);

            List<ch.zhaw.psit4.martin.models.MParameter> functionParameter =
                    parseFunctionParameters(jsonFunction, function);

            function.setParameter(new HashSet<MParameter>(functionParameter));

            addKeywordsToFunction(jsonFunction, function);
            functions.add(function);

        }
        return new HashSet<>(functions);
    }

    /**
     * @param function
     * @param plugin
     * @return true if functionExists in Database
     */
    private boolean functionExistsInDB(MFunction function, MPlugin plugin) {
        return (getExistingFunctionFromDB(function, plugin) != null) ? true : false;
    }

    /**
     * Checks if a function exists in the DB and returns the given function form the database
     * 
     * @param function
     * @param plugin
     * @return null if the function does not exist in the Database or the function
     */
    private MFunction getExistingFunctionFromDB(MFunction function, MPlugin plugin) {
        Set<MFunction> functions = plugin.getFunctions();
        if (functions != null)
            for (MFunction f : functions) {
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
    public List<ch.zhaw.psit4.martin.models.MParameter> parseFunctionParameters(
            JSONObject jsonFunction, MFunction function) {
        List<ch.zhaw.psit4.martin.models.MParameter> functionParameter = new ArrayList<>();

        JSONArray jsonParameter = jsonFunction.getJSONArray("Parameter");

        for (int i = 0; i < jsonParameter.length(); i++) {
            // get parameter
            JSONObject jsonparam = jsonParameter.getJSONObject(i);

            ch.zhaw.psit4.martin.models.MParameter parameter =
                    new ch.zhaw.psit4.martin.models.MParameter();
            parameter.setName(jsonparam.getString("Name"));
            parameter.setRequired(jsonparam.getBoolean("Required"));
            parameter.setType(jsonparam.getString("Type"));

            functionParameter.add(parameter);

            addKeywordsToParameter(jsonparam, parameter);

            // parseKeywords(jsonFunction, param)
        }

        return functionParameter;
    }

    /**
     * Gets a set of keywords from the JSON file for a specific function.
     * 
     * @param jsonFunction The array of JSON function elements.
     * @return A set of keyword objects.
     */
    public void addKeywordsToFunction(JSONObject jsonFunction, MFunction function) {
        JSONArray jsonKeywords = jsonFunction.getJSONArray("Keywords");
        for (int keyWordNum = 0; keyWordNum < jsonKeywords.length(); keyWordNum++) {
            MKeyword keyword = new MKeyword();
            keyword.setKeyword(jsonKeywords.getString(keyWordNum));

            function.addKeyword(keyword);
        }
    }

    /**
     * Gets a set of keywords from the JSON file for a specific function.
     * 
     * @param jsonParameter The array of JSON function elements.
     * @return A set of keyword objects.
     */
    public void addKeywordsToParameter(JSONObject jsonParameter, MParameter param) {
        if (jsonParameter.getJSONArray("Keywords") != null
                && jsonParameter.getJSONArray("Keywords").length() >= 1) {

            JSONArray jsonKeywords = jsonParameter.getJSONArray("Keywords");
            for (int keyWordNum = 0; keyWordNum < jsonKeywords.length(); keyWordNum++) {
                MKeyword keyword = new MKeyword();
                keyword.setKeyword(jsonKeywords.getString(keyWordNum));

                param.addKeyword(keyword);
            }
        }
    }
}