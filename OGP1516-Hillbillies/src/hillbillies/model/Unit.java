package hillbillies.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import helperclasses.*;

/**
 * A class of Hillbilly units.
 * 
 * @author Joris Schrauwen, Wim Schmitz
 * 
 * 
 * @invar Each unit can have its position as position. |
 *        isValidPosition(this.getPosition())
 * @invar Each unit can have its name as name. | canHaveAsName(this.getName())
 *
 */
public class Unit {

	/**
	 * Initialize this new unit with given position, name, weight, strength,
	 * agility and toughness.
	 * 
	 * @param 	position
	 *        	The default position for this new unit.
	 * @param 	name
	 *          The name for this new unit.
	 * @param 	weight
	 *         	The weight for this new unit.
	 * @param 	strength
	 *          The strength for this new unit.
	 * @param 	agility
	 *          The agility for this new unit.
	 * @param 	toughness
	 *        	The toughness for this new unit.
	 * @param 	orientation.
	 *          The orientation for this new unit.
	 * @param 	enableDefaultBehaviour
	 * 			The starting state of this unit:
	 * 			if true, the unit will start performing a random activity immediatly after spawn
	 * 
	 * @post   The position of this new unit is the given position. 
	 * 		    |new.getPosition() == position
	 * @post   The name of this new unit is equal to the given name. 
	 * 		    |new.getName() == name
	 * @post   The attributes of this unit well be set to the specified values
	 * 		   	|new.getStrength() == strength
	 * 		   	|new.getAgility() == agility
	 * 		   	|new.getWeight() == weight
	 * 		   	|new.getToughness() == toughness
	 * @post   The new unit will perform its default behaviour if enableDefaultBehaviour is true.
	 * 		
	 * @throws OutOfBoundsException
	 *         The given position is out of bounds. 
	 *         	|!isValidPosition(position)
	 * @throws IllegalArgumentException
	 *         This new unit cannot have the given name as its name. 
	 *         	|!canHaveAsName(this.getName())
	 */
	public Unit(String name, Vector3d position, int weight, int agility, int strength, int toughness,
			boolean enableDefaultBehavior) throws OutOfBoundsException, IllegalArgumentException {
		

		Vector3d pos = position.getCube().add(0.5);
		if (!pos.isValidPosition()) {
			// display message?
			throw new OutOfBoundsException(pos.getDoubleArray());
		}

		if (!canHaveAsName(name)) {
			// REGEX!!
			// display message?
			throw new IllegalArgumentException(name);
		}

		this.position = pos;
		this.name = name;

		if (isWithinRange(strength)) {
			this.setStrength(strength);
		} else {
			this.setStrength(25);
		}

		if (isWithinRange(agility)) {
			this.setAgility(agility);
		} else {
			this.setAgility(25);
		}

		if (isWithinRange(toughness)) {
			this.setToughness(toughness);
		} else {
			this.setToughness(25);
		}

		if (isWithinRange(weight)) {
			this.setWeight(weight);
		} else {
			this.setWeight((int) ((this.getAgility() + this.getStrength()) / 2));
		}

		this.orientation = (float) Math.PI / 2;
		this.stamina = getMaxHitpoints();
		this.hitpoints = getMaxHitpoints();
		this.movement = "Walking";
		this.enableDefaultBehavior = enableDefaultBehavior;
		this.status = "Idle";
		this.speed = 0;
		this.velocity = new Vector3d();
		this.activityProgress = 0;
		this.timeNeeded = 0;
		this.destination = new Vector3d(-1,-1,-1);
		this.nextPosition = this.getPosition();
		this.movingTime = 0;
		this.sprintingTime = 0;
		this.waitingTo = null;
		this.opponent = null;
		this.experience = 0;
		this.faction = new Faction();
		this.carries = null;
		this.world = null;
		this.target = new Vector3d();
		this.queue = new ArrayList<Vector3d>();
	}

	/**
	 * Variable registering the position of this unit.
	 */
	private Vector3d position;

	/**
	 * Variable registering the name of this unit.
	 */
	private String name;
	
	/**
	 * Variable registering the weight of this unit.
	 */
	private int weight;

	/**
	 * Variable registering the strength of this unit.
	 */
	private int strength;

	/**
	 * Variable registering the agility of this unit.
	 */
	private int agility;

	/**
	 * Variable registering the toughness of this unit.
	 */
	private int toughness;

	/**
	 * Variable registering the orientation of this unit.
	 */
	private float orientation;

	/**
	 * Variable registering the stamina of this unit.
	 */
	private int stamina;

	/**
	 * Variable registering the hitpoints of this unit.
	 */
	private int hitpoints;

	/**
	 * Variable registering the status of this unit.
	 */
	private String status;

	/**
	 * Variable registering the unit's movement status.
	 */
	private String movement;

	/**
	 * Variable registering the passed time since the unit started its current activity
	 */
	private double counter;

	/**
	 * Variable registering whether default behavior is enabled for this unit.
	 */
	private boolean enableDefaultBehavior;
	
	/**
	 * Variable registering the units current absolute speed.
	 */
	private double speed;
	
	/**
	 * Three-dimensional vector registering the units current vectorial speed.
	 */
	private Vector3d velocity;

	/**
	 * Variable registering the units current activity progress.
	 */
	private double activityProgress;
	
	/**
	 * Variable registering the time needed for this unit to end its current activity
	 */
	private double timeNeeded;

	/**
	 * Three-dimensional vector registering the units current destination.
	 */
	private Vector3d destination;
	
	/**
	 * Three-dimensional vector registering the units positon to move to after it has moved to .
	 */
	private Vector3d nextPosition;

	/**
	 * Variable registering the time that this unit has been moving.
	 */
	private double movingTime;
	
	/**
	 * Variable registering the time that this unit has been sprinting.
	 */
	private double sprintingTime;
	
	/**
	 * String registering the activity that this unit is waiting to do after his current activity.
	 */
	private String waitingTo;
	
	/**
	 * Unit registering this units current opponent in a fight.
	 */
	private Unit opponent;
	
	/**
	 * Variable registering this units current progress to his next level.
	 */
	private int experience;
	
	/**
	 * Boolean registering weither this unit is alive or not.
	 */
	private boolean alive;
	
	private GameObject carries;
	private World world;
	private Vector3d target;
	private ArrayList<Vector3d> queue;
	
	/**
	 * Variable registering this units faction.
	 */
	private Faction faction;
	
	///////////////////////////
	///	GENERAL POSITIONING ///
	///////////////////////////
	/**
	 * Return the position of this unit.
	 */
	@Basic
	public Vector3d getPosition() {
		return this.position;
	}
	
	/**
	 * Set this units position to the specified position
	 * @param  newPos
	 * 			The position to place this unit on.
	 * @throws OutOfBoundsException
	 * 			|!isValidPosition(newPos)
	 * @post   This units position is the specified position
	 * 			|this.position == newPos
	 */
	public void setPosition(Vector3d newPos) throws OutOfBoundsException {
		if (!this.getWorld().isValidPosition(newPos)) {
			throw new OutOfBoundsException(newPos.getDoubleArray());
		}
		this.position = newPos;
	}
	
	public int[] getOccupyingCube(){
		return this.getPosition().getIntArray();
	}
	
	/**
	 * Return the position of the cube occupied by this position.
	 */
	public int[] getCube(Vector3d position) {
		return position.getIntArray();
	}

	/**
	 * Check if two units are in the same or a neighboring cube.
	 * 
	 * @param  otherUnit
	 * 		   the other Unit to check the location of  
	 * @return True if the distance between two Units is less than or equal to one block
	 * 		   | result == (distance < sqrt(2))
	 */
	public boolean isAdjacentTo(Unit other){
		
		Vector3d thisCube = this.getPosition().getCubeCenter();
		Vector3d otherCube = other.getPosition().getCubeCenter();

		return (thisCube.subtract(otherCube).calcNorm() <= Math.sqrt(2));

	}
	
	/**
	 * Check if the cube occupied by this unit is adjacent to the cube with the given coordinates.
	 * 
	 * @param 	coords
	 * 			The coordinates of the adjacent (?) cube.
	 * @return	True if and only if the distance between the units occupying cube 
	 * 			and the cube with the given coords is smaller than or equal to sqrt(3).
	 */
	public boolean isAdjacentTo(Vector3d coords) {
		return (new Vector3d(this.getOccupyingCube()).subtract(coords).calcNorm() <= Math.sqrt(3));
	}
	
	/**
	 * Constant reflecting the length of any side of a cube of the game world.
	 * 
	 * @return The length of all sides of all cubes of the game world is 1. |
	 *         result == 1
	 */
	public static final int CUBE_LENGTH = 1;

	
	
	
	/////////////////
	///	UNIT NAME ///
	/////////////////
	/**
	 * Return the name of this unit.
	 * 
	 * @return this units name
	 */
	@Basic
	@Raw
	@Immutable
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set this units name to the specified name.
	 * @param	newName
	 * 				the String to set this units name to.
	 * @throws	NameException
	 * 				Throws a NameException if the specified name is not valid
	 * @post	This units name will be set to newName
	 * 				|this.getName() == newName
	 */
	public void setName(String newName) throws NameException{
		if (!this.canHaveAsName(newName)) {
			throw new NameException(newName);
		}
		this.name = newName;
	}

	/**
	 * Check whether this unit can have the given name as its name.
	 * 
	 * @param name
	 *            The name to check.
	 * @return True if and only if the name uses 2 characters or more, the first
	 *         letter is a capital one and all characters are either letters,
	 *         spaces or quotes. | result == (name != null)
	 */
	@Raw
	public boolean canHaveAsName(String name) {

		if (name == null) {
			return false;
		}
		char[] chars = name.toCharArray();

		if ((chars.length < 2) || (!Character.isUpperCase(chars[0]))) {
			return false;
		}
		for (char i : chars) {
			if ((!Character.isLetter(i)) && (i != ' ') && (i != '\'') && (i != '"')) {
				return false;
			}
		}
		return true;
	}
	
	
	///////////////////////
	///	UNIT ATTRIBUTES ///
	///////////////////////
	
	public boolean isWithinRange(int value) {
		return ((value >= 25) && (value <= 100));
	}

	/**
	 * Constant reflecting the lowest possible value for an attribute of a unit.
	 * 
	 * @return The lowest possible value for all attributes of all units is 1. |
	 *         result == 1
	 */
	public static final int MIN_ATTRIBUTE = 1;

	/**
	 * Constant reflecting the highest possible value for an attribute of a
	 * unit.
	 * 
	 * @return The highest possible value for all attributes of all units is
	 *         200. | result == 200
	 */
	public static final int MAX_ATTRIBUTE = 200;

	/**
	 * Constant reflecting the duration after which a Unit will stop its current
	 * activity, and start resting.
	 * 
	 * @return The interval to do an activity. | result == 180
	 */
	public static final double REST_INTERVAL = 180;

	/**
	 * Return the weight of this unit.
	 */
	@Basic
	public int getWeight() {
		return this.weight;
	}

	/**
	 * Set the weight of this unit to the given weight.
	 * 
	 * @param weight
	 *            The new weight for this unit.
	 * @post If the given weight is in range of the weight for a unit and the
	 *       given weight is at least the sum of the unit's strength and agility
	 *       divided by 2, then the new weight of this unit is equal to the
	 *       given weight. 
	 *       | if ((weight >= MIN_ATTRIBUTE) && (weight <= MAX_ATTRIBUTE) && 
	 *       | 					(weight >= (this.getAgility() + this.getStrength()) / 2)) 
	 *       | 	then new.getWeight == weight
	 * @post If the unit is carrying a game object it is allowed 
	 *       to surpass the given MAX_ATTRIBUTE.
	 *       | if ((this.isCarryingBoulder()) || (this.isCarryingLog())) 
	 *       |	then new.getWeight() = weight
	 */
	public void setWeight(int weight) {
		if ((weight >= MIN_ATTRIBUTE) && (weight <= MAX_ATTRIBUTE)
				&& (weight >= ((this.getAgility() + this.getStrength()) / 2))) {
			this.weight = weight;
		}
		else if ((this.isCarryingBoulder()) || (this.isCarryingLog())) {
			this.weight = weight;
		}
		else {
			this.weight = (int) ((this.getAgility() + this.getStrength()) / 2);
		}
	}

	/**
	 * Return the strength of this unit.
	 */
	@Basic
	public int getStrength() {
		return this.strength;
	}

	/**
	 * Set the strength of this unit to the given strength.
	 * 
	 * @param strength
	 *            The new strength for this unit.
	 * @post If the given strength is in range of the strength for a unit the
	 *       new strength of this unit is equal to the given strength. | if
	 *       ((strength >= MIN_ATTRIBUTE) && (strength <= MAX_ATTRIBUTE) | then
	 *       new.getStrength == strength
	 */
	public void setStrength(int strength) {
		if ((strength >= MIN_ATTRIBUTE) && (strength <= MAX_ATTRIBUTE)) {
			this.strength = strength;
		}
	}

	/**
	 * Return the agility of this unit.
	 */
	@Basic
	public int getAgility() {
		return this.agility;
	}

	/**
	 * Set the new agility of this unit to the given agility.
	 * 
	 * @param agility
	 *            The new agility for this unit.
	 * @post If the given agility is in range of the agility for a unit the new
	 *       agility of this unit is equal to the given agility. | if ((agility
	 *       >= MIN_ATTRIBUTE) && (agility <= MAX_ATTRIBUTE) | then
	 *       new.getAgility == agility
	 */
	public void setAgility(int agility) {
		if ((agility >= MIN_ATTRIBUTE) && (agility <= MAX_ATTRIBUTE)) {
			this.agility = agility;
		}
	}

	/**
	 * Return the toughness of this unit
	 */
	@Basic
	public int getToughness() {
		return this.toughness;
	}

	/**
	 * Set the new toughness of this unit to the given toughness.
	 * 
	 * @param toughness
	 *            The new toughness for this unit.
	 * @post If the given toughness is in range of the toughness for a unit the
	 *       new toughness of this unit is equal to the given toughness. | if
	 *       ((toughness >= MIN_ATTRIBUTE) && (toughness <= MAX_ATTRIBUTE) |
	 *       then new.getToughness == toughness
	 */
	public void setToughness(int toughness) {
		if ((toughness >= MIN_ATTRIBUTE) && (toughness <= MAX_ATTRIBUTE)) {
			this.toughness = toughness;
		}
	}

	/**
	 * Inspect the current orientation of this unit.
	 */
	@Basic
	public float getOrientation() {
		return this.orientation;
	}

	/**
	 * Change the orientation of this unit to the specified angle.
	 * 
	 * @param orientation
	 *            The new angle of orientation for this unit.
	 * @post If the specified angle is a double precision number between 0 and
	 *       2*PI, inclusively, the orientation of this unit will be changed to
	 *       the specified angle.
	 */
	public void setOrientation(float angle) {
		if ((angle >= -1 * Math.PI) && (angle <= (float) Math.PI)) {
			this.orientation = angle;
		}

	}
		
	public GameObject getCarry() {
		return this.carries;
	}
	
	/*
	 * Let the unit pick up the given game object.
	 */
	public void pickUp(GameObject object) {
		this.carries = object;
		this.setWeight(this.getWeight() + object.getWeight());
	}
	
	/*
	 * Make the unit drop the game object it is (or is not) carrying.
	 */
	public void dropItem() {
		this.getCarry().setCarrier(null);
		this.setWeight(this.getWeight() - this.getCarry().getWeight());
		this.pickUp(null);
	}
	
	/**
	 * Return whether this unit is carrying a log.
	 * @return	True if and only if this unit is carrying a log.
	 * 			| this.getCarry().getType() == LOG
	 */
	public boolean isCarryingLog() {
		if (this.getCarry() != null) {
			return this.getCarry().getType() == Log.TYPE;
		}
		return false;
	}
	
	/**
	 * Return whether this unit is carrying a boulder.
	 * @return	True if and only if this unit is carrying a boulder.
	 * 			| return this.getCarry().getType() == BOULDER
	 */
	public boolean isCarryingBoulder() {
		if (this.getCarry() != null) {
			return this.getCarry().getType() == Boulder.TYPE;
		}
		return false;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Upgrade the equipment of a unit by increasing its toughness and weight by 1.
	 * 
	 * @post 	The new toughness of this unit has increased by 1.
	 * 			| new.getToughness() = this.getToughnes() + 1
	 * 
	 * @post	The new weight of this unit has increased by 1.
	 * 			| new.getWeight() = this.getWeight() + 1
	 */
	public void upgradeEquipment() {
		this.setToughness(this.getToughness() + 1);
		this.setWeight(this.getWeight() + 1);
	}
	
	public Vector3d getTarget() {
		return this.target;
	}
	
	public void setTarget(Vector3d vector) {
		this.target = vector;
	}
	
	public ArrayList<Vector3d> getQueue() {
		return this.queue;
	}
	
	public void resetQueue() {
		this.queue = new ArrayList<Vector3d>();
	}
	
	public void addToQueue(Vector3d cube) {
		this.getQueue().add(cube);
	}
	
	
	/////////////////////////////
	///	HITPOINTS AND STAMINA ///
	/////////////////////////////
	/**
	 * Return the current amount of hitpoints of this unit.
	 */
	public int getHitpoints() {
		return this.hitpoints;
	}
	
	/**
	 * Set this units hitpoints to the specified integer value.
	 * 
	 * @param hitpoints
	 * 			The value to set this units currents hitpoints to.
	 */
	public void setHitpoints(int hitpoints){

		if ((hitpoints >= getMinHitpoints()) && (hitpoints <= this.getMaxHitpoints())) {
			this.hitpoints = hitpoints;
		}

		else if (hitpoints > this.getMaxHitpoints()) {
			this.hitpoints = this.getMaxHitpoints();
		}

		else if (hitpoints < getMinHitpoints()) {
			this.hitpoints = getMinHitpoints();
		}
	}
	
	/**
	 * Inspect the maximal amount of hitpoints of this unit.
	 * @return	ceil(this.weight*this.toughness/50)
	 */
	@Basic
	@Raw
	public int getMaxHitpoints() {
		return (int) Math.ceil(this.getWeight() * this.getToughness() * 0.02);
	}
	
	/**
	 * Inspect the minimal hitpoints this unit can have.
	 * @return 0
	 */
	@Basic
	@Raw
	public int getMinHitpoints() {
		return 0;
	}
	
	/**
	 * Return the current amount of stamina of this unit.
	 */
	public int getStamina() {
		return this.stamina;
	}
	
	/**
	 * Set this units stamina to the specified integer value.
	 * 
	 * @param stamina
	 * 			The value to set this units stamina to
	 */
	public void setStamina(int stamina) {
		if ((stamina >= 0) && (stamina <= this.getMaxHitpoints()))
			this.stamina = stamina;

		else if (stamina > this.getMaxHitpoints())
			this.stamina = this.getMaxHitpoints();

		else if (stamina < getMinHitpoints())
			this.stamina = getMinHitpoints();
	}
	
	
	///////////////////////////
	/// VELOCITY AND MOVING ///
	///////////////////////////
	public double getCurrentSpeed() {
		return this.speed;
	}
	
	public void setSpeed(int z) {
		double basevel = 0.75 * (this.getStrength() + this.getAgility()) / this.getWeight();
		double walkvel;

		if (z < 0) {
			walkvel = 1.2 * basevel;
		} else if (z > 0) {
			walkvel = 0.5 * basevel;
		} else {
			walkvel = basevel;
		}

		if (this.isSprinting()) {
			walkvel = walkvel * 2;
		}
		this.speed = walkvel;
	}
	
	public void stopWalking() {
		this.speed = 0.0;
	}
	
	public Vector3d getDestination() {
		return this.destination;
	}

	public void setDestination(Vector3d destination) {
		this.destination = destination;
	}

	public Vector3d getNextPosition() {
		return this.nextPosition;
	}

	public void setNextPosition(Vector3d nextPosition) {
		this.nextPosition = nextPosition;
	}
	
	public double getMovingTime() {
		return this.movingTime;
	}
	
	public void setMovingTime(double time) {
		this.movingTime = time;
	}
	
	public double getSprintingTime() {
		return this.sprintingTime;
	}
	
	public void setSprintingTime(double time) {
		this.sprintingTime = time;
	}
	
	public void updateSprinting(double dt) {
		this.setSprintingTime(this.getSprintingTime() + dt);
		
		while (this.getSprintingTime() >= 0.1) {
			this.setSprintingTime(this.getSprintingTime() - 0.1);
			this.setStamina(this.getStamina() - 1);
			
			if (this.getStamina() == 0) {
				this.stopSprinting();
			}
		}
	}
	
	/**
	 * Return the velocity of a Unit.
	 */
	public double[] getVelocity() {
		return this.velocity.getDoubleArray();
	}

	/**
	 * Set the velocity of a unit.
	 */
	public void setVelocity(Vector3d velocity) {
		this.velocity = velocity;
	}

	/**
	 * Calculate the velocity of a unit.
	 * 
	 * @return
	 */
	public Vector3d calcVelocity(Vector3d vector) {
		return vector.multiply(this.getCurrentSpeed() / vector.calcNorm());
	}
	
	/**
	 * Update this units position according to its current velocity, 
	 * destination and advanced time dt.
	 * @throws OutOfBoundsException 
	 */
	public void updatePosition(double dt) throws OutOfBoundsException {
		Vector3d oldPos = this.getPosition();
		double[] velocity = this.getVelocity();

		Vector3d newPos = new Vector3d(oldPos.getX() + (dt * velocity[0]),
							oldPos.getY() + (dt * velocity[1]),
							oldPos.getZ() + (dt * velocity[2]));
		
		if (this.getMovingTime() >= this.getTimeNeeded()) {
			this.setPosition(this.getNextPosition());
		} 
		else {
			this.setPosition(newPos);
		}
	}

	/**
	 * Initiate a more complex movement from the unit's current position to
	 * another arbitrary cube of the game world.
	 * 
	 * @param  destination
	 *            The new location to which the unit has to move.
	 *            
	 * @effect this unit will start moving to the specified location, if its current activity can be interrupted.
	 * 
	 * @post   If this units current activity is interruptable, this units status will be "Moving".
	 * 			 |this.status == "Moving"
	 * @post   If this units current activity is interruptable, this units destination will be the vector location.
	 * 			 |this.destination = location
	 */
	public void moveTo(Vector3d  location) {
		if (this.canBeInterrupted("Moving")){
			this.setStatus("Moving");
		}
			
		
		this.setDestination(location);

		int[] nextPos = new int[3];

		for (int i = 0; i < 3; i++) {

			if (this.getPosition().getCube().getDimension(i) == location.getDimension(i)) {
				nextPos[i] = 0;
			} else if (this.getPosition().getCube().getDimension(i) < location.getDimension(i)) {
				nextPos[i] = 1;
			} else {
				nextPos[i] = -1;
			}
		}
		this.moveToAdjacent(new Vector3d(nextPos[0], nextPos[1], nextPos[2]));
	}
	
	public ArrayList<Vector3d> getSuitableCubes(Vector3d c) {
		ArrayList<Vector3d> l = c.createAdjacentVectors();
		World w = this.getWorld();
		
		for (Vector3d cube: l) {
			int[] a = cube.getIntArray();
			if ((cube.equals(c)) || (!w.isPassable(a[0], a[1], a[2])) 
					|| (!w.isSolidConnectedToBorder(a[0], a[1], a[2] - 1))) {
				l.remove(cube);
			}
		}
		return l;
	}
	
	//TODO: finish this function and movement as a whole.
	public void search(Vector3d c0, int n) {
		ArrayList<Vector3d> l = getSuitableCubes(c0);
		for (Vector3d c: l) {

		}
		
	}

	/**
	 * Initiate movement to a game world cube adjacent to the units current
	 * location.
	 * 
	 * @param   vector
	 *            The adjacent cube to which this unit has to move.
	 * @post    If this unit has reached the next cube, it will have updated its current nextPosition, Speed, Velocity, Orientation and TimeNeeded.
	 * 			  |this.nextPosition == nextPos
	 * 			  |this.Speed == vector.getZ()
	 * 			  |this.Velocity == this.calcVelocity()
	 * 			  |this.orientation = atan2(this.velocity.getY(),this.velocity.getX())
	 * 			  |this.timeNeeded == (distance to next cube)/(this.velocity)
	 * @post    If this unit has reached the next cube, its xp will be incremented by 1
	 * 			  |this.xp += 1
	 *            
	 */
	public void moveToAdjacent(Vector3d vector) {	
		Vector3d oldPos = this.getPosition().getCube();	
		Vector3d nextPos = oldPos.add(0.5).add(vector);

		if (this.getWaitingTo() == "Resting")
			this.rest();
		
		if (this.canBeInterrupted("Moving")) {
			this.setStatus("Moving");
			if(this.getWorld().isValidPosition(nextPos)) {
				if(this.nextPositionReached()) {
					this.setNextPosition(nextPos);
					this.setSpeed((int) vector.getZ());
					this.setVelocity(this.calcVelocity(vector));			
					this.setTimeNeeded(vector.calcNorm() / this.getCurrentSpeed());
					this.setMovingTime(0);
					
		
					float vy = (float) this.getVelocity()[1];
					float vx = (float) this.getVelocity()[0];
					this.setOrientation((float) Math.atan2(vy, vx));
					
					this.setXP(this.getExperience() + 1);
				}				
			}
		}
	}
	
	/**
	 * Check whether the unit has reached its destination.
	 */
	public boolean destinationReached() {
		Vector3d dest = this.getDestination();
		Vector3d pos = this.getPosition();
		
		if (!pos.equals(dest.add(0.5))) {
			return false;
		}		
		return true;
	}
	
	/**
	 * Check whether the unit has reached a neighboring cube.
	 */
	public boolean nextPositionReached() {
		Vector3d nextPos = this.getNextPosition();
		Vector3d pos = this.getPosition();
		return pos.equals(nextPos);
	}

	/**
	 * Check whether this unit is sprinting.
	 */
	public boolean isSprinting() {
		return this.movement == "Sprinting";
	}

	/**
	 * Set this units movement status to sprinting.
	 */
	public void startSprinting() {
		if (this.getStamina() > 0)
			this.movement = "Sprinting";
	}

	/**
	 * Stop this unit from sprinting.
	 */
	public void stopSprinting() {
		this.movement = "Walking";
	}
	
	
	
	
	////////////////////////////////////
	/// STATUS AND ACTIVITY PROGRESS ///
	////////////////////////////////////
	/**
	 * Return the current status of this unit.
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * Set the units current status to the specified activity.
	 * 
	 * @post The units activity is changed to the given activity.
	 */
	public void setStatus(String activity) {
		this.status = activity;
	}

	public double getActivityProgress() {
		return this.activityProgress;
	}

	public void setActivityProgress(double progress) {
		this.activityProgress = progress;
	}

	/**
	 * Return the time needed for a unit to complete an activity.
	 */
	public double getTimeNeeded() {
		return this.timeNeeded;
	}

	/**
	 * Calculate the total time needed for a unit to finish its activity.
	 * 
	 * @param status
	 *            The status of this unit.
	 */
	private void setTimeNeeded() {
		if (this.isWorking())
			this.timeNeeded = 500 / this.getStrength();

		if (this.isResting() || this.isInitResting())
			this.timeNeeded = (0.2 * 200 / this.getToughness());
		
		if (this.isAttacking())
			this.timeNeeded = 1;
	}
	
	public void setTimeNeeded(double time) {
		this.timeNeeded = time;
	}

	public double getCounter() {
		return this.counter;
	}

	private void setCounter(double time) {
		this.counter = time;
	}

	
	public String getWaitingTo() {
		return this.waitingTo;
	}
	
	public void setWaitingTo(String arg) {
		this.waitingTo = arg;
	}

	public static String getRandomActivity(String[] activities) {
		int rnd = new Random().nextInt(activities.length);
		return activities[rnd];
	}


	/*
	 * Check whether this unit is idle.
	 */
	public boolean isIdle() {
		return this.getStatus() == "Idle";
	}

	/*
	 * Check whether this unit is moving.
	 */
	public boolean isMoving() {
		return this.getStatus() == "Moving";
	}

	/*
	 * Check whether this unit is working.
	 */
	public boolean isWorking() {
		return this.getStatus() == "Working";
	}

	/*
	 * Check whether this unit is attacking another unit.
	 */
	public boolean isAttacking() {
		return this.getStatus() == "Attacking";
	}

	/*
	 * Check whether this unit is defending itself.
	 */
	public boolean isDefending() {
		return this.getStatus() == "Defending";
	}

	/*
	 * Check whether this unit is resting.
	 */
	public boolean isResting() {
		return this.getStatus() == "Resting";
	}
	
	/*
	 * Check whether this unit is in its initial resting phase.
	 */
	public boolean isInitResting() {
		return this.getStatus() == "InitResting";
	}
	
	/*
	 * Check whether default behavior for this unit is enabled.
	 */
	public boolean isDefaultBehaviorEnabled() {
		return this.enableDefaultBehavior;
	}

	/*
	 * Set the boolean 'enableDefaultBehavior' of this unit to the given boolean value.
	 */
	public void setDefaultBehaviorEnabled(boolean value) {
		this.enableDefaultBehavior = value;
	}

	/**
	 * Start default behavior for a unit.
	 * 
	 * @effect This unit will randomly choose one of three activities namely: working, resting or moving to a random location in the game world. 
	 * @effect This unit will keep choosing and finishing new activities whenever default behavior is enabled.
	 * 
	 * @post   This units default behaviour state will be true
	 * 			|this.defaultBehaviour == true
	 * @post   This units status will we 'Idle'.
	 * 			|this.status == "Idle"
	 */
	public void startDefaultBehavior() {
		if ((this.isIdle()) && (this.isDefaultBehaviorEnabled())) {
			int rnd = ThreadLocalRandom.current().nextInt(0, 2 + 1);
			Vector3d randomLoc = new Vector3d();

			if (rnd == 0) {
				// TODO: random cube kiezen waarschijnlijk
				// we nemen de cube eronder.
				Vector3d target = this.getPosition();
				target.setZ(target.getZ() - 1);
				this.workAt(target);
				
			} else if (rnd == 1) {
				this.rest();
			} else if (rnd == 2) {
				for (int i = 0; i < 2; i++) {
					randomLoc.setDimension(i, ThreadLocalRandom.current().nextInt(0, 49 + 1));
				}
				this.moveTo(randomLoc);
			}
		}
	}

	/**
	 * Disable default behavior for this unit.
	 * 
	 * @post This units default behaviour state will be set to false
	 * 			|this.defaultBehaviour == false 
	 */
	public void stopDefaultBehavior() {
		this.setDefaultBehaviorEnabled(false);
	}

	/**
	 * Check whether the units current activity can be interrupted by the given
	 * interruptor.
	 * 
	 * @param 	interruptor
	 *          The interruptor
	 */
	public boolean canBeInterrupted(String interruptor) {
		if (this.isIdle()) {
			return true;
		}
		if ((this.isWorking()) && (interruptor != "Working"))
			return true;

		if ((this.isResting()) && (interruptor != "Resting"))
			return true;

		if ((this.isInitResting()) && (interruptor == "Fighting"))
			return true;

		if ((this.isMoving()) && (interruptor != "Working"))
			return true;

		if (this.getStatus() == null)
			return true;

		return false;
	}
	
	////////////////
	/// FIGHTING ///
	////////////////
	/**
	 * Initiate an attack move of two Units from a different section, standing
	 * next to each other.
	 * 
	 * @param defender
	 *            the defending Unit
	 * @effect Turn the two Units to each other | if
	 * @effect Update the status of the fighting units, and reset their activity
	 *         progress.
	 * 
	 * @effect
	 */
	public void attack(Unit defender) {
		// TODO: exceptions for when two units are from the same faction.
		// TODO: look again at orientation: not yet on point (see tests: put 9 units in a square and make the centre unit fight everyone else).
		if (this.isAdjacentTo(defender) && this != defender){
			float attackerOr = (float) Math.atan2(defender.getPosition().getY() - this.getPosition().getY(),
                    defender.getPosition().getX() - this.getPosition().getX());
			float defenderOr = (float) Math.atan2(this.getPosition().getY() - defender.getPosition().getY(),
                    this.getPosition().getX() - defender.getPosition().getX());
			this.setOrientation(attackerOr);
			defender.setOrientation(defenderOr);

			this.setActivityProgress(0);
			defender.setActivityProgress(0);

			this.setStatus("Attacking");
			defender.setStatus("Defending");
			this.setTimeNeeded();

			this.setOpponent(defender);
			defender.setOpponent(this);
		}
	}

	/**
	 * Pair two units to each other as opponents.
	 * 
	 * @param   opponent
	 *          The unit to set as opponent.
	 * @effect  The units opponent can be set as this opponent.
	 */
	public void setOpponent(Unit opponent) {
		this.opponent = opponent;
	}

	/**
	 * Inspect this units opponent.
	 * 
	 * @return 	this units opponent in the current attack
	 */
	public Unit getOpponent() {
		return this.opponent;
	}
	
	/**
	 * This method is invocated when a unit finished its attack. The defender will try to dodge, then block, and will take damage if this fails.
	 * The attackers and defenders XP will be also updated accordingly.
	 * 
	 * @param attacker
	 * 			The unit to which this unit is defending.
	 * 
	 * @post If the defending unit succeeds dodging, that unit will move to a random passable location next to his cube.
	 * 		 It will also get 20 XP.
	 * @post If the defending unit succees blocking, that unit will get 20 XP.
	 * 
	 * @post If the defending unit fails dodging and blocking, that unit will take damage based on the attackers attributes.
	 * 		 The attacker will also get 20 XP.
	 */
	public void defend(Unit attacker) {

		double dodgeProb = 0.2 * this.getAgility() / attacker.getAgility();
		boolean dodged = (new Random().nextDouble() <= dodgeProb);

		if (dodged == true) {
			this.dodge();
			int curXP = this.getExperience();
			this.setXP(curXP + 20);
		} else {
			double blockProb = 0.25 * (this.getStrength() - this.getAgility())
					/ (attacker.getStrength() - attacker.getAgility());
			boolean blocked = (new Random().nextDouble() <= blockProb);
			if (blocked != true) {
				attacker.doDamage(this);
				int curXP = attacker.getExperience();
				attacker.setXP(curXP + 20);
			} else {
				int curXP = this.getExperience();
				this.setXP(curXP + 20);
			}

		attacker.setOpponent(null);
		attacker.setStatus("Idle");
		this.setOpponent(null);
		this.setStatus("Idle");
		}
	}
	
	/**
	 * Do damage hitpoints to the defender, and check if the strike is fatal for the defender. If so, terminate the defender.
	 * 
	 * @param defender
	 * 		  The defending unit.
	 */
	
	public void doDamage(Unit defender) {
		double curHealth = defender.getHitpoints();
		double damage = this.getStrength() / 10;
		defender.setHitpoints((int) (curHealth - damage));
		
		if (defender.getHitpoints()<=0) {
			defender.setHitpoints(0);
			defender.die();
		}
	}
	
	/**
	 * Terminate the unit. Drop its' object if it was carrying something, remove it from its' faction, and change its' state to not alive.
	 * 
	 */
	public void die(){
		if (this.carries!=null){
			this.dropItem();
		}
		
		this.getFaction().removeUnit(this);
		this.alive = false;
	}
	
	/**
	 * Return the current living state of this unit
	 * 
	 * @return True if this unit is currently alive.
	 */
	public boolean isAlive(){
		return this.alive;
	}
	
	
	public void dodge() {
		Vector3d pos = this.getPosition();
		Vector3d evasion = new Vector3d();
		boolean foundNewPos = false;
		 while (foundNewPos == false) {
			for (int i = 0; i < 2; i++) {

				double plus = new Random().nextInt(3);
				double randomValue = -1 +  plus;
				evasion.setDimension(i,randomValue);
			}
			Vector3d newPos = new Vector3d();

			for (int i = 0; i < 2; i++)
				newPos.setDimension(i, pos.getDimension(i) + evasion.getDimension(i));
			
			newPos.setDimension(2,this.getPosition().getZ());
			
			foundNewPos = true;
			// TODO create passable terrain check in World class
			try {
				this.setPosition(newPos);
			} catch (OutOfBoundsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
	}

	///////////////
	/// RESTING ///
	///////////////
	/**
	 * This method will initiate resting.
	 * 
	 * @post The units current status will be resting.
	 */
	public void rest() {
		if ((this.isMoving()) && (!(this.getWaitingTo() == "Resting"))) {
			this.setWaitingTo("Resting");
		}
		else if (this.canBeInterrupted("Resting")) {
			if (this.isMoving()) {
				this.setWaitingTo("Moving");
			}
			this.setActivityProgress(0);
			this.setStatus("InitResting");
			this.setTimeNeeded();
		}
	}

	/**
	 * Restore hitpoints and stamina of a unit, when it is resting.
	 * 
	 * @post If this units current stamina is not equal to its maximal amount of
	 *       stamina points, the amount of stamina is increased by 2. | if
	 *       (this.getStamina() != this.getMaxHitpoints()) | then
	 *       new.getStamina() = this.getStamina() + 2
	 * @post If this units current stamina is equal to its maximal stamina and
	 *       this unit is at full health, its status will be set to default.
	 *       Else its hitpoints will be increased by 1. | if ((this.getStamina()
	 *       == this.getMaxHitpoints()) && | (this.getHitpoints !=
	 *       this.getMaxHitpoints())) | then new.getStatus() = "Default" | else
	 *       | new.getHitpoints() = this.getHitpoints() + 1
	 */
	public void restore() {

		if (this.getHitpoints() == this.getMaxHitpoints()) {
			if (this.getStamina() == this.getMaxHitpoints()) {
				if (this.getWaitingTo() == "Moving") {
					this.setStatus("Moving");
					this.setWaitingTo(null);
				}
				else {
					this.setStatus("Idle");
				}
			}
			else {
				this.setStamina(this.getStamina() + 2);
			}
		} 
		else {
			this.setHitpoints(this.getHitpoints() + 1);
		}
	}

	
	///////////////
	/// WORKING ///
	///////////////
	/**
	 * Initiate working at the given position for this unit.
	 * 
	 * @param 	pos
	 * 			The position at which this unit will start working.
	 * @post 	If the units current activity can be interrupted by working, 
	 * 			the given position is within this units worlds bounds, 
	 * 			and the given position is adjacent to the units current position,
	 * 			the unit will start working by updating its status, activity progress, 
	 * 			time needed, target position and orientation.
	 * 			| if (this.canBeInterrupted("Working")) 
	 *			|	if ((this.getWorld().isValidPosition(pos)) &&
	 *			|		(this.isAdjacentTo(pos.roundDown()))) 
	 *			|	then
	 *			| 		new.getActivityProgress() == 0
	 *			|		new.getStatus() == "Working"
	 *			|		new.getTimeNeeded() == this.setTimeNeeded()
	 *			|		new.getTarget() == pos
	 *			|		Vector3d diff = pos.subtract(this.getPosition())
	 *			|		new.getOrientation() == this.setOrientation((float) Math.atan2(diff.getY(), 
	 *			|																		diff.getX()))
	 */
	public void workAt(Vector3d pos) {

		if (this.canBeInterrupted("Working")) {
			if ((this.getWorld().isValidPosition(pos)) &&
					(this.isAdjacentTo(pos.roundDown()))) {
				this.setActivityProgress(0);
				this.setStatus("Working");
				this.setTimeNeeded();
				this.setTarget(pos.roundDown());
				Vector3d diff = pos.subtract(this.getPosition());
				this.setOrientation((float) Math.atan2(diff.getY(), diff.getX()));
			}
			
		}
	}
	
	public void workDone(){
		// TODO: A LOT OF WORK HAS TO BE DONE HERE.
		this.setActivityProgress(0);
		this.setStatus("Idle");
		
		World world = this.getWorld();
		Vector3d pos = this.getPosition();
		int cubeType = world.getCubeType(pos);
		
		if ((this.isCarryingBoulder()) || (this.isCarryingLog())) {
			this.dropItem();
		}
		else if (cubeType == World.TYPE_WORKSHOP) {
			if ((world.isBoulderAvailable(pos)) && (world.isLogAvailable(pos))) {
				this.upgradeEquipment();
			}
		}
		else if(world.isBoulderAvailable(pos)) {
			this.pickUp(world.getSelectedBoulder());			
		}
		else if(world.isLogAvailable(pos)) {
			this.pickUp(world.getSelectedLog());		
		}
		else if (cubeType == World.TYPE_ROCK) {
				world.destroyCube(this.getTarget());
		}
		else if (cubeType == World.TYPE_TREE) {
				world.destroyCube(this.getTarget());
		}
		world.resetSelection();
	}

	////////////////////
	/// ADVANCE TIME ///
	////////////////////
	/**
	 * Advance a unit's game time and update it's position, attributes and status 
	 * according to it's current status.
	 * @param 	dt
	 * 			The amount of time a unit will advance.
	 * @throws 	InterruptedException
	 * @throws 	IllegalArgumentException
	 * 			Throws an IllegalArgumentException if the given dt is larger than 0.2 .
	 */
	public void advanceTime(double dt) {
		if (!isValidDuration(dt)) {
			throw new IllegalArgumentException();
		}

		this.setCounter(this.getCounter() + dt);

		if (this.getCounter() >= REST_INTERVAL) {
			this.rest();
			this.setCounter(0);
		}
		if (this.isIdle()) {
			if (this.isDefaultBehaviorEnabled()) {
				this.startDefaultBehavior();
			}
		}
		if (this.isMoving()) {		
			this.setMovingTime(this.getMovingTime() + dt);
			
			if (this.isSprinting()) {				
				this.updateSprinting(dt);
			}
			
			try {
				this.updatePosition(dt);
			} catch (OutOfBoundsException e) {
				e.printStackTrace();
			}
			
			if (this.getPosition() == this.getNextPosition()) {
				if ((this.destinationReached()) || (this.getDestination().getX() == -1)) {				
					this.setStatus("Idle");	
					this.setDestination(new Vector3d(-1,-1,-1));
				}
				else {
					this.moveTo(this.getDestination());
				}
			}	
		}		
		else if ((this.isInitResting()) || (this.isResting())) {

			this.setActivityProgress(this.getActivityProgress() + dt);

			if (this.getActivityProgress() >= this.getTimeNeeded()) {
				this.setStatus("Resting");
				this.restore();
				this.setActivityProgress(0);
			}
		} 
		else if (this.isWorking()) {
			this.setActivityProgress(this.getActivityProgress() + dt);
			if (this.getActivityProgress() >= this.getTimeNeeded()) {
				this.workDone();				
							
				int curXP = this.getExperience();
				this.setXP (curXP + 10);
			}
		} 
		else if (this.isAttacking()) {
			Unit defender = this.getOpponent();

			double attacked = this.getActivityProgress();
			this.setActivityProgress(attacked + dt);

			if (this.getActivityProgress() >= 1) {
				defender.defend(this);
			}
		}
	}


	/**
	 * Check whether the given duration is a valid duration to advance the time.
	 * 
	 * @param duration
	 *            The duration to check.
	 * @return True if and only if the given duration is larger than or equal to
	 *         zero, and always smaller than 0.2. | result == ((duration < 0) ||
	 *         (duration >=0.2))
	 */
	public static boolean isValidDuration(double duration) {
		if ((duration < 0) || (duration >= 0.2)){
			return false;
		}
		return true;
	}
	

	
	
	
	///////////////////////////////
	/// EXPERIENCE AND LEVELING ///
	///////////////////////////////
	
	public int getExperience(){
		return this.experience;
	}
	
	public void setXP(int experience){
		this.experience = experience;
		
		if (this.getExperience() >= 10) {
			this.levelUp();
		}
	}
	
	
	private void levelUp(){
		int curXP = this.getExperience();
		this.setXP(curXP - 10);
		
		int strength = this.getStrength();
		int agility = this.getAgility();
		int toughness = this.getToughness();
		int attribute = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		
		if (attribute == 0) {
			this.setStrength(strength + 1);
		}
		else if (attribute == 1) {
			this.setAgility(agility + 1);
		}		
		else if (attribute == 2) {
			this.setToughness(toughness + 1);
		}
		
		this.setWeight(this.getWeight());
	}
	
	
	
	
	////////////////////
	/// UNIT FACTION ///
	////////////////////
	
	/**
	 * Return the faction to which this unit belongs.
	 */
	public Faction getFaction() {
		return this.faction;
	}
	
	/**
	 * Add this unit to the given faction.
	 * @param 	faction
	 * 			The faction to which the unit will be added
	 * 
	 * @post 	The units new faction will be faction
	 * 			| new.getFaction == faction
	 */
	public void setFaction(Faction faction) {
			this.faction = faction;
	}
}


