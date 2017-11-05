package uk.me.riley1.gpx.processor.core;

import java.awt.Color;

import org.w3c.dom.Element;

import uk.me.riley1.gpx.processor.ui.ConfigurationWindow.Configuration;

public class GPXRoute {
	
	private static final double MAX_OVERSHOOT = 10L;
	private Element thisElement;
	private Configuration configuration;
	private GPXWayPoints wayPoints;
	

	public GPXRoute(Element route, Configuration config) {
		thisElement = route;
		wayPoints = new GPXWayPoints(this);
		this.configuration = config;
	}
	
	
	public void addMarkers() {
		
		double markerDistance = 0;
		double distance = 0L;
		GPXWayPoints wayPoints = getWayPoints();
		GPXWayPoint fromPt = wayPoints.getFirstWayPoint(isForward());
		GPXWayPoint toPt = wayPoints.getNextWayPoint();
		UnitConverter converter = (UnitConverter) configuration.get(Configuration.DISTANCE_UNIT);
		boolean doStartFinishMarkers = (Boolean) configuration.get(Configuration.START_FINISH);
		
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
					if (isForward()) {
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
		
		if (doStartFinishMarkers) {
			
			fromPt = wayPoints.getFirstWayPoint(true);
			toPt = wayPoints.getLastWayPoint(true);
			
			if (fromPt.getDistanceBetweenPoints(toPt) > 500) {
				fromPt.UpdatePoint("Start", "Pin", Color.GREEN);
				toPt.UpdatePoint("Finish", "Pin", Color.RED);
			}
			else {
				fromPt.UpdatePoint("Start/Finish", "Pin", Color.GREEN);
			}
		}
	}

	public void removeMarkers() {
		
		GPXWayPoints wayPoints = getWayPoints();
		GPXWayPoint fromPt = wayPoints.getFirstWayPoint(isForward());
		while (fromPt != null) {
			fromPt.removeMarker();
			fromPt = wayPoints.getNextWayPoint();
		}

	}
	
	public Element getElement() {
		return thisElement;
	}

	public boolean isForward() {
		
		String s = configuration != null ? (String) configuration.get(Configuration.DIRECTION) : "backward";
		boolean direction = "forwards".equalsIgnoreCase(s);
		return direction;
	}

	public GPXWayPoints getWayPoints() {
		return wayPoints;
	}


}
