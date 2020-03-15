package main;

import java.io.IOException;

import config.ConfigExecuter;
import tracker.SignalTracker;

/**
 * Simple calculator server
 * 
 * @author berksevgi95
 * 
 * */
public class Main {

	/*
	 * Here is main method of the application. It creates a SignalTacker
	 * instance which is used for tracking application process signals
	 * and takes a ConfigExecuter instance as a parameter which is needed
	 * for creating a WebSocket connection
	 * */
	public static void main(String[] args) throws IOException {
		try {
			new SignalTracker(new ConfigExecuter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


