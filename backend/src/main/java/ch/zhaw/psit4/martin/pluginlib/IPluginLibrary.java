package ch.zhaw.psit4.martin.pluginlib;

import java.util.List;

import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.PluginInformation;
import ch.zhaw.psit4.martin.models.*;

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

    public List<PluginInformation> getPluginInformation();
}
