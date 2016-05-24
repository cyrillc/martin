package zhaw.picturePlugin;


import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MartinPlugin;



public class PicturePlugin implements MartinPlugin {

    private IMartinContext context;
    private boolean active;


    @Override
    public void activate(IMartinContext context) throws Exception {
        this.context = context;
        this.active = true;
    }


    @Override
    public void initializeRequest(String feature, long requestID) throws Exception {
        if (active) {
            if(feature.equals("picture")) {
                Feature work = new PictureWork(requestID);
                context.registerWorkItem(work);
            }
            
        }
    }

    @Override
    public void deactivate() throws Exception {
        this.active = false;
    }



}
