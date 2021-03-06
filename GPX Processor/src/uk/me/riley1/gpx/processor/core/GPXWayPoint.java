package uk.me.riley1.gpx.processor.core;

import java.awt.Color;
import java.util.Formatter;

import org.w3c.dom.Attr;
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
	
	/**
	 * Gets the coordinates in degrees of a point that is between this point and otherPoint at with a 
	 * distance from this point specified by the <b>distance</b> parameter.
	 * @param otherPoint the other point from which we get the bearing
	 * @param distance the distance from this point to a new point on the same bearing as to <b>otherPoint</b>
	 * from which we get the coordinates
	 * @return The coordinates of the new point.
	 */
	Coordinates getDestPoint(GPXWayPoint otherPoint, double distance) {
		
		Element fromPt = getElement();
		double lat1 = Double.parseDouble(fromPt.getAttribute("lat"));
		double lon1 = Double.parseDouble(fromPt.getAttribute("lon"));
		double lat1r = Math.toRadians(lat1);
		double lon1r = Math.toRadians(lon1);
		double brng = getBearingBetweenPoints(otherPoint);
		double angularDist = distance/RADIUS_OF_EARTH;
		
		double lat2 = Math.asin( Math.sin(lat1r)*Math.cos(angularDist) +
                Math.cos(lat1r)*Math.sin(angularDist)*Math.cos(brng) );
		double lon2 = lon1r + Math.atan2(Math.sin(brng)*Math.sin(angularDist)*Math.cos(lat1r),
                     Math.cos(angularDist)-Math.sin(lat1r)*Math.sin(lat2));
		lon2 = Math.toDegrees(lon2);
		lon2 = (lon2+540) % 360-180;
		
		Coordinates result = new Coordinates(Math.toDegrees(lat2), lon2);
		return result;
		
	}

	public void addMarker(double pointInterval, UnitConverter conv) {
		
		String name = String.valueOf(pointInterval) + " " + conv.getName();
		if (pointInterval > 1) {
			name+= "s";
		}
		UpdatePoint(name, MARKER_SYMBOL);
	}

	public void removeMarker() {
		
		Element sym = (Element) getElement().getElementsByTagName(GPXProcessor.WP_SYM).item(0);
		Node text = sym.getFirstChild();
		if (!NORMAL_SYMBOL.equalsIgnoreCase(text.getNodeValue())) {
			UpdatePoint("Mark", NORMAL_SYMBOL);
		}
	}

	/**
	 * Creates a new {@link GPXWayPoint} that is a specified distance from <b>otherpoint</b>
	 * but on the same bearing between <b>otherpoint</b> and this point
	 * @param otherPoint The  {@link GPXWayPoint} that gives us the bearing to this {@link GPXWayPoint}
	 * @param pointInterval the value of the distance in local units that specifies how far along a route
	 * or how far to go to the end of the route that the new point with represent. It is uses to from the
	 * name of the new {@link GPXWayPoint} e.g. 10 Miles or 400 Meters
	 * 
	 * @param distance the exact distance from 
	 * @param converter
	 * @return
	 */
	public GPXWayPoint createNewWaypoint(GPXWayPoint otherPoint, double pointInterval, double distance, UnitConverter converter) {
		
		Coordinates coords = otherPoint.getDestPoint(this, distance);

		Element wpElem = (Element) getElement().cloneNode(true);
		GPXWayPoint newWayPoint = new GPXWayPoint(wpElem, this.forward);
		
		Attr latAttrib = wpElem.getAttributeNode("lat");
		Attr lonAttrib = wpElem.getAttributeNode("lon");
		latAttrib.setValue(String.valueOf(coords.latitude));
		lonAttrib.setValue(String.valueOf(coords.longitude));
		
		String name = String.valueOf(pointInterval) + " " + converter.getName();
		if (pointInterval > 1) {
			name+= "s";
		}

		newWayPoint.UpdatePoint(name, MARKER_SYMBOL);
		
		return newWayPoint;
	}
	
	/**
	 * Updates to parameters in this GPXWaypoint
	 * @param name this value replaces the existing name of this waypoint. ignored if null
	 * @param symbolName this value replaces the existing name of the symbol that visibly represents this waypoint. ignored if null
	*/
	public void  UpdatePoint(String name, String symbolName) {
		
		UpdatePoint(name, symbolName, null);
	}
	
	/**
	 * Updates to parameters in this GPXWaypoint
	 * @param name this value replaces the existing name of this {@link GPXWayPoint}. ignored if null
	 * @param symbolName this value replaces the existing name of the symbol that visibly represents this 
	 * {@link GPXWayPoint}. ignored if null
	 * @param color the colour of the symbol if relevant. ignored if null
	 */
	public void UpdatePoint(String name, String symbolName, Color color) {
		
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
		if (color != null) {
			updateColor(color);
		}
	}
	
	
	public Element getElement() {
		return Element;
	}

	public boolean isForward() {
		return forward;
	}
	
	/**
	 * Updates the symbol colour of this {@link GPXWayPoint} using  
	 * @param color the specified new color of the symbol as an sRGB {@link Color}
	 */
	public void updateColor(Color color) {
		if (color != null) {
			StringBuffer sColor = new StringBuffer();
			Formatter format = new Formatter(sColor);
			format.format("%1$02X%2$02X%3$02X",color.getRed(), color.getGreen(), color.getBlue());
			format.close();
			updateColor(sColor.toString());
		}
	}
	
	private void updateColor(String sColor) {
		
		Element extensions = (Element) getElement().getElementsByTagName(GPXProcessor.WP_EXTENSIONS).item(0);
		if (extensions != null) {
			Element eColor = (Element) extensions.getElementsByTagName(GPXProcessor.WP_COLOR).item(0);
			if (eColor != null && sColor != null) {
				Node text = eColor.getFirstChild();
				text.setNodeValue(sColor);
			}
		}
	}
	
	class Coordinates {
		
		double latitude = 0;
		double longitude = 0;
		
		Coordinates(double lat, double lon) {
			latitude = lat;
			longitude = lon;
		}
	}
}
