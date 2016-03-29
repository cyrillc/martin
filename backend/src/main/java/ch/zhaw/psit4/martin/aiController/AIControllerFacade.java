package ch.zhaw.psit4.martin.aiController;

/**
 * @author marco
 *
 */
public class AIControllerFacade {
    private Request request;
    private RequestAnalyzer resquestAnalyzer;
    private Modul modul;

    /**
     * 
     */
    public AIControllerFacade() {
        // TODO Auto-generated constructor stub
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Modul getModul() {
        return modul;
    }

    public void setModul(Modul modul) {
        this.modul = modul;
    }
    
    
    

}
