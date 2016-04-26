package ch.zhaw.psit4.martin.pluginlib.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
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
import ch.zhaw.psit4.martin.db.author.AuthorService;
import ch.zhaw.psit4.martin.db.function.Function;
import ch.zhaw.psit4.martin.db.function.FunctionService;
import ch.zhaw.psit4.martin.db.keyword.Keyword;
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
        
        // parse JSON arguments
        Set<Function> functons = parsePluginFunctions(jsonKeywords);
        
        // get plugin
        Plugin dbPlugin = getPluginMetadata(extension);
        dbPlugin.setFunctions(functons);
        
        // get author
        Author author = getAuthorData(extension);
        Set<Plugin> pluginSet = new HashSet<>();
        pluginSet.add(dbPlugin);
        author.setPlugins(pluginSet);
        
        // update DB
        authorService.addAuthor(author);
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

            Set<ch.zhaw.psit4.martin.db.parameter.Parameter> params = parseParameters(jsonFunct);

            function.setParameter(params);
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
        for (int paramNum = 0; paramNum < jsonParameter.length(); paramNum++) {
            // get parameter
            JSONObject jsonparam = jsonParameter.getJSONObject(paramNum);
            String paramName = jsonparam.getString("Name");
            boolean required = jsonparam.getBoolean("Required");
            String type = jsonparam.getString("Type");
            String regex = jsonparam.getString("Tokens-regex");

            ch.zhaw.psit4.martin.db.parameter.Parameter param =
                    new ch.zhaw.psit4.martin.db.parameter.Parameter();
            param.setName(paramName);
            param.setRequired(required);
            param.setType(type);
            param.setTokensRegex(regex);

            Set<Keyword> keywords = parseKeywords(jsonFunct);
            param.setParameterKeywords(keywords);

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
        for (int keyWordNum = 0; keyWordNum < jsonKeywords.length(); keyWordNum++) {
            Keyword keyword = new Keyword();
            keyword.setKeyword(jsonKeywords.getString(keyWordNum));
            keywords.add(keyword);
        }
        return keywords;
    }
}
