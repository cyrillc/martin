package zhaw.picturePlugin;

import zhaw.picturePlugin.plugin.ImageSearch;
import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;

public class PictureWork extends Feature {

    private ImageSearch image;
    private String imageType;

    public PictureWork(long requestID) {
        super(requestID);
        image = new ImageSearch();
    }

    @Override
    public void initialize(Map<String, IBaseType> args) throws Exception  {
        MText text = (MText) args.get("picture")
        this.imageType = text.getData();       
    }

    @Override
    public String execute() throws Exception {
        String response = null;
        if (!this.imageType.isEmpty()) {
            response = image.getImage(imageType);
        }
        if (response == null) {
            response = "No picture found for " + imageType;
        }
        return response;
    }
}
