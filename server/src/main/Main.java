package main;

import java.io.IOException;

import config.ConfigExecuter;
import tracker.SignalTracker;

public class Main {

	public static void main(String[] args) throws IOException {
		try {
			new SignalTracker(new ConfigExecuter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


