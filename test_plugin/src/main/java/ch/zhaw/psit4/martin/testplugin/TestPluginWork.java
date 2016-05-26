package ch.zhaw.psit4.martin.testplugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MPerson;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;

public class TestPluginWork extends Feature {

	public TestPluginWork(long requestID) {
		super(requestID);
	}

	private Boolean person1IsMe = false;
	private MPerson person1;
	private MPerson person2;

	private static String MY_NAME = "Martin";

	@Override
	public void initialize(Map<String, IBaseType> args) throws Exception {
		person1 = (MPerson) args.get("name1");

		if (person1.toString().equalsIgnoreCase(MY_NAME)) {
			person1IsMe = true;
		}

		if (args.get("name2") != null) {
			person2 = (MPerson) args.get("name2");

			if (person2.toString().equalsIgnoreCase(MY_NAME)) {
				person1 = person2;
				person2 = (MPerson) args.get("name2");
			}
		}
	}

	@Override
	public List<MOutput> execute() throws Exception {
		List<MOutput> answer = new ArrayList<>();

		if (person2 == null && !person1IsMe) {
			answer.add(new MOutput(MOutputType.TEXT, "Who is " + person1 + "?"));
		} else if (person2 == null && person1IsMe) {
			answer.add(new MOutput(MOutputType.TEXT, "Hello my friend!"));
		} else if (person1IsMe) {
			answer.add(new MOutput(MOutputType.TEXT, "Hello " + person2 + ", it's me " + person1 + "!"));
		} else {
			answer.add(new MOutput(MOutputType.TEXT, "Hello " + person1 + " and " + person2 + "!"));
		}

		return answer;
	}
}
