package ch.zhaw.psit4.martin;

public class HelloMartin {
	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public void getMessagePrint() {
		System.out.println("Your Message : " + message);
	}

	public String getMessage() {
		return message;
	}
}
