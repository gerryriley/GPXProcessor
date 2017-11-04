package uk.me.riley1.gpx.processor.core;

import uk.me.riley1.gpx.processor.ui.ConfigurationWindow.Configuration;

public class GPXProcessorMain {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.print("No Filename specified to process. exiting...");
			System.exit(-1);
		}
		String fileName = args[0];
		String direction = "backward";
		if (args.length > 1) {
			direction = args[1];
		}
		GPXProcessor proc = null;
		Configuration config = new Configuration();
		config.put(Configuration.GPX_FILE, new GPXFile(fileName));
		config.put(Configuration.DIRECTION, direction);
		try {
			proc = new GPXProcessor(config);
			proc.removeMarkers();
			proc.addMarkers();
			proc.generateOutput();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
