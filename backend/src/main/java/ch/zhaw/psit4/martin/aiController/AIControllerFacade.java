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
import ch.zhaw.psit4.martin.timing.TimingInfoLogger;
import ch.zhaw.psit4.martin.timing.TimingInfoLoggerFactory;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.PluginInformation;
import ch.zhaw.psit4.martin.frontend.FrontendController;
import ch.zhaw.psit4.martin.models.*;
import ch.zhaw.psit4.martin.models.repositories.MHistoryItemRepository;

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
    private static final TimingInfoLogger TIMING_LOG = TimingInfoLoggerFactory
            .getInstance();

    @Autowired
    private IPluginLibrary pluginLibrary;

    @Autowired
    private MHistoryItemRepository historyItemRepository;

    @Autowired
    private RequestProcessor requestProcessor;

    @Autowired
    private FrontendController frontend;

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

    public List<MExampleCall> getExampleCalls() {
        return pluginLibrary.getExampleCalls();
    }

    /**
     * 
     * @return A list of the newest History
     *
     * @param amount
     *            the amount of historyItems to get
     */
    public List<MExampleCall> getRandomExampleCalls() {
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
    public MResponse elaborateRequest(MRequest request) {
        TIMING_LOG.logStart(this.getClass().getSimpleName());

        MResponse response = new MResponse();

        TIMING_LOG.logEnd(this.getClass().getSimpleName());
        ExtendedRequest extendedRequest = requestProcessor.extend(request,
                response);
        TIMING_LOG.logStart(this.getClass().getSimpleName());

        if (extendedRequest.getSentence().getPredefinedAnswer().size() > 0) {
            extendedRequest.getResponse().setResponseList(extendedRequest.getSentence().getPredefinedAnswer());
        } else if (extendedRequest.getCalls().size() > 0) {
            TIMING_LOG.logEnd(this.getClass().getSimpleName());
            extendedRequest
                    .setResponse(pluginLibrary.executeRequest(extendedRequest));
            TIMING_LOG.logStart(this.getClass().getSimpleName());
        } else {
            extendedRequest.getResponse().setSingleResponse(MOutputType.TEXT,
                    "Sorry, I can't understand you.");
        }

        historyItemRepository.save(new MHistoryItem(
                extendedRequest.getRequest(), extendedRequest.getResponse()));

        TIMING_LOG.logEnd(this.getClass().getSimpleName());
        frontend.sendOutputToConnectedClients(
                extendedRequest.getResponse().getResponses());
        return extendedRequest.getResponse();
    }

    /**
     * 
     * @return all the history of requests with the relative responses
     */
    public List<MHistoryItem> getHistory() {
        return historyItemRepository.findAll();
    }

    /**
     * 
     * @return A list of the newest History
     *
     * @param amount
     *            the amount of historyItems to get
     */
    public List<MHistoryItem> getLimitedHistory(int amount) {
        List<MHistoryItem> list = historyItemRepository
                .getLimitedHistory(new PageRequest(0, amount));
        List<MHistoryItem> shallowCopy = list.subList(0, list.size());
        Collections.reverse(shallowCopy);
        return shallowCopy;
    }

    public List<PluginInformation> getPluginInformation() {
        return pluginLibrary.getPluginInformation();
    }

}
