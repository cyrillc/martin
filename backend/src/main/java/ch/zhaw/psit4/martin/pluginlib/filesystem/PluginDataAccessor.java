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
import ch.zhaw.psit4.martin.common.MartinHelper;
import ch.zhaw.psit4.martin.models.*;
import ch.zhaw.psit4.martin.models.repositories.MKeywordRepository;
import ch.zhaw.psit4.martin.models.repositories.MPluginRepository;

public class PluginDataAccessor {

    /**
     * File name of the plugin keywords JSON.
     */
    public static final String PLUGIN_FUNCTIONS = "functions.json";

    private static final Log LOG = LogFactory.getLog(PluginDataAccessor.class);

    @Autowired
    private MPluginRepository pluginRepository;

    @Autowired
    private MKeywordRepository keywordRepository;

    public void savePluginInDB(Extension pluginData, ClassLoader classLoader)
            throws FunctionsJSONMissingException {

        MPlugin dbPlugin = createPluginFromFrameworkData(pluginData);

        if (pluginRepository.findByUuid(dbPlugin.getUuid()) == null) {
            // Plugin is new
            JSONObject jsonPluginSource =
                    MartinHelper.parseJSON(classLoader.getResource(PLUGIN_FUNCTIONS));
            addJSONDataToPlugin(jsonPluginSource, dbPlugin);
            pluginRepository.save(dbPlugin);
        } else {
            LOG.info("Plugin " + dbPlugin.getName() + " already exists in DB");
        }
    }

    private void addJSONDataToPlugin(JSONObject jsonPluginSource, MPlugin dbPlugin) {
        Set<MFunction> functionsFromJson = parsePluginFunctions(jsonPluginSource, dbPlugin);
        functionsFromJson.stream().forEach(f -> dbPlugin.addFunction(f));
    }


    private MPlugin createPluginFromFrameworkData(Extension extension) {

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
        for (int functionNumber = 0; functionNumber < jsonFunctions.length(); functionNumber++) {
            // get function attributes
            JSONObject jsonFunction = jsonFunctions.getJSONObject(functionNumber);

            LOG.debug("create Function: " + jsonFunction.getString("Name") + " with Plugin ID = "
                    + plugin.getId());
            MFunction function = new MFunction(jsonFunction.getString("Name"),
                                                jsonFunction.getString("Description"));

            Set<MParameter> functionParameter = parseFunctionParameters(jsonFunction, function);
            functionParameter.stream().forEach(p -> function.addParameter(p));

            addKeywordsToFunction(jsonFunction, function);
            addExampleCallsToFunction(jsonFunction, function);
            functions.add(function);

        }
        return new HashSet<>(functions);
    }

    private void addExampleCallsToFunction(JSONObject jsonFunction, MFunction function) {
        
        JSONArray jsonCalls = jsonFunction.getJSONArray("Examples");
        for (int callNumber = 0; callNumber < jsonCalls.length(); callNumber++) {
            MExampleCall exampleCall = new MExampleCall();
            exampleCall.setCall(jsonCalls.getString(callNumber));

            function.addExampleCall(exampleCall);
        }
    }

    /**
     * Gets a set of parameters from the JSON file for a specific function.
     * 
     * @param jsonFunction The array of JSON function elements.
     * @return A set of Parameter objects.
     */
    public Set<MParameter> parseFunctionParameters(JSONObject jsonFunction, MFunction function) {
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

        return new HashSet<>(functionParameter);
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
                String keywordName = jsonKeywords.getString(keyWordNum);
                MKeyword keyword = keywordRepository.findByKeywordIgnoreCase(keywordName);
                if (keyword == null) {
                    keyword = new MKeyword();
                    keyword.setKeyword(keywordName);
                }
                param.addKeyword(keyword);
            }
        }
    }
}
