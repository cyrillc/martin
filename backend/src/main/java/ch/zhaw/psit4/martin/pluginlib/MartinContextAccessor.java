package ch.zhaw.psit4.martin.pluginlib;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.aiController.MOutputQueueThread;
import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.types.output.MOutput;

/**
 * The MArtIn Context Access object hidden from MArtIn plugins.
 * 
 * This class handles communication of a plugin with the main application by
 * providing the application with an object, that MArtIn is aware of. This
 * implementation of the Context has full access rights to the queue.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class MartinContextAccessor implements IMartinContext {
    /*
     * The id-counter of this class
     */
    private AtomicLong idCounter;
    /*
     * Log from the common logging api
     */
    private static final Log LOG = LogFactory
            .getLog(MartinContextAccessor.class);
    
    /*
     * Thread safe list implementation.
     */
    private CopyOnWriteArrayList<Feature> featureQueue;
    @Autowired
    private MOutputQueueThread outputQueue;

    public MartinContextAccessor() {
        featureQueue = new CopyOnWriteArrayList<>();
        idCounter = new AtomicLong();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.zhaw.psit4.martin.api.IMartinContext#registerWorkItem(ch.zhaw.psit4.
     * martin.api.Feature)
     */
    @Override
    public void registerWorkItem(Feature item) {
        try {
            item.setID(getnextID());
            featureQueue.add(item);
        } catch (Exception e) {
            LOG.error("An error occured at registerWorkItem()", e);
        }
    }

    /**
     * Clears the work list.
     */
    public void clearWorkList() {
        featureQueue.clear();
    }

    /**
     * Retrieves and removes the head of the {@link WorkItem} queue, or returns
     * {@code null} if this queue is empty.
     * 
     * @return the head of the {@link WorkItem} queue, or {@code null} if this
     *         queue is empty
     */
    public Feature fetchWorkItem(long requestID) {
        for (int i = 0; i < featureQueue.size(); i++) {
            if (featureQueue.get(i).getRequestID() == requestID)
                return featureQueue.remove(i);
        }
        return null;
    }

    @Override
    public void addToOutputQueue(List<MOutput> output) {
        outputQueue.addToOutputQueue(output);
    }

    public int getNumberOfFeatures() {
        return featureQueue.size();
    }

    private long getnextID() {
        return idCounter.getAndIncrement();
    }

}
