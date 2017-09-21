package uk.me.riley1.gpx.processor.core;

import java.io.File;

public class GPXFile extends File {

	public GPXFile(String pathname) {
		super(pathname);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MAX_COPIES = 100;

	
	public GPXFile renameFile() {
		
		int x = 1;
		boolean success = false;
		String name = getAbsolutePath();
		GPXFile copy = new GPXFile(getAbsolutePath());
		
		do  {
				name = getAbsolutePath();
				int i = name.lastIndexOf('.');
				if (i > 1 && name.substring(i).length() > 2) {
					String suffix = name.substring(i);
					name = name.substring(0, i) + "(" + String.valueOf(x) + ")" + suffix;
					File test = new File(name);
					success = !test.exists();
				} 
				else {
					name = name + String.valueOf(x);
				}
				
		} while (!success && x++ < MAX_COPIES);
		
		if (x >= MAX_COPIES) {
			copy = null;
		} else {
			File renamed = new File(name);
			renameTo(renamed);
		}
			
		return copy;
	}
}
