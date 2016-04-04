package ch.zhaw.psit4.martin.aiController;

import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;

/**
 * This class represents the AIControllerFacade The class follows the Facade
 * Pattern and should wrap the whole subsystem of AIController. This offers an
 * unique Interface to the outside.
 * 
 * @version 0.0.1-SNAPSHOT
 *
 */
public class AIControllerFacade {
    
    public Response elaborateRequest(Request request){
        return new Response("Command: '" + request.getCommand() + "' received in AIController!");
    }    
}
