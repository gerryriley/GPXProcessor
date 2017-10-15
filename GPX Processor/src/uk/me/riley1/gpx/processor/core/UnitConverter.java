package uk.me.riley1.gpx.processor.core;

public enum UnitConverter {
	 MILES("Mile", 0.000621f), KM("Kilometer", 0.001f), METERS("Meters", 1), FEET("Feet",3.28084f ),
	 YARDS("Yard", 1.093613f), CM("Centimeter", 100), MM("Millimeter", 1000);
	
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

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
