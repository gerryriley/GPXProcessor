package uk.me.riley1.gpx.processor.core;


public class GPXProcessorMain {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.print("No Filename specified to process. exiting...");
			System.exit(-1);
		}
		String fileName = args[0];
		GPXFile file = new GPXFile(fileName);
		String direction = "backward";
		if (args.length > 1) {
			direction = args[1];
		}
		GPXProcessor proc;
		try {
			proc = new GPXProcessor(file, direction);
			proc.removeMarkers();
			proc.addMarkers();
			proc.generateOutput();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
