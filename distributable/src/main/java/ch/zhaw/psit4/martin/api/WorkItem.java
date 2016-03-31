package ch.zhaw.psit4.martin.api;

/**
 * Work object used by {@link IMartinContext} with callbacks.
 * 
 * Martin calls the methods of registered WorkItems if the AIController
 * allocates a timeslot for them. Workitems allow a plugin to do work
 * asynchronously.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */
public class WorkItem {
    
    /**
     * Called if the work is started by MArtIn.
     * @throws Exception An excpetion occured during work start.
     */
    public void onWorkStarted() throws Exception {}

    /**
     * Main method called by MArtIn.
     * @throws Exception An excpetion occured during work.
     */
    public void doWork() throws Exception {}

    /**
     * Called if the work has ended.
     * @throws Exception An excpetion occured during work end.
     */
    public void onWorkDone()throws Exception {}
}
