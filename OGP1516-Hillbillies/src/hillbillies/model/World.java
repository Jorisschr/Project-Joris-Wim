package hillbillies.model;

import helperclasses.*;
import hillbillies.part2.listener.TerrainChangeListener;
import hillbillies.util.ConnectedToBorder;
import ogp.framework.util.ModelException;

public class World {
	
	/**
	 * Create a new world of the given size and with the given terrain. To keep
	 * the GUI display up to date, the method in the given listener must be
	 * called whenever the terrain type of a cube in the world changes.
	 * 
	 * @param terrainTypes
	 *            A three-dimensional array (structured as [x][y][z]) with the
	 *            types of the terrain, encoded as integers. The terrain always
	 *            has the shape of a box (i.e., the array terrainTypes[0] has
	 *            the same length as terrainTypes[1] etc.). The integer types
	 *            are as follows:
	 *            <ul>
	 *            <li>0: air</li>
	 *            <li>1: rock</li>
	 *            <li>2: tree</li>
	 *            <li>3: workshop</li>
	 *            </ul>
	 * @param modelListener
	 *            An object with a single method,
	 *            {@link TerrainChangeListener#notifyTerrainChanged(int, int, int)}
	 *            . This method must be called by your implementation whenever
	 *            the terrain type of a cube changes (e.g., as a consequence of
	 *            cave-ins), so that the GUI will correctly update the display.
	 *            The coordinate of the changed cube must be given in the form
	 *            of the parameters x, y and z. You do not need to call this
	 *            method during the construction of your world.
	 * @return
	 * @throws ModelException
	 */
	public World(int[][][] terrainTypes, TerrainChangeListener modelListener) {
		
		this.terrain = terrainTypes;
		
		this.nbCubesX = terrainTypes.length;
		this.nbCubesY = terrainTypes[0].length;
		this.nbCubesZ = terrainTypes[0][0].length;
		
		this.connections = new ConnectedToBorder(this.getNbCubesX(),
												this.getNbCubesY(),
												this.getNbCubesZ());
	}
	
	public static double getLowerBound() {
		return LOWER_BOUND;
	}

	public static double getUpperBound() {
		return UPPER_BOUND;
	}

	/**
	 * Variable registering the lower bound for the x, y and z dimensions of the
	 * generated world.
	 */
	private static final double LOWER_BOUND = 0.5;

	/**
	 * Variable registering the upper bound for the x, y and z dimensions of the
	 * generated world.
	 */
	private static final double UPPER_BOUND = 49.5;
	
	private int[][][] terrain;
	
	/*
	 * Variable registering the number of cubes in the world in the x-direction.
	 */
	private final int nbCubesX;
	
	/*
	 * Variable registering the number of cubes in the world in the y-direction.
	 */
	private final int nbCubesY;
	
	/*
	 * Variable registering the number of cubes in the world in the z-direction.
	 */
	private final int nbCubesZ;
	
	/*
	 * Variable registering
	 */
	private ConnectedToBorder connections;

	/*
	 * Return the number of cubes in the world in the x-direction.
	 */
	public int getNbCubesX() {
		return this.nbCubesX;
	}
	
	/*
	 * Return the number of cubes in the world in the y-direction.
	 */
	public int getNbCubesY() {
		return this.nbCubesY;
	}
	
	/*
	 * Return the number of cubes in the world in the z-direction.
	 */
	public int getNbCubesZ() {
		return this.nbCubesZ;
	}
	
	/**
	 * Return the terrain type of the cube at the given coordinates.
	 * 
	 * @param x
	 *            The x-coordinate of the cube
	 * @param y
	 *            The y-coordinate of the cube
	 * @param z
	 *            The z-coordinate of the cube
	 * @return The terrain type of the given cube, encoded as an integer
	 *         according to the values in
	 *         {@link #createWorld(int[][][], TerrainChangeListener)}.
	 */
	public int getCubeType(int x, int y, int z) {
		return this.terrain[x][y][z];
	}
	
	/**
	 * Set the terrain type of the cube at the given coordinates the given
	 * value.
	 * 
	 * @param world
	 *            The world in which to set the type.
	 * @param x
	 *            The x-coordinate of the cube
	 * @param y
	 *            The y-coordinate of the cube
	 * @param z
	 *            The z-coordinate of the cube
	 * @param value
	 *            The new value of the terrain type of the cube, encoded as an
	 *            integer according to the values in
	 *            {@link #createWorld(int[][][], TerrainChangeListener)}.
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
	public void setCubeType(int x, int y, int z, int value) {
		if ((value >= 0) && (value < 4)) {
			this.terrain[x][y][z] = value;
		}
	}
	
	/**
	 * Returns whether the cube at the given position is a solid cube that is
	 * connected to a border of the world through other directly adjacent solid
	 * cubes.
	 * 
	 * @note The result is pre-computed, so this query returns immediately.
	 * 
	 * @param x
	 *            The x-coordinate of the cube to test
	 * @param y
	 *            The y-coordinate of the cube to test
	 * @param z
	 *            The z-coordinate of the cube to test
	 * @return true if the cube is connected; false otherwise
	 */
	public boolean isSolidConnectedToBorder(int x, int y, int z) {
		return connections.isSolidConnectedToBorder(x, y, z);
	}
}
