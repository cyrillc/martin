package zhaw.picturePlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MNominalModifier;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;
import zhaw.picturePlugin.plugin.ImageSearch;

public class PictureWork extends Feature {

	private ImageSearch image;
	private String imageType;

	public PictureWork(long requestID) {
		super(requestID);
		image = new ImageSearch();
	}

	@Override
	public void initialize(Map<String, IBaseType> args) throws Exception {
		MNominalModifier text = (MNominalModifier) args.get("picture");
		this.imageType = text.getData();
	}

	@Override
	public List<MOutput> execute() throws Exception {
		List<MOutput> response = new ArrayList<>();
		String apiResponse = null;
		response.add(new MOutput(MOutputType.HEADING, "Picture of " + imageType));
		if (!this.imageType.isEmpty()) {
			apiResponse = image.getImageFromPixelBay(imageType);
			if(apiResponse == ""){
				apiResponse = image.getImage(imageType);
			}
			if (apiResponse == null) {
				apiResponse = "No picture found for " + imageType;
				response.add(new MOutput(MOutputType.TEXT, apiResponse));
			} else {
				response.add(new MOutput(MOutputType.IMAGE, apiResponse));
			}
		}

		return response;
	}
}
