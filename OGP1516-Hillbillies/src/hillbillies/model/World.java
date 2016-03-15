package hillbillies.model;

import hillbillies.part2.listener.TerrainChangeListener;
import ogp.framework.util.ModelException;

public class World {
	
	public World(int[][][] terrainTypes, TerrainChangeListener modelListener) {
		
		this.terrain = terrainTypes;
		
		this.nbCubesX = terrainTypes.length;
		this.nbCubesY = terrainTypes[0].length;
		this.nbCubesZ = terrainTypes[0][0].length;
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
}
