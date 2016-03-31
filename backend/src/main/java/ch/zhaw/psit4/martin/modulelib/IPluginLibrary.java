package ch.zhaw.psit4.martin.modulelib;

import java.util.List;

/**
 * Interface for class <code>PluginLibrary</code>.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */
public interface IPluginLibrary {
    void startLibrary();

    <T> List<T> fetchPlugins(final String extPointId);
}
