package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.json.JSONException;
import org.json.JSONObject;

import model.Config;
import tracker.SignalTracker;

public class ConfigExecuter extends Thread {
	
	private Config config;
	
	public ConfigExecuter() {
		Runnable serverTask = new Runnable() {
			@Override
			public void run() {

				try {
					WatchService watcher = FileSystems.getDefault().newWatchService();

					Path logDir = Paths.get(new File(".").getAbsolutePath());
					logDir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

					while (true) {
						WatchKey key = watcher.take();
						WatchEvent.Kind<?> kind = key.pollEvents().get(0).kind();

						if (StandardWatchEventKinds.ENTRY_MODIFY.equals(kind)) {
							System.out.println("Config has been changed");
							Runtime.getRuntime().exec("kill -HUP " + SignalTracker.getPid());
						}
						key.reset();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		Thread serverThread = new Thread(serverTask);
		serverThread.start();
	}
	
	public Config read() throws JSONException, IOException {
		BufferedReader infile = new BufferedReader(
			new InputStreamReader(
				new FileInputStream("config.json")
			)
		);
        String data = null;
		while ((data = infile.readLine()) != null) { // read and store only line
			JSONObject jsonConfig = new JSONObject(data);
			this.config = new Config(
				(Integer)jsonConfig.get("port"), 
				(String)jsonConfig.get("message")
			);
		}
		infile.close();
		return config;
	}
	
	public void write(Config newConfig) throws IOException {
		JSONObject config = new JSONObject();
        config.put("port", newConfig.getPort());
        config.put("message", newConfig.getMessage());

    	FileWriter file = new FileWriter("config.json");
        file.write(config.toString());
        file.flush();
        file.close();
 
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}
