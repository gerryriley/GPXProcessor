package uk.me.riley1.gpx.processor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class GPXWayPoint {
	
	private static final double RADIUS_OF_EARTH = 6371e3;
	private static final String MARKER_SYMBOL = "Pin";
	private static final String NORMAL_SYMBOL = "TinyDot";
	private static final int DEGREES_IN_A_CIRCLE = 360;
	private Element Element;
	private boolean forward;

	public GPXWayPoint(Element wayPoint, boolean forward) {
		
		this.Element = wayPoint;
		this.forward = forward;				
	}

	/**
	 * Returns the distance between this and another geographic point in meters
	 * @param otherPoint the distance is calculated from this point to the other {@link GPXWayPoint}
	 * @return a double representing the distance between this and the other {@link GPXWayPoint}
	 */
	public double getDistanceBetweenPoints(GPXWayPoint otherPoint) {
		
		Element fromPt = getElement();
		Element toPt = otherPoint.getElement();
		double distance = 0;
		double lat1 = Double.parseDouble(fromPt.getAttribute("lat"));
		double lat2 = Double.parseDouble(toPt.getAttribute("lat"));
		double lon1 = Double.parseDouble(fromPt.getAttribute("lon"));
		double lon2 = Double.parseDouble(toPt.getAttribute("lon"));
		double lat1r = Math.toRadians(lat1);
		double lat2r = Math.toRadians(lat2);
		double deltaLat = Math.toRadians(lat2 - lat1);
		double deltaLon = Math.toRadians(lon2 - lon1);
		
		// This calculation takes two latitudes in radians and the differences between latitudes and longitudes
		// and calculates the straight line great circle distance using the haversine formula see
		// http://www.movable-type.co.uk/scripts/latlong.html
		
		double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
		        Math.cos(lat1r) * Math.cos(lat2r) *
		        Math.sin(deltaLon/2) * Math.sin(deltaLon)/2;
		
		distance = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)) * RADIUS_OF_EARTH;
			
		return distance;
	}
	
	/**
	 * Returns the bearing between this and another geographic point
	 * @param otherPoint the bearing is calculated from this point to the other {@link GPXWayPoint}
	 * @return a double representing the angular bearing between this and the other {@link GPXWayPoint}
	 *  in radians between Pi and -Pi
	 */

	double getBearingBetweenPoints(GPXWayPoint otherPoint) {
		
		Element fromPt = getElement();
		Element toPt = otherPoint.getElement();
		double lat1 = Double.parseDouble(fromPt.getAttribute("lat"));
		double lat2 = Double.parseDouble(toPt.getAttribute("lat"));
		double lon1 = Double.parseDouble(fromPt.getAttribute("lon"));
		double lon2 = Double.parseDouble(toPt.getAttribute("lon"));
		double lat1r = Math.toRadians(lat1);
		double lat2r = Math.toRadians(lat2);
		double deltaLon = Math.toRadians(lon2 - lon1);
		
		/* This calculation takes the latitudes and longitudes in radians of this and otherPoint 
		and the differences between longitudes in radians
		and calculates the initial bearing to follow in a straight line around a great circle to
		reach the other point using the haversine formula see
		http://www.movable-type.co.uk/scripts/latlong.html */
		
		double y = Math.sin(deltaLon) * Math.cos(lat2r);
		double x = Math.cos(lat1r)*Math.sin(lat2r) -
		        Math.sin(lat1r)*Math.cos(lat2r)*Math.cos(deltaLon);
		double brng = Math.atan2(y, x); // this gives result in + or - radians
		//brng = Math.toDegrees(brng);
		//brng = (brng + DEGREES_IN_A_CIRCLE) % DEGREES_IN_A_CIRCLE; // convert from +/- 180 degrees to 0 to 360 degrees

		return brng;
	}
	
	double[] getDestPoint(GPXWayPoint otherPoint, double distance) {
		
		final int lat = 0;
		final int lon = 1;
		Element fromPt = getElement();
		double[] result = new double[2];
		double lat1 = Double.parseDouble(fromPt.getAttribute("lat"));
		double lon1 = Double.parseDouble(fromPt.getAttribute("lon"));
		double lat1r = Math.toRadians(lat1);
		double lon1r = Math.toRadians(lon1);
		double brng = getBearingBetweenPoints(otherPoint);
		
		double lat2 = Math.asin( Math.sin(lat1r)*Math.cos(distance/RADIUS_OF_EARTH) +
                Math.cos(lat1r)*Math.sin(distance/RADIUS_OF_EARTH)*Math.cos(brng) );
		double lon2 = lon1r + Math.atan2(Math.sin(brng)*Math.sin(distance/RADIUS_OF_EARTH)*Math.cos(lat1r),
                     Math.cos(distance/RADIUS_OF_EARTH)-Math.sin(lat1r)*Math.sin(lat2));
		lon2 = Math.toDegrees(lon2);
		lon2 = (lon2+540) % 360-180;
		
		result[lat] = Math.toDegrees(lat2);
		result[lon] = lon2;
		
		return result;
		
	}

	public void addMarker(int pointInterval, UnitConverter conv) {
		
		String name = " " + conv.getName();
		if (pointInterval > 1) {
			name+= "s";
		}
		UpdatePoint(name, MARKER_SYMBOL);
	}

	public void removeMarker() {
		
		Element sym = (Element) getElement().getElementsByTagName(GPXProcessor.WP_SYM).item(0);
		Node text = sym.getFirstChild();
		if (MARKER_SYMBOL.equalsIgnoreCase(text.getNodeValue())) {
			UpdatePoint("XXX", NORMAL_SYMBOL);;
		}
	}
	
	/**
	 * Updates to parameters in this GPXWaypoint
	 * @param name this value replaces the existing name of this waypoint
	 * @param symbolName this value replaces the existing name of the symbol that visibly represents this waypoint
	 */
	public void UpdatePoint(String name, String symbolName) {
		
		if (name != null) {
			Element wpName = (Element) getElement().getElementsByTagName(GPXProcessor.WP_NAME).item(0);
			Node text = wpName.getFirstChild();
			text.setNodeValue(name);
		}
		if (symbolName != null) {
			Element sym = (Element) getElement().getElementsByTagName(GPXProcessor.WP_SYM).item(0);
			Node text = sym.getFirstChild();
			text.setNodeValue(symbolName);
		}
	}

	public void insertWaypoint(GPXWayPoint otherPoint, double distance, UnitConverter converter) {
		
		double brng = getBearingBetweenPoints(otherPoint);
		double lat = 
		
		Document doc = getElement().getOwnerDocument();
		Element NewWaypoint = (Element) getElement().cloneNode(true);
		
		
	}
	
	
	public Element getElement() {
		return Element;
	}

	public boolean isForward() {
		return forward;
	}
}
