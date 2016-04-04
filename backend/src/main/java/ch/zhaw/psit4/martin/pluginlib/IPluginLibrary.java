package ch.zhaw.psit4.martin.pluginlib;

import java.util.List;
import java.util.Map;

import javax.naming.ldap.ExtendedRequest;

import ch.zhaw.psit4.martin.api.util.Pair;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCall;

/**
 * Interface for class <code>PluginLibrary</code>.
 *
 * @version 0.0.1-SNAPSHOT
 */
public interface IPluginLibrary {
    /*
     * Start the module and initialize components
     */
    void startLibrary();

    // TODO: use ExtendedRequest from MArtin common package
    public Response executeRequest(ExtendedRequest req);

    /**
     * Querry all plugins by keyword and return matching pluginIDs.
     * 
     * @param keyword
     *            The keyword to search.
     * @return {@link Pair} of found plugins sorted by probability (highest
     *         first). The first element is the Plugin ID the second is the
     *         feature ID
     */
    public List<Pair<String, String>> queryFunctionsByKeyword(String keyword);

    /**
     * Get a {@link Map} filled with all required parameters for a plugin and
     * the argument types.
     * 
     * @param plugin
     *            The pluginID to querry.
     * @param The
     *            feature designator to querry.
     * @return A {@link Map} of arguments with key = ({@link String}) argument
     *         name and value = ({@link String}) Argument type (from
     *         {@link ch.zhaw.psit4.martin.api.types})
     */
    public Map<String, String> queryFunctionArguments(String plugin, String feature);
    
    /**
     * Returns a list of example calls read from the plugin database. Is usually
     * only called from the AI controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of example calls
     */
    public List<ExampleCall> getExampleCalls();
}
