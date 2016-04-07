package ch.zhaw.psit4.martin.aiController;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ch.zhaw.psit4.martin.boot.MartinBoot;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.common.dao.HistoryItemDAO;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCall;

import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.HistoryItem;
import ch.zhaw.psit4.martin.requestProcessor.RequestProcessor;

/**
 * This class represents the AIControllerFacade The class follows the Facade
 * Pattern and should wrap the whole subsystem of AIController. This offers an
 * unique Interface to the outside.
 * 
 * @version 0.0.1-SNAPSHOT
 *
 */
public class AIControllerFacade {

    private RequestProcessor requestProcessor;
    private IPluginLibrary library;

    public AIControllerFacade() {
        this.requestProcessor = new RequestProcessor();
        this.requestProcessor.setLibrary(MartinBoot.getPluginLibrary());
    }

    /**
     * Returns a list of example calls from the plugin library. Is usually only
     * called from the frontend controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of example calls
     */
    public List<ExampleCall> getExampleCalls() {
        IPluginLibrary pluginLibrary = MartinBoot.getPluginLibrary();
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
        try {
            ExtendedRequest extendedRequest = this.requestProcessor
                    .extend(request);
            Response response = library.executeRequest(extendedRequest);
            return response;
        } catch (Exception e) {
            Response response = new Response("Sorry, I cant't understand you.");
            return response;
        }
    }
}
