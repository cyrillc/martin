package zhaw.picturePlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MEventListener;
import ch.zhaw.psit4.martin.api.types.MEventData;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;
import zhaw.picturePlugin.plugin.ImageSearch;

public class PicturePush extends MEventListener {

    private IMartinContext context;
    
    public PicturePush(IMartinContext context) {
        this.context = context;
    }
    @Override
    public void handleEvent(MEventData event) {
        ImageSearch imageSearch = new ImageSearch();
        List<MOutput> response = new ArrayList<>();
        try {
            String image = imageSearch.getImage("clockwork");
            response.add(new MOutput(MOutputType.IMAGE, image));
            context.addToOutputQueue(response);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
