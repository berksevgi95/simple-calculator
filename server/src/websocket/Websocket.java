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

/*
 * WebSocket class is used for creating a WebSocket connection between client
 * and server. Before creating a connection, it reads configurations from the 
 * config file on file system, then creates the connection according to these 
 * configurations
 * */
public class Websocket extends WebSocketServer {

    private WebSocket connection;
    private ConfigExecuter configExecuter;

    /*
     * WebSocket constructor fetches configurations,then creates a connection 
     * with using InetSocketAddress class
     * */
    public Websocket(ConfigExecuter configExecuter) throws JSONException, IOException {
        super(new InetSocketAddress(configExecuter.read().getPort()));
        setReuseAddr(true);
        this.configExecuter = configExecuter;
    }

    /*
     * An event fired on the time of establishing a connection
     * */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    	connection = conn;
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    /*
     * An event fired on the time of destroying current connection
     * */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    	connection = null;
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    /*
     * An event fired on the time of handshaking and sending message 
     * through current connection
     * */
    @Override
    public void onMessage(WebSocket conn, String message) {

    	JSONObject json = new JSONObject(message);

		if (json.has("acknowledgment")) {
			
			/*
	    	 * If a connection is established, client sends an acknowledgement signal
	    	 * to the server. At that time, server responses to the client with sending
	    	 * its configurations
	    	 * */
			System.out.println("Connection established");
			JSONObject currentConfig = new JSONObject(this.configExecuter.getConfig());
			connection.send(currentConfig.toString());
			
		} else if (json.has("number1") && json.has("operation") && json.has("number2")) {
			
			/*
			 * If client sends an equation, server will solve that equation by creating 
			 * a new Equation instance
			 * */
			System.out.println("New operation received: " + message);
			connection.send(new Equation(
				Integer.parseInt((String) json.get("number1")),
				(String) json.get("operation"), 
				Integer.parseInt((String) json.get("number2")),
				this.configExecuter.getConfig().getMessage()
			).execute());

		} else if (json.has("port") && json.has("message")) {
			
			/*
			 * If server receives configuration object from client, it writes new 
			 * configurations to the config file
			 * */
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

    /*
     * An event fired on the time of occur an error
     * */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    /*
     * An event fired on the time of a WebSocket instance is online
     * */
	@Override
	public void onStart() {
		System.out.println("Websocket server started on port " + this.getPort());
		
	}
}