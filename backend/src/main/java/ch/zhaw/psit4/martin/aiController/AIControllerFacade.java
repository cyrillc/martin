package ch.zhaw.psit4.martin.aiController;

import java.util.List;

import javax.annotation.PostConstruct;

import ch.zhaw.psit4.martin.boot.MartinBoot;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.common.service.HistoryItemService;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCall;
import ch.zhaw.psit4.martin.pluginlib.db.ExampleCallService;
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
    @PostConstruct
    public void postAIControllerFacade() {}

    /**
     * Returns a list of example calls from the plugin library. Is usually only
     * called from the frontend controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of example calls
     */

    public List<ExampleCall> getExampleCalls() {

        IPluginLibrary library = (IPluginLibrary) MartinBoot.context
                .getBean("IPluginLibrary");
        return library.getExampleCalls();
    }

    /**
     * This method respond to a request with a response. Try to understand what
     * it requested and elaborate an appropiate response for the request.
     * 
     * @param request
     *            Request containing a string command
     * @return the response of the AI.
     */
    public Response elaborateRequest(Request request) {
        try {
            IPluginLibrary lib = (IPluginLibrary) MartinBoot.context
                    .getBean("IPluginLibrary");
            RequestProcessor requestProcessor = (RequestProcessor) MartinBoot.context
                    .getBean("RequestProcessor");
            HistoryItemService historyItemService = (HistoryItemService) MartinBoot.context
                    .getBean("historyItemService");
            ExtendedRequest extendedRequest = requestProcessor.extend(request);
            Response response = lib.executeRequest(extendedRequest);
            historyItemService.addHistoryItem(new HistoryItem(request, response));
            return response;
        } catch (Exception e) {
            return new Response("Sorry, I can't understand you.");
        }
    }
}
