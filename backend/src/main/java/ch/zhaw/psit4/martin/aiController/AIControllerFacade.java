package ch.zhaw.psit4.martin.aiController;

import java.util.List;

import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.pluginlib.PluginLibrary;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCall;

/**
 * This class represents the AIControllerFacade The class follows the Facade
 * Pattern and should wrap the whole subsystem of AIController. This offers an
 * unique Interface to the outside.
 * 
 * @version 0.0.1-SNAPSHOT
 * @author marco
 *
 */
public class AIControllerFacade {
    
    /**
     * Returns a list of example calls from the plugin library. Is usually
     * only called from the frontend controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of example calls
     */
    public List<ExampleCall> getExampleCalls(){
        PluginLibrary pluginLibrary = new PluginLibrary();
        return pluginLibrary.getExampleCalls();
    }

    /**
     * This method respond to a request with a response. Try to understand what
     * it requested and elaborate an appropiate response for the request.
     * 
     * @param request
     * @return the response of the AI.
     */
    public Response elaborateRequest(Request request) {
        return new Response("Command: '" + request.getCommand()
                + "' received in AIController!");
    }
}
