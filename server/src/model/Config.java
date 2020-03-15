package model;

public class Config {
	
	private Integer port;
	private String message;
	
	public Config(Integer port, String message) {
		this.port = port;
		this.message = message;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
