package observerTest;

import escape.GameObserver;

public class TestObserver implements GameObserver {
	
	public TestObserver() {}
	
	private String currentMessage = null;
	
	public String getMessage() {
		return currentMessage;
	}
	
	@Override
	public void notify(String message) {
		currentMessage = message;
		System.out.println(message);
	}

	@Override
	public void notify(String message, Throwable cause) {
		currentMessage = (cause + " : " + message);
		System.out.println(cause + " : " + message);
	}
}
