package ch.zhaw.psit4.martin.api.types;

public class Location implements IMartinType {
	private boolean instance = false;
	
	private String location;

	@Override
	public void fromString(String data) throws MartinTypeInstanciationException {
		this.location = data;
		this.instance = true;
	}

	@Override
	public boolean isInstancaeableWith(String data) {
		// TODO: Replace static dictionary with a dynamic one
		switch(data){
			case "Zürich":
			case "Chur":
				return true;
		}
		
		return false;
	}
	
	@Override
	public String toString(){
		return location;
	}

	@Override
	public boolean isInstance() {
		return instance;
	}
}
