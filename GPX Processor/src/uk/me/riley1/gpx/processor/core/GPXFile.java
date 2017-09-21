package uk.me.riley1.gpx.processor.core;

import java.io.File;
import java.io.IOException;

public class GPXFile extends File {

	public GPXFile(String pathname) {
		super(pathname);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MAX_COPIES = 100;

	
	public GPXFile createCopy() {
		
		GPXFile copy = null;
		int x = 1;
		boolean success = false;
		do  {
			try {
				
					copy = new GPXFile(getAbsolutePath() + String.valueOf(x));
					success = copy.createNewFile();
				
			} catch (IOException e) {
				//ignore
			}
		} while (!success && x < MAX_COPIES);
		
		if (x >= MAX_COPIES) {
			copy = null;
		}
		return copy;
	}
}
