package uk.me.riley1.gpx.processor;

public enum UnitConverter {
	 MILES("Mile", 0.000621f), KM("Kilometer", 0.001f), METERS("Meters", 1), FEET("Feet",3.28084f ),
	 YARDS("Yards", 1.093613f), CM("Centimeters", 100), MM("Millimeters", 1000);
	
	private float convFactor;
	private String name;
	private double value;
	
	private UnitConverter(String name, float convFactor) {
		this.name = name;
		this.convFactor = convFactor;
		
	}
	
	public void normalize(double value) {
		
		 this.value = value/convFactor;
	}

	public double convert() {
		return value * convFactor;
	}

}
