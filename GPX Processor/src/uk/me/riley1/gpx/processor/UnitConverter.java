package uk.me.riley1.gpx.processor;

import javax.naming.directory.InvalidAttributesException;

public class UnitConverter {
	

	public static int MILES = 0;
	public static int KM = 1;
	public static int METERS = 2;
	public static int FEET = 3;
	public static int YARDS = 4;
	public static int CM = 5;
	public static int MM = 6;
	private static int LOWEST = MILES;
	private static int HIGHEST = MM;
	private static double factors[]= {0.000621, 0.001, 1, 3.28084, 1.093613, 100, 1000};
	private int unit;
	private double convFactor;
	private double value;

	public UnitConverter(int unit, double value) throws InvalidAttributesException {
		
		if (unit < LOWEST || unit > HIGHEST) {
			throw new InvalidAttributesException("Either 'from unit' or 'to unit' or both attribute(s) are invalid");
		}
		
		setUnit(unit);
		normalize(value);
	}
	
	public void normalize(double value) {
		
		 this.value = value/convFactor;
	}
	
	public double convert() {
		
		return value * convFactor;
	}
	
	public int getUnit() {
		return this.unit;
	}
	
	public void setUnit(int unit) {
		this.unit = unit;
		convFactor = factors[unit];

	}
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
