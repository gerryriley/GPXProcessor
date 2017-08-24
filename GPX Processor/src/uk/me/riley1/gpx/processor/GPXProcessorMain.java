package uk.me.riley1.gpx.processor;

import java.io.File;

public class GPXProcessorMain {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.print("No Filename specified to process. exiting...");
			System.exit(-1);
		}
		String fileName = args[0];
		File file = new File(fileName);
		GPXProcessor proc = new GPXProcessor(file);
		proc.removeMarkers();
		proc.addMarkers(false);
		proc.generateOutput(file);
	}
}
