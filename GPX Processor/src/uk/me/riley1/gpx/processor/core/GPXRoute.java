package uk.me.riley1.gpx.processor.core;

import org.w3c.dom.Element;

public class GPXRoute {
	
	private static final double MAX_OVERSHOOT = 10L;
	private Element thisElement;
	private boolean forward;
	private GPXWayPoints wayPoints;
	

	public GPXRoute(Element route, boolean forward) {
		thisElement = route;
		this.forward = forward;
		wayPoints = new GPXWayPoints(this);
	}
	
	
	public void addMarkers(UnitConverter converter) {
		
		double markerDistance = 0;
		double distance = 0L;
		GPXWayPoints wayPoints = getWayPoints();
		GPXWayPoint fromPt = wayPoints.getFirstWayPoint();
		GPXWayPoint toPt = wayPoints.getNextWayPoint();
		while (fromPt != null && toPt != null) {
			distance += fromPt.getDistanceBetweenPoints(toPt);
			if (distance >= converter.getValue()) { // This is the unit value in meters.
				
				//We have at reached or exceeded the specified distance so lets change 
				//previous point or insert one before or after it
				markerDistance += converter.convert();	// increment the distance by the value of the specified unit
				distance -= converter.getValue();
				
				if (distance > MAX_OVERSHOOT) {
					
					GPXWayPoint newPt = fromPt.createNewWaypoint(toPt, markerDistance, distance, converter);

					/*
					 * If we are doing distance covered i.e. forward then need to insert the new waypoint
					 * before the next point.
					 */
					if (forward) {
						wayPoints.insertWaypoint(newPt, toPt);
					}
					/* 
					 * If we are doing distance to go i.e backward then need to insert new waypoint
					 * before fromPt
					 */
					else {
						wayPoints.insertWaypoint(newPt, fromPt);
					}
					toPt = newPt;
				}
				
				/*
				 * Not worth inserting a new waypoint as the distance to the toPt is close enough
				 * to the interval between points. Just modify the marker and name.
				 */
				else {
					
					toPt.addMarker(markerDistance, converter);
				}
				distance = 0;	// 0 distance to start accumulating between successive points
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
