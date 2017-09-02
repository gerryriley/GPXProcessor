package uk.me.riley1.gpx.processor;

import org.w3c.dom.Element;

public class GPXRoute {
	
	private static final double MAX_OVERSHOOT = 100L;
	private Element thisElement;
	private boolean forward;
	private GPXWayPoints wayPoints;
	

	public GPXRoute(Element route, boolean forward) {
		thisElement = route;
		this.forward = forward;
		wayPoints = new GPXWayPoints(this);
	}
	
	
	public void addMarkers(UnitConverter converter) {
		
		int markerDistance = 0;
		double distance = 0L;
		GPXWayPoints wayPoints = getWayPoints();
		GPXWayPoint fromPt = wayPoints.getFirstWayPoint();
		GPXWayPoint toPt = wayPoints.getNextWayPoint();
		while (fromPt != null && toPt != null) {
			distance += fromPt.getDistanceBetweenPoints(toPt);
			if (distance > converter.getValue()) { // This is the unit value in meters.
				
				//We have at reached or exceeded the specified distance so lets change previous point
				markerDistance += converter.convert();	// increment the distance by the value of the specified unit
				distance -= converter.getValue();
				
				if (distance > MAX_OVERSHOOT) {
					
					fromPt.insertWaypoint(toPt, distance, converter);
				}
				
				else {
					
					fromPt.addMarker(markerDistance, converter);
				}
			}
			fromPt = toPt;
			toPt = wayPoints.getNextWayPoint();
		}
	}

	public void removeMarkers() {
		
		GPXWayPoints wayPoints = getWayPoints();
		GPXWayPoint fromPt = wayPoints.getFirstWayPoint();
		while (fromPt != null) {
			fromPt.removeMarker();
			fromPt = wayPoints.getNextWayPoint();
		}

	}
	
	public Element getElement() {
		return thisElement;
	}

	public boolean isForward() {
		return forward;
	}

	public GPXWayPoints getWayPoints() {
		return wayPoints;
	}


}
