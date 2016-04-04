package ch.zhaw.psit4.martin.pluginlib;

import java.util.List;
import java.util.Map;

/**
 * Interface for class <code>PluginLibrary</code>.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */
public interface IPluginLibrary {
    /*
     * Start the module and initialize components
     */
    void startLibrary();

    /**
     * Querry all plugins by keyword and return matching pluginIDs.
     * 
     * @param keyword
     *            The keyword to search.
     * @return {@link ArrayList} of found plugins sorted by probability (highest
     *         first).
     */
    List<String> queryPluginsByKeyword(String keyword);

    /**
     * Get a {@link Map} filled with all required parameters for a plugin and
     * the argument types.
     * 
     * @param pluginID
     *            The pluginID to querry.
     * @return A {@link Map} of arguments with key = ({@link String}) argument
     *         name and value = ({@link String}) Argument type (from
     *         {@link ch.zhaw.psit4.martin.api.types})
     */
    <T> Map<String, String> queryPluginArguments(String pluginID);

    /*
     * Fetches all extensions for the given extension point qualifiers.
     * 
     * @param extPointId The extension point id to gather plugins for
     * 
     * @return The gathered plugins in a LinkedList
     */
    <T> List<T> fetchPlugins(final String extPointId);
}
