package websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import config.ConfigExecuter;

import java.io.IOException;
import java.net.InetSocketAddress;

import model.Config;
import model.Equation;

public class Websocket extends WebSocketServer {

    private WebSocket connection;
    private ConfigExecuter configExecuter;

    public Websocket(ConfigExecuter configExecuter) throws JSONException, IOException {
        super(new InetSocketAddress(configExecuter.read().getPort()));
        setReuseAddr(true);
        this.configExecuter = configExecuter;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    	connection = conn;
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    	connection = null;
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

    	JSONObject json = new JSONObject(message);

		if (json.has("acknowledgment")) {
			
			System.out.println("Connection established");
			JSONObject currentConfig = new JSONObject(this.configExecuter.getConfig());
			connection.send(currentConfig.toString());
			
		} else if (json.has("number1") && json.has("operation") && json.has("number2")) {
			
			System.out.println("New operation received: " + message);
			connection.send(new Equation(
				Integer.parseInt((String) json.get("number1")),
				(String) json.get("operation"), 
				Integer.parseInt((String) json.get("number2")),
				this.configExecuter.getConfig().getMessage()
			).execute());

		} else if (json.has("port") && json.has("message")) {
			
			try {
				this.configExecuter.write(new Config(
					(Integer) json.get("port"), 
					(String) json.get("message")
				));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

	@Override
	public void onStart() {
		System.out.println("Websocket server started on port " + this.getPort());
		
	}
}