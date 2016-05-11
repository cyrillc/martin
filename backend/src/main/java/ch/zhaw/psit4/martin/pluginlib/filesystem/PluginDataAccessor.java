package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.registry.Extension;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.common.MartinExtensionParser;
import ch.zhaw.psit4.martin.models.*;
import ch.zhaw.psit4.martin.models.repositories.MPluginRepository;

public class PluginDataAccessor {

    /**
     * File name of the plugin keywords JSON.
     */
    public static final String PLUGIN_FUNCTIONS = "functions.json";

    private static final Log LOG = LogFactory.getLog(PluginDataAccessor.class);

    @Autowired
    private MPluginRepository pluginRepository;

    public void savePluginInDB(Extension pluginData, ClassLoader classLoader)
            throws FunctionsJSONMissingException {

        JSONObject jsonPluginSource = parseJSON(classLoader.getResource(PLUGIN_FUNCTIONS));

        // check if there is a JSON file
        if (jsonPluginSource == null) {
            throw new FunctionsJSONMissingException(
                    "keywords.json missing for " + pluginData.getParameter("name").valueAsString());
        }

        MPlugin dbPlugin = createPluginFromFrameworkData(pluginData);

        if(pluginRepository.findByUuid(dbPlugin.getUuid()) == null) {
            //Plugin is new
            fillPluginModel(jsonPluginSource, dbPlugin);
            pluginRepository.save(dbPlugin);
        } else {
            LOG.info("Plugin "+dbPlugin.getName()+" already exists in DB");
        }
    }

    private void fillPluginModel(JSONObject jsonPluginSource, MPlugin dbPlugin) {
        Set<MFunction> functionsFromJson = parsePluginFunctions(jsonPluginSource, dbPlugin);

        for (MFunction function : functionsFromJson) {
            Set<MParameter> parameter = function.getParameters();

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
    public MPlugin createPluginFromFrameworkData(Extension extension) {

        MPlugin plugin = MartinExtensionParser.getPluginFroExtension(extension);

        if (plugin.getUuid() == null) {
            LOG.error("Extension ID not accessible, generating new UUID");
            plugin.setUuid(UUID.randomUUID().toString());
        }

        plugin.setAuthor(MartinExtensionParser.getAuthorFromExtension(extension));

        return plugin;
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
    public List<MParameter> parseFunctionParameters(
            JSONObject jsonFunction, MFunction function) {
        List<MParameter> functionParameter = new ArrayList<>();

        JSONArray jsonParameter = jsonFunction.getJSONArray("Parameter");

        for (int i = 0; i < jsonParameter.length(); i++) {
            // get parameter
            JSONObject jsonparam = jsonParameter.getJSONObject(i);

            MParameter parameter = new MParameter();
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
