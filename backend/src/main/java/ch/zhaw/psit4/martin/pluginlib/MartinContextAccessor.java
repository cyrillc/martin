package ch.zhaw.psit4.martin.pluginlib;

import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.IMartinContext;

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
     * the work list.
     */
    private List<Feature> queue;
    /*
     * The id-counter of this class
     */
    private AtomicLong idCounter;
    /*
     * Log from the common logging api
     */
    private static final Log LOG = LogFactory
            .getLog(MartinContextAccessor.class);

    public MartinContextAccessor() {
        queue = new LinkedList<Feature>();
        idCounter = new AtomicLong();
    }

    /**
     * Registers a {@link WorkItem} in the context.
     * 
     * @param item
     *            The {@link WorkItem} to register.
     */
    public void registerWorkItem(Feature item) {
        try {
            item.setID(getnextID());
            queue.add(item);
        } catch (Exception e) {
            LOG.error("An error occured at registerWorkItem()", e);
        }
    }

    /**
     * Clears the work list.
     */
    public void clearWorkList() {
        queue.clear();
    }

    /**
     * Retrieves and removes the head of the {@link WorkItem} queue, or returns
     * {@code null} if this queue is empty.
     * 
     * @return the head of the {@link WorkItem} queue, or {@code null} if this
     *         queue is empty
     */
    public Feature fetchWorkItem(long requestID) {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getRequestID() == requestID)
                return queue.remove(i);
        }
        return null;
    }
    
    public int getNumberOfFeatures() {
        return queue.size();
    }
    
    private long getnextID() {
        return idCounter.getAndIncrement();
    }
}
