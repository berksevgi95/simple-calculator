package tracker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* Vulnerable packages */
import sun.misc.Signal;
import sun.misc.SignalHandler;

import websocket.Websocket;
import config.ConfigExecuter;

/*
 * SignalTracker class is primarily used for creating a WebSocket connection
 * and tracking main process of the application. If the application fires up a 
 * SIGHUP signal, then restarts that connection
 * */
public class SignalTracker extends Thread {
	
	private ConfigExecuter configExecuter;
	private Websocket websocket;
	
	/*
	 * Initialize method just destroys the current connection and creates a new one
	 * */
	public void initialize() throws IOException, InterruptedException {
		
		if(websocket != null) {
			websocket.stop();
		}
		websocket = new Websocket(configExecuter);
		websocket.start();
	}
	
	/*
	 * Process tracker tracks SIGHUP signal. If a signal occurs, then 
	 * reinitialises the connection
	 * */
	public void track(String name) {
		Signal signal = new Signal(name);
		Signal.handle(signal, new SignalHandler() {
			@Override
			public void handle(Signal arg0) {
				if (signal.toString().trim().equals("SIGHUP")) {
					try {
						initialize();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}
		});
	}
	
	/*
	 * SignalTracker keeps awake itself till the application ends off
	 * */
	public SignalTracker(ConfigExecuter configExecuter) throws IOException, InterruptedException {

		this.configExecuter = configExecuter;
		track("HUP");
		this.initialize();

		while (true) {
			Thread.sleep(1000);
		}
	}
	
	/*
	 * Static function for getting PID of the main process
	 * */
	public static String getPid() throws IOException {
	    byte[] bo = new byte[256];
	    InputStream is = new FileInputStream("/proc/self/stat");
	    is.read(bo);
	    for (int i = 0; i < bo.length; i++) {
	        if ((bo[i] < '0') || (bo[i] > '9')) {
	        	is.close();
	            return new String(bo, 0, i);
	        }
	    }
	    is.close();
	    return "-1";
	}
	
}
