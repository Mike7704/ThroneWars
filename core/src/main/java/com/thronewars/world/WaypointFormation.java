package com.thronewars.world;

import com.badlogic.gdx.utils.Array;

/**
 * Represents a formation of waypoints in the game world.
 */
public class WaypointFormation {

    private final Array<Waypoint> waypoints = new Array<>();

	public WaypointFormation(int rows, int columns, float startX, float startY, float horizontalSpacing, float verticalSpacing) {
        createWaypoints(
            rows,
            columns,
            startX,
            startY,
            horizontalSpacing,
            verticalSpacing
        );
    }

	private void createWaypoints(int rows, int columns, float startX, float startY, float horizontalSpacing, float verticalSpacing) {
		for (int column = columns; column >= 0; column--) {
			for (int row = 0; row < rows; row++) {
                float x = startX + (column * horizontalSpacing);
                float y = startY + (row * verticalSpacing);

                waypoints.add(new Waypoint(x, y));
            }
        }
    }

    public Waypoint getAvailableWaypoint() {
        for (Waypoint waypoint : waypoints) {
            if (waypoint.isAvailable()) {
                return waypoint;
            }
        }

        return null;
    }

    public Array<Waypoint> getWaypoints() {
        return waypoints;
    }
}