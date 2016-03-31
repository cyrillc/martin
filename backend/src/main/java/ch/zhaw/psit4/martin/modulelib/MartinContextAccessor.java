package ch.zhaw.psit4.martin.modulelib;

import java.util.LinkedList;
import java.util.List;

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
    private List<WorkItem> list;
    
    public MartinContextAccessor() {
        list = new LinkedList<WorkItem>();
    }
    
    /**Registers a work item in the context.
     * @param item The item to register.
     */
    public void registerWorkItem(WorkItem item) {
        list.add(item);
    }
    
    /**
     * Clears the work list.
     */
    public void clearWorkList() {
        list.clear();
    }
}
