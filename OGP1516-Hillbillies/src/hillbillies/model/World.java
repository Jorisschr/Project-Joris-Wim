package hillbillies.model;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
		
		this.updateConnections();
		
		this.nbUnits = 0;
	}
	
	private void updateConnections() {
		
	}

	public static double getLowerBound() {
		return LOWER_BOUND;
	}

	/*
	 * Return the upper bound of the x dimension of this world, for any game objects position.
	 */
	public final double getUBX() {
		return (this.getNbCubesX() - 0.5);
	}
	
	/*
	 * Return the upper bound of the y dimension of this world, for any game objects position.
	 */
	public final double getUBY() {
		return (this.getNbCubesY() - 0.5);
	}
	
	/*
	 * Return the upper bound of the z dimension of this world, for any game objects position.
	 */
	public final double getUBZ() {
		return (this.getNbCubesZ() - 0.5);
	}
	
	public int[][][] getTerrain() {
		return this.terrain;
	}
	/**
	 * Variable registering the lower bound for the x, y and z dimensions of the
	 * generated world.
	 */
	private static final double LOWER_BOUND = 0.5;

	private static final int MAX_UNITS = 100;
	private static final int MAX_FACTIONS = 5;
	
	private static final int TYPE_AIR = 0;
	private static final int TYPE_ROCK = 1;
	private static final int TYPE_TREE = 2;
	private static final int TYPE_WORKSHOP = 3;
	
	private static int[][][] terrain;
	
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
	 * Variable registering the amount living units in this world.
	 */
	private int nbUnits;

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
	
	public static int getCubeType(Vector3d position) {
		Vector3d cube = position.getCube();
		
		return World.terrain[(int) cube.getX()][(int) cube.getY()][(int) cube.getZ()];
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
	 * Checks if this cube of this postion is a solid block.
	 * @return  True if the World's cube at this postion is solid.
	 * 	    | cube.getCubeType() != Air
	 */
	public static boolean isSolid(Vector3d position) {
		return(getCubeType(position.getCube()) == 1 || (getCubeType(position.getCube()) == 2));
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
	
	/**
	 * Check whether the cube at the given coordinates is passable.
	 * 
	 * @param 	x
	 * 			The x coordinate of the cube to check.
	 * @param 	y
	 * 			The y coordinate of the cube to check.
	 * @param 	z
	 * 			The z coordinate of the cube to check.
	 * @return 	True if the type of the cube to check is air or workshop, false otherwise.
	 */
	public boolean isPassable(int x, int y, int z) {
		if ((this.getCubeType(x, y, z) == TYPE_AIR) || (this.getCubeType(x, y, z) == TYPE_WORKSHOP)) {
			return true;
		}
		return false;
	}
	
	/*
	 * Return the amount of living units in this world.
	 */
	public int getNbUnits() {
		return this.nbUnits;
	}
	
	/*
	 * Set the number of living units in this world to nb.
	 * 
	 * @param	nb
	 * 			The new number of living units.
	 */
	public void setNbUnits(int nb) {
		this.nbUnits = nb;
	}
	
	public Unit spawnUnit(boolean enableDefaultBehavior) {
		
		if (this.getNbUnits() < MAX_UNITS) {
			this.setNbUnits(this.getNbUnits() + 1);
			String name = "Joris Test";
			Vector3d pos = this.getRndValidPos();
			int weight = ThreadLocalRandom.current().nextInt(25, 100 + 1);
			int agility = ThreadLocalRandom.current().nextInt(25, 100 + 1);
			int strength = ThreadLocalRandom.current().nextInt(25, 100 + 1);
			int toughness = ThreadLocalRandom.current().nextInt(25, 100 + 1);
						
			try {
				return new Unit(name, pos, weight, agility, strength, toughness, enableDefaultBehavior);
			} catch (Throwable exc) {
				
			}
		}
		// hoe niks doen als er al 100 unit zijn?
		// exception maken in Unit denk ik.
		return null;

	}
	
	public boolean posOutOfBounds(int x, int y, int z) {
		if ((x > this.getUBX()) || (y > this.getUBY()) || (z > this.getUBZ())) {
			return true;
		}
		if ((x < 0) || (y < 0) || (z < 0)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check whether the center of the given cube is a valid position for a game object.
	 * 
	 * @param 	x
	 * 			The x coordinate of the cube to check.
	 * @param 	y
	 * 			The y coordinate of the cube to check.
	 * @param 	z
	 * 			The z coordinate of the cube to check.
	 * @return 	True if the type of the cube to check is air or workshop, 
	 * 			and the cube beneath the given cube is solid and connected to the world border
	 * 			(or has z coordinate  = 0).
	 */
	public boolean isValidPosition(int x, int y, int z) {	
		if (!posOutOfBounds(x, y, z)) {
			if (isPassable(x, y, z)) {
				if ((isSolidConnectedToBorder(x, y, z - 1)) || (z == 0)) {
					return true;
				}
			}					
		}		
		return false;
	}
	
	public boolean isValidPosition(Vector3d vector) {
		int[] pos = vector.getIntArray();
		return isValidPosition(pos[0], pos[1], pos[2]);
	}
	
	private Vector3d getRndValidPos() {
		Vector3d rndPos = new Vector3d(-1, -1, -1);
		
		while (!isValidPosition(rndPos)) {
			rndPos = rndPos.genRndVector3d((int) this.getUBX(),(int) this.getUBY(),(int) this.getUBZ());
		}	    
		return rndPos;
	}
}
