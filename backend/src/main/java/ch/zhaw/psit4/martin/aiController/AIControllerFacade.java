package ch.zhaw.psit4.martin.aiController;

import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;

/**
 * This class represents the AIControllerFacade The class follows the Facade
 * Pattern and should wrap the whole subsystem of AIController. This offers an
 * unique Interface to the outside.
 * 
 * @version 1.0
 * @author marco
 *
 */
public class AIControllerFacade {

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
