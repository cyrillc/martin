package ch.zhaw.psit4.martin.pluginlib;

import java.util.List;
import java.util.Map;

import ch.zhaw.psit4.martin.api.util.Pair;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCall;
import ch.zhaw.psit4.martin.pluginlib.db.function.Function;

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

    /**
     * Answer a request by searching plugin-library for function and executing
     * them.
     * 
     * @param req The {@link ExtendedQequest} to answer.
     * 
     * @return The generated {@link Response}.
     */
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
    public List<Pair<ch.zhaw.psit4.martin.pluginlib.db.plugin.Plugin, Function>> queryFunctionsByKeyword(String keyword);

    /**
     * Get a {@link Map} filled with all required parameters for a plugin and
     * the argument types.
     * 
     * @param plugin
     *            The pluginID to query.
     * @param The
     *            feature designator to query.
     * @return A {@link Map} of arguments with key = ({@link String}) argument
     *         name and value = ({@link String}) Argument type (from
     *         {@link ch.zhaw.psit4.martin.api.types})
     */
    public Map<String, String> queryFunctionArguments(String plugin,
            String feature);

    /**
     * Returns a list of example calls read from the plugin database. Is usually
     * only called from the AI controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of example calls
     */
    public List<ExampleCall> getExampleCalls();
    
    /**
     * Returns a list of 5 randomly choosen example calls read from the plugin database. Is usually
     * only called from the AI controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of 5 randomly choosen example calls
     */
    public List<ExampleCall> getRandomExampleCalls();
}
