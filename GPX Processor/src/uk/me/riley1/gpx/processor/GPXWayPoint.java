package uk.me.riley1.gpx.processor;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class GPXWayPoint {
	
	private static final double RADIUS_OF_EARTH = 6371e3;
	private static final String MARKER_SYMBOL = "Pin";
	private static final String NORMAL_SYMBOL = "TinyDot";
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
	public double getDBP(GPXWayPoint otherPoint) {
		
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
		// TODO Auto-generated method stub
		
	}
	
	
	public Element getElement() {
		return Element;
	}

	public boolean isForward() {
		return forward;
	}
}
