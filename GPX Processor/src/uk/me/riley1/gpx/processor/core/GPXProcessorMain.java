package uk.me.riley1.gpx.processor.core;

import java.io.File;

public class GPXProcessorMain {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.print("No Filename specified to process. exiting...");
			System.exit(-1);
		}
		String fileName = args[0];
		File file = new File(fileName);
		Boolean forward = false;
		if (args.length > 1) {
			forward = args[1].equalsIgnoreCase("forward");
		}
		GPXProcessor proc;
		try {
			proc = new GPXProcessor(file);
			proc.removeMarkers();
			proc.addMarkers(forward);
			proc.generateOutput(file);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
