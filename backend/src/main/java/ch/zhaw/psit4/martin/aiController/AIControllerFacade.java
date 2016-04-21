package ch.zhaw.psit4.martin.aiController;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.common.service.HistoryItemService;
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
    private static final Log LOG = LogFactory.getLog(AIControllerFacade.class);
    
    @Autowired
    private IPluginLibrary library;
    
    @Autowired
    private HistoryItemService historyItemService;
    
    @Autowired
    private RequestProcessor requestProcessor;

    @PostConstruct
    public void postAIControllerFacade() {
        // does nothing. Is it needed b'cause of beans.xml?
    }

    /**
     * Returns a list of example calls from the plugin library. Is usually only
     * called from the frontend controller when the user first loads the MArtIn
     * frontend.
     * 
     * @return a list of example calls
     */

    public List<ExampleCall> getExampleCalls() {
        return library.getExampleCalls();
    }
    
    /**
     * 
     * @return A list of the newest History
     *
     * @param amount the amount of historyItems to get
     */
    public List<ExampleCall> getRandomExampleCalls() {
        return library.getRandomExampleCalls();
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
            ExtendedRequest extendedRequest = requestProcessor.extend(request);
            Response response = library.executeRequest(extendedRequest);
            historyItemService
                    .addHistoryItem(new HistoryItem(request, response));
            return response;
        } catch (Exception e) {
        	LOG.info(e);
            Response response = new Response("Sorry, I can't understand you.");
            historyItemService
                    .addHistoryItem(new HistoryItem(request, response));
            return response;
        }
    }

    /**
     * 
     * @return all the history of requests with the relative responses
     */
    public List<HistoryItem> getHistory() {
        return historyItemService.getHistory();
    }
    
    /**
     * 
     * @return A list of the newest History
     *
     * @param amount the amount of historyItems to get
     */
    public List<HistoryItem> getLimitedHistory(int amount) {
        return historyItemService.getLimitedHistory(amount);
    }
}
