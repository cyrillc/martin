package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

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
import ch.zhaw.psit4.martin.db.function.Function;
import ch.zhaw.psit4.martin.db.keyword.Keyword;
import ch.zhaw.psit4.martin.db.plugin.Plugin;
import ch.zhaw.psit4.martin.db.plugin.PluginService;

public class PluginDataAccessor {
    
    /*
     * Log from the common logging api
     */
    private static final Log LOG = LogFactory.getLog(PluginDataAccessor.class);
    
    @Autowired
    private PluginService pluginService;
    
    public PluginDataAccessor() {
        // empty
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

        // update DB-object
        plugin.setName(pluginName.valueAsString());
        if(pluginDesctibtion != null)
            plugin.setDescription(pluginDesctibtion.valueAsString());
        else
            plugin.setDescription("No Describtion provided.");
        if(pluginDate != null) {
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

        return plugin;
    }
    
    /**
     * Get the author Metadata from plugins.xml
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
     * Get the plugin JSON file for the keywords and functions.
     * 
     * @param keywordsUrl The URL of the file on the filesystem.
     * @return The loaded JSON-file or null if an error occurred.
     */
    public JSONObject parseFunctionsJSON(URL functionsUrl) {
        // keywords JSON loading
        JSONObject json = null;
        try {
            // Get JSON
            InputStream is = functionsUrl.openStream();
            json = new JSONObject(IOUtils.toString(is));
            is.close();
        } catch (JSONException | IOException e) {
            LOG.error("keywords.json could not be accessed.", e);
        }

        return json;
    }

    /**
     * Gets a set of function attributes from the JSON file.
     * 
     * @param json The JSON file.
     * @return A set of function objects.
     */
    public Set<Function> parsePluginFunctions(JSONObject json) {
        JSONArray jsonFunctions = json.getJSONArray("Functions");
        Set<Function> functions = new HashSet<>();
        for (int numFuncts = 0; numFuncts < jsonFunctions.length(); numFuncts++) {
            // get function
            JSONObject jsonFunct = jsonFunctions.getJSONObject(numFuncts);
            String functName = jsonFunct.getString("Name");
            String description = jsonFunct.getString("Describtion");

            Function function = new Function();
            function.setName(functName);
            function.setDescription(description);
            functions.add(function);
        }
        return functions;
    }
    
    /**
     * Gets a set of parameters from the JSON file for a specific function.
     * 
     * @param jsonFunct The array of JSON function elements.
     * @return A set of Parameter objects.
     */
    public Set<ch.zhaw.psit4.martin.db.parameter.Parameter> parseParameters(JSONObject jsonFunct) {
        JSONArray jsonParameter = jsonFunct.getJSONArray("Parameter");
        Set<ch.zhaw.psit4.martin.db.parameter.Parameter> parameter = new HashSet<>();
        for(int paramNum = 0; paramNum < jsonParameter.length(); paramNum++) {
            // get parameter
            JSONObject jsonparam = jsonParameter.getJSONObject(paramNum);
            String paramName = jsonFunct.getString("Name");
            boolean required = jsonFunct.getBoolean("Required");
            String type = jsonFunct.getString("Type");
            String regex = jsonFunct.getString("Tokens-regex");
            
            ch.zhaw.psit4.martin.db.parameter.Parameter param = new ch.zhaw.psit4.martin.db.parameter.Parameter();
            param.setName(paramName);
            param.setRequired(required);
            param.setType(type);
            param.setTokensRegex(regex);
            parameter.add(param);
        }
        return parameter;
    }
    
    /**
     * Gets a set of keywords from the JSON file for a specific function.
     * 
     * @param jsonFunct The array of JSON function elements.
     * @return A set of keyword objects.
     */
    public Set<Keyword> parseKeywords(JSONObject jsonFunct) {
        JSONArray jsonKeywords = jsonFunct.getJSONArray("Examples");
        Set<Keyword> keywords = new HashSet<>();
        for(int keyWordNum = 0; keyWordNum < jsonKeywords.length(); keyWordNum++) {
            Keyword keyword = new Keyword();
            keyword.setKeyword(jsonKeywords.getString(keyWordNum));
            keywords.add(keyword);
        }
        return keywords;
    }
}
