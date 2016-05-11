package ch.zhaw.psit4.martin.aiController;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.requestprocessor.RequestProcessor;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.PluginInformation;
import ch.zhaw.psit4.martin.models.*;
import ch.zhaw.psit4.martin.models.repositories.HistoryItemRepository;

/**
 * This class represents the AIControllerFacade The class follows the Facade
 * Pattern and should wrap the whole subsystem of AIController. This offers an
 * unique Interface to the outside.
 * 
 * @version 0.0.1-SNAPSHOT
 *
 */
public class AIControllerFacade {
    @SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(AIControllerFacade.class);
    
    @Autowired
    private IPluginLibrary pluginLibrary;
    
    @Autowired
    private HistoryItemRepository historyItemRepository;
    
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
        return pluginLibrary.getExampleCalls();
    }
    
    /**
     * 
     * @return A list of the newest History
     *
     * @param amount the amount of historyItems to get
     */
    public List<ExampleCall> getRandomExampleCalls() {
        return pluginLibrary.getRandomExampleCalls();
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
        ExtendedRequest extendedRequest = requestProcessor.extend(request);
        Response response;
        
        if(extendedRequest.getSentence().getPredefinedAnswer() != null){
        	response = new Response(extendedRequest.getSentence().getPredefinedAnswer());
        } else if(extendedRequest.getCalls().size() > 0){
        	response = pluginLibrary.executeRequest(extendedRequest);
        } else {
        	response = new Response("Sorry, I can't understand you.");
        }
        
        historyItemRepository.save(new HistoryItem(request, response));
        
        return response;
    }

    /**
     * 
     * @return all the history of requests with the relative responses
     */
    public List<HistoryItem> getHistory() {
        return historyItemRepository.findAll();
    }
    
    /**
     * 
     * @return A list of the newest History
     *
     * @param amount the amount of historyItems to get
     */
    public List<HistoryItem> getLimitedHistory(int amount) {
    	List<HistoryItem> list = historyItemRepository.getLimitedHistory(new PageRequest(0, amount));
    	List<HistoryItem> shallowCopy = list.subList(0, list.size());
    	Collections.reverse(shallowCopy);
        return shallowCopy;
    }

    public List<PluginInformation> getPluginInformation() {
        return pluginLibrary.getPluginInformation();
    }

    
}
