package com.thronewars.world;

import com.badlogic.gdx.utils.Array;

/**
 * Represents a formation of waypoints in the game world.
 */
public class WaypointFormation {

    private final Array<Waypoint> waypoints = new Array<>();

    private final int columns;
    private float startX;
    private final float horizontalSpacing;
    
	public WaypointFormation(int rows, int columns, float startX, float startY, float horizontalSpacing, float verticalSpacing) {
		this.startX = startX;
	    this.columns = columns;
	    this.horizontalSpacing = horizontalSpacing;

		createWaypoints(rows, columns, startX, startY, horizontalSpacing, verticalSpacing);
	}

	/**
	 * Creates waypoints in a grid formation based on the specified parameters.
	 *
	 * @param rows The number of rows in the formation.
	 * @param columns The number of columns in the formation.
	 * @param startX The starting X position for the formation.
	 * @param startY The starting Y position for the formation.
	 * @param horizontalSpacing The horizontal spacing between waypoints.
	 * @param verticalSpacing The vertical spacing between waypoints.
	 */
	private void createWaypoints(int rows, int columns, float startX, float startY, float horizontalSpacing, float verticalSpacing) {
		for (int column = columns - 1; column >= 0; column--) {
	        for (int row = 0; row < rows; row++) {
	            float x = startX + (column * horizontalSpacing);
	            float y = startY + (row * verticalSpacing);

	            waypoints.add(new Waypoint(x, y, column));
	        }
	    }
    }

	/**
	 * Retrieves the first available waypoint in the formation.
	 * @return The first available waypoint, or null if none are available.
	 */
    public Waypoint getAvailableWaypoint() {
        for (Waypoint waypoint : waypoints) {
            if (waypoint.isAvailable()) {
                return waypoint;
            }
        }

        return null;
    }
	
	public int getColumns() {
	    return columns;
	}
	
	/**
	 * Positions the front column at the specified X coordinate.
	 * @param frontX The X coordinate for the front column.
	 */
	public void setFrontColumnX(float frontX) {
		float newStartX = frontX - ((columns - 1) * horizontalSpacing);
		setX(newStartX);
	}
    
	/**
	 * Moves the formation horizontally while preserving its layout.
	 *
	 * @param newX The new starting X position.
	 */
	public void setX(float newX) {
		float difference = newX - startX;

		if (difference == 0f) {
			return;
		}

		startX = newX;

		for (Waypoint waypoint : waypoints) {
			waypoint.setX(waypoint.getX() + difference);
		}
	}
    
    public Array<Waypoint> getWaypoints() {
        return waypoints;
    }
}