package hillbillies.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
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
	 * @param unitSet 
	 * @return
	 * @throws ModelException
	 */
	public World(int[][][] terrainTypes, TerrainChangeListener modelListener) {
		this.terrain = terrainTypes;
		
		this.nbX = terrainTypes.length;
		this.nbY = terrainTypes[0].length;
		this.nbZ = terrainTypes[0][0].length;
		
		this.connections = new ConnectedToBorder(this.getNbX(),
												this.getNbY(),
												this.getNbZ());
		
		this.updateConnections();
		
		this.nbUnits = 0;
		this.unitSet = Collections.emptySet();
		this.factionSet = Collections.emptySet();
		this.boulderSet = Collections.emptySet();
		this.logSet = Collections.emptySet();
		this.selectedBoulder = null;
		this.selectedLog = null;
	}
	
	private void updateConnections() {
		for (int k = 0; k < nbZ; k++) {
			for (int j = 0; j < nbY; j++) {
				for (int i =0; i < nbX; i++) {
					if ((getCubeType(i, j, k) == TYPE_AIR) || (getCubeType(i, j ,k) == TYPE_WORKSHOP)) {
						connections.changeSolidToPassable(i, j, k);
					}
				}
			}
			
		}
		
	}

	public static double getLowerBound() {
		return LOWER_BOUND;
	}

	/*
	 * Return the upper bound of the x dimension of this world, for any game objects position.
	 */
	public final double getUBX() {
		return (this.getNbX() - 0.5);
	}
	
	/*
	 * Return the upper bound of the y dimension of this world, for any game objects position.
	 */
	public final double getUBY() {
		return (this.getNbY() - 0.5);
	}
	
	/*
	 * Return the upper bound of the z dimension of this world, for any game objects position.
	 */
	public final double getUBZ() {
		return (this.getNbZ() - 0.5);
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
	
	public static final int TYPE_AIR = 0;
	public static final int TYPE_ROCK = 1;
	public static final int TYPE_TREE = 2;
	public static final int TYPE_WORKSHOP = 3;
	
	private int[][][] terrain;
	
	/*
	 * Variable registering the number of cubes in the world in the x-direction.
	 */
	private final int nbX;
	
	/*
	 * Variable registering the number of cubes in the world in the y-direction.
	 */
	private final int nbY;
	
	/*
	 * Variable registering the number of cubes in the world in the z-direction.
	 */
	private final int nbZ;
	
	/*
	 * Variable registering
	 */
	private ConnectedToBorder connections;
	
	/*
	 * Variable registering the amount living units in this world.
	 */
	private int nbUnits;	
	private Set<Unit> unitSet;	
	private Set<Faction> factionSet;
	private Set<Boulder> boulderSet;
	private Set<Log> logSet;
	private Boulder selectedBoulder;
	private Log selectedLog;

	/*
	 * Return the number of cubes in the world in the x-direction.
	 */
	public int getNbX() {
		return this.nbX;
	}
	
	/*
	 * Return the number of cubes in the world in the y-direction.
	 */
	public int getNbY() {
		return this.nbY;
	}
	
	/*
	 * Return the number of cubes in the world in the z-direction.
	 */
	public int getNbZ() {
		return this.nbZ;
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
	
	public int getCubeType(Vector3d position) {
		Vector3d cube = position.getCube();
		
		return this.terrain[(int) cube.getX()][(int) cube.getY()][(int) cube.getZ()];
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
		if ((value >= TYPE_AIR) && (value < TYPE_WORKSHOP)) {
			this.terrain[x][y][z] = value;
		}
	}
	
	public void setCubeType(Vector3d cubePos, int value) {
		if ((value >= TYPE_AIR) && (value < TYPE_WORKSHOP)) {
			int[] coord = cubePos.getIntArray();
			this.terrain[coord[0]][coord[1]][coord[2]] = value;
		}
	}
	
	/**
	 * Checks if the cube at this position is a solid block.
	 * @return  True if the World's cube at this position is solid.
	 * 	    	| ((getCubeType(position.getCube) == 1) || (getCubeType(position.getCube()) == 2))
	 */
	public boolean isSolid(Vector3d position) {
		return ((getCubeType(position.getCube()) == TYPE_ROCK) || 
				(getCubeType(position.getCube()) == TYPE_TREE));
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
		
		this.setNbUnits(this.getNbUnits() + 1);
		String name = "Hillbilly";
		Vector3d pos = this.getRndValidPos();
		int weight = ThreadLocalRandom.current().nextInt(25, 100 + 1);
		int agility = ThreadLocalRandom.current().nextInt(25, 100 + 1);
		int strength = ThreadLocalRandom.current().nextInt(25, 100 + 1);
		int toughness = ThreadLocalRandom.current().nextInt(25, 100 + 1);
						
		try {
			return new Unit(name, pos, weight, agility, strength, toughness, enableDefaultBehavior);
		} catch (Throwable exc) {							
		}
		// hoe niks doen als er al 100 unit zijn?
		// exception maken in Unit denk ik.
		return null;

	}
	
	public void addUnit(Unit unit) {
		if (this.getNbUnits() < MAX_UNITS) {
			this.unitSet.add(unit);
			unit.setWorld(this);
			if (this.getActiveFactions().size() < MAX_FACTIONS) {
				Faction faction = new Faction();
				this.addFaction(faction);
				faction.addUnit(unit);				
			}
			else if (!this.getSmallestFaction().isFactionFull()) {
				this.getSmallestFaction().addUnit(unit);
			}
		}
	}
	
	public Set<Unit> getUnits() {
		return this.unitSet;
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
	
	public void advanceTime(double dt) {
		for (Unit unit: this.getUnits()) {
			try {
				unit.advanceTime(dt);
			} catch (Throwable e) {
				System.out.println("Exception while advancing time for Unit: " + unit.getName() + 
								   ",at: " + Arrays.toString(unit.getPosition().getDoubleArray()));
				e.printStackTrace();
			}
		}
		 
		for (Boulder boulder: this.getBoulders()){
			try{
				boulder.advanceTime(dt);
			} catch (Throwable e) {
				System.out.println("Exception while advancing time for Boulder at: " + boulder.getPosition());
				e.printStackTrace();
			}
		}
		
		for (Log log: this.getLogs()){
			try{
				log.advanceTime(dt);
			} catch (Throwable e) {
				System.out.println("Exception while advancing time for Log at: " + log.getPosition());
				e.printStackTrace();
			}
		}
	}
	
	public void addFaction(Faction faction) {
		this.factionSet.add(faction);
	}
	
	public Set<Faction> getActiveFactions() {
		return this.factionSet;
	}

	
	/**
	 * Return the smallest active faction of this world. 
	 * (IE. the faction with the least number of members)
	 */
	public Faction getSmallestFaction() {
		Faction smallestFaction = new Faction();
		int nbMembers = Faction.MAX_MEMBERS + 1;
		for (Faction faction: this.getActiveFactions()) {
			if (faction.getNbUnits() < nbMembers) {
				nbMembers = faction.getNbUnits();
				smallestFaction = faction;
			}
		}
		return smallestFaction;
	}
	
	public void addBoulder(Boulder boulder) {
		this.boulderSet.add(boulder);
	}
	
	public Set<Boulder> getBoulders() {
		return this.boulderSet;
	}
	
	/**
	 * Set the selected boulder of this world to the given boulder.
	 */
	public void setSelectedBoulder(Boulder boulder) {
		this.selectedBoulder = boulder;
	}
	
	/**
	 * Return the selected boulder of this world.
	 */
	public Boulder getSelectedBoulder() {
		return this.selectedBoulder;
	}
	
	/**
	 * Return whether a boulder is available at the given position in the given world.
	 * @param 	pos
	 * 			The position to check.
	 * @return	True if and only if a boulder has the same position as the given position.
	 */
	public boolean isBoulderAvailable(Vector3d pos) {
		for (Boulder boulder: this.getBoulders()) {
			if(boulder.getPosition().equals(pos)) {
				this.setSelectedBoulder(boulder);
				return true;
			}
		}
		return false;
	}
	
	public void addLog(Log log) {
		this.logSet.add(log);
	}
	
	public Set<Log> getLogs() {
		return this.logSet;
	}
	
	/**
	 * Set the selected log of this world to the given log.
	 */
	public void setSelectedLog(Log log) {
		this.selectedLog = log;
	}
	
	/**
	 * Return the selected log of this world.
	 */
	public Log getSelectedLog() {
		return this.selectedLog;
	}
	
	/**
	 * Return whether a log is available at the given position in the given world.
	 * @param 	pos
	 * 			The position to check.
	 * @return	True if and only if a log has the same position as the given position.
	 */
	public boolean isLogAvailable(Vector3d pos) {
		for (Log log: this.getLogs()) {
			if (log.getPosition().equals(pos)) {
				this.setSelectedLog(log);
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Reset the selected boulder and log to null.
	 */
	public void resetSelection() {
		this.setSelectedBoulder(null);
		this.setSelectedLog(null);
	}
	
	/**
	 * Change the type of the given cube to air and add a log or boulder with a probability of 25%
	 * if the cube at the given position is respectively a tree of rock.
	 * 
	 * @param	cubePos
	 * 			The position of the cube to be changed.
	 */
	public void destroyCube(Vector3d cubePos) {
		int type = this.getCubeType(cubePos);
		this.setCubeType(cubePos, TYPE_AIR);
		
		double prob = 0.25;
		boolean spawn = (new Random().nextDouble() <= prob);
		
		if (spawn) {
			Vector3d pos = cubePos.add(0.5);
			
			if (type == TYPE_ROCK) {
				try {
					this.addBoulder(new Boulder(pos));
				} catch (OutOfBoundsException e) {
					e.printStackTrace();
				}
			}
			else if (type == TYPE_TREE) {
				try {
					this.addLog(new Log(pos));
				} catch (OutOfBoundsException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//TODO 
	public void deleteObject(GameObject object) {
		if (object.getType() == Log.TYPE) {
			
		}
	}
}
