package ch.zhaw.psit4.martin.pluginlib;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.WorkItem;

/**
 * The MArtIn Context Access object hidden from MArtIn plugins.
 * 
 * This class handles communication of a plugin with the main application by
 * providing the application with an object, that MArtIn is aware of. This
 * implementation of the Context has full access rights to the queue.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */
public class MartinContextAccessor implements IMartinContext {
    /*
     * the work list.
     */
    private Queue<WorkItem> queue;
    /*
     * The id-counter of this class
     */
    private AtomicLong idCounter;
    /*
     * Log from the common logging api
     */
    private Log log;


    public MartinContextAccessor() {
        queue = new PriorityQueue<WorkItem>();
        idCounter = new AtomicLong();
        log = LogFactory.getLog(MartinContextAccessor.class);
    }

    /**
     * Registers a {@link WorkItem} in the context.
     * 
     * @param item
     *            The {@link WorkItem} to register.
     */
    public void registerWorkItem(WorkItem item) {
        try {
            item.setID(getnextID());
            queue.add(item);
        } catch (Exception e) {
            log.error(e.getMessage());
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
    public WorkItem fetchWorkItem() {
        return queue.poll();
    }
    
    private long getnextID() {
        return idCounter.getAndIncrement();
    }
}
