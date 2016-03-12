package hillbillies.model;
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
 * @author 	Joris Schrauwen, Wim Schmitz
 * 			
 * 
 * @invar 	Each unit can have its position as position.
 * 			| isValidPosition(this.getPosition())
 * @invar  	Each unit can have its name as name.
 * 		   	| canHaveAsName(this.getName())
 *
 */
public class Unit {
	
/**
	 * Initialize this new unit with given position, name, weight, 
	 * strength, agility and toughness.
	 * 
	 * @param	position
	 * 			The default position for this new unit.
	 * @param	name
	 * 			The name for this new unit.
	 * @param	weight
	 * 			The weight for this new unit.
	 * @param	strength
	 * 			The strength for this new unit.
	 * @param 	agility
	 * 			The agility for this new unit.
	 * @param	toughness
	 * 			The toughness for this new unit.
	 * @param 	orientation.
	 * 			The orientation for this new unit.
	 * @post	The position of this new unit is the given position.
	 * 			| new.getPosition() == position
	 * @post    The name of this new unit is equal to the given name.
	 *        	| new.getName() == name
	 * @throws	OutOfBoundsException
	 * 			The given position is out of bounds.
	 * 			| ! isValidPosition(position)
	 * @throws  IllegalArgumentException
	 *         	This new unit cannot have the given name as its name.
	 *       	| ! canHaveAsName(this.getName())
	 */
	public Unit(String name, int[] position, int weight, int agility, 
				int strength, int toughness, boolean enableDefaultBehavior) 
					throws OutOfBoundsException, IllegalArgumentException {
		
		double[] pos = {(double) position[0] + 0.5, (double) position[1] + 0.5, (double) position[2] + 0.5};	
		if (!isValidPosition(pos)) {
			//display message?
			throw new OutOfBoundsException(pos);
		}
		
		if (!canHaveAsName(name)) {
			// display message?
			throw new IllegalArgumentException(name);
		}
		
		this.position = pos;	
		this.name = name;
		
		if (isWithinRange(strength)) {	
			this.setStrength(strength);	
		}
		else {
			this.setStrength(25);
		}
		
		if (isWithinRange(agility)) {
			this.setAgility(agility);
		}
		else {
			this.setAgility(25);
		}
		
		if (isWithinRange(toughness)) {
			this.setToughness(toughness);
		}
		else {
			this.setToughness(25);
		}
		
		if (isWithinRange(weight)) {
			this.setWeight(weight);
		}
		else {
			this.setWeight((int) ((this.getAgility() + this.getStrength()) / 2));
		}
		
		this.orientation = (float) Math.PI/2;
		this.stamina = getMaxHitpoints();
		this.hitpoints = getMaxHitpoints();
		this.interrupted = false;
		this.movement = "Walking";
		this.enableDefaultBehavior = enableDefaultBehavior;
		this.status = "Idle";
		this.speed = 0;
		this.velocity = new double[] {0, 0, 0};
		this.activityProgress = 0;
		this.timeNeeded = 0;
		this.destination = new int[] {0, 0, 0};
		this.nextPosition = new double[] {0.5, 0.5, 0.5};
		
	}
	
	/**
	 * Variable registering the position of this unit.
	 */
	private double[] position;
	
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
	
	/*
	 * Variable registering the unit's movement status.
	 */
	private String movement;
	
	/*
	 * Variable registering whether the unit's behavior is being interrupted.
	 */
	private boolean interrupted;
	
	/**
	 * Variable registering the passed time since the unit started its current activity
	 */
	private double counter;
	
	/**
	 * Variable registering whether default behavior is enabled for this unit.
	 */
	private boolean enableDefaultBehavior;
	
	private double speed;
	
	private double[] velocity;
	
	private double activityProgress;
	
	private double timeNeeded;
	
	private int[] destination;
	
	private double[] nextPosition;
	
	/**
	 * Variable registering the lower bound for the x, y and z
	 * dimensions of the generated world.
	 */
	private static final int LOWER_BOUND = 0;
	
	/**
	 * Variable registering the upper bound for the x, y and z
	 * dimensions of the generated world.
	 */
	private static final int UPPER_BOUND = 50;
	
	/**
	 * Return the position of this unit.
	 */
	@Basic
	public double[] getPosition(){
		return this.position;
	}
	
	/**
	 * Check whether the given position is a valid position for a unit.
	 * @param 	position
	 * 			The position to check.
	 * @return	True if and only if all doubles of the given position
	 * 			are larger than or equal to the lower bound and smaller 
	 * 			than or equal to the upper bound.
	 * 			| result == (for (int i = 0; i < position.length;)
	 *			| 				((position[i] > LOWER_BOUND) && 
	 *			|					(position[i] < UPPER_BOUND)))
	 */
	public boolean isValidPosition(double[] position){
		for (int i = 0; i < position.length; i++) {
			if ((position[i] < LOWER_BOUND) || (position[i] > UPPER_BOUND)) {
				return false;
			}
		}
		return true;
	}
	
	private void setPosition(double[] newPos) {
		if (isValidPosition(newPos)) {
			this.position = newPos;
		}
	}
		
	/**
	 * Return the name of this unit.
	 */
	@Basic @Raw @Immutable
	public String getName() {
		return this.name;
	}
	
	public void setName(String newName) {
		if (this.canHaveAsName(newName)) {
			this.name = newName;
		}
	}
	
	/**
	 * Check whether this unit can have the given name as its name.
	 *  
	 * @param   name
	 *          The name to check.
	 * @return  True if and only if the name uses 2 characters or more, 
	 * 			the first letter is a capital one and all characters
	 * 			are either letters, spaces or quotes.
	 *       	| result == (name != null)
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
		for (char i : chars)  {
			if ((!Character.isLetter(i)) && (i !=' ') && 
					(i != '\'') && (i != '"')) {
				return false;
		    }
		}
		return true;
	}
	
	public boolean isWithinRange(int value) {
		return ((value >= 25) && (value <= 100));
	}

	/**
	 * Return the position of the cube occupied by this unit.
	 */
	public int[] getCube(){
		int[] cubeposition = new int[3];
		for (int i = 0; i < cubeposition.length; i++) {
		    cubeposition[i] = (int) this.position[i];
		}
		return cubeposition;
	}
	
	/**
	 * Return the position of the cube occupied by this position.
	 */
	public int[] getCube(double[] position) {
		int[] cubeposition = new int[3];
		for (int i =0; i < cubeposition.length; i++) {
			cubeposition[i] = (int) position[i];
		}
		return cubeposition;
	}
	
	/**
	 * Constant reflecting the lowest possible value 
	 * for an attribute of a unit.
	 * 
	 * @return 	The lowest possible value for all attributes
	 * 			of all units is 1.
	 * 			| result == 1
	 */
	public static final int MIN_ATTRIBUTE = 1;
	
	/**
	 * Constant reflecting the highest possible value 
	 * for an attribute of a unit.
	 * 
	 * @return 	The highest possible value for all attributes 
	 * 			of all units is 200.
	 * 			| result == 200
	 */
	public static final int MAX_ATTRIBUTE = 200;
	
	/**
	 * Constant reflecting the duration after which a Unit will stop its current activity, and start resting.
	 * 
	 * @return	The interval to do an activity.
	 * 		| result == 180
	 */
	public static final double REST_INTERVAL = 180;
	
	/**
	 * Return the weight of this unit.
	 */
	@Basic
	public int getWeight(){
		return this.weight;
	}
	
	/**
	 * Set the weight of this unit to the given weight.
	 * 
	 * @param  	weight
	 * 			The new weight for this unit.
	 * @post  	If the given weight is in range of the weight for a unit  
	 * 			and the given weight is at least the sum of the unit's
	 * 			strength and agility divided by 2, then the new weight of this unit 
	 * 			is equal to the given weight.
	 * 			| if ((weight >= MIN_ATTRIBUTE) && (weight <= MAX_ATTRIBUTE) && 
	 * 			|		(weight >= (this.getAgility() + this.getStrength()) / 2))
	 * 			|	then new.getWeight == weight
	 */
	public void setWeight(int weight) {
		if ((weight >= MIN_ATTRIBUTE) && (weight <= MAX_ATTRIBUTE) && 
				(weight >= ((this.getAgility() + this.getStrength()) / 2))) {
			this.weight = weight;
		}
	}
	
	/**
	 * Return the strength of this unit.
	 */
	@Basic
	public int getStrength(){
		return this.strength;
	}
	
	/**
	 * Set the strength of this unit to the given strength.
	 * 
	 * @param 	strength
	 * 			The new strength for this unit.
	 * @post 	If the given strength is in range of the strength for a unit
	 *			the new strength of this unit is equal to the given strength.
	 *			| if ((strength >= MIN_ATTRIBUTE) && (strength <= MAX_ATTRIBUTE)
	 *			|	then new.getStrength == strength
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
	public int getAgility(){
		return this.agility;
	}
	
	/**
	 * Set the new agility of this unit to the given agility.
	 * 
	 * @param 	agility
	 * 			The new agility for this unit.
	 * @post	If the given agility is in range of the agility for a unit
	 *			the new agility of this unit is equal to the given agility.
	 *			| if ((agility >= MIN_ATTRIBUTE) && (agility <= MAX_ATTRIBUTE)
	 *			|	then new.getAgility == agility
	 */
	public void setAgility(int agility){
		if ((agility >= MIN_ATTRIBUTE) && (agility <= MAX_ATTRIBUTE)) {
			this.agility = agility;
		}
	}
	
	/**
	 * Return the toughness of this unit
	 */
	@Basic
	public int getToughness (){
		return this.toughness;
	}
	
	/**
	 * Set the new toughness of this unit to the given toughness.
	 * 
	 * @param 	toughness
	 * 			The new toughness for this unit.
	 * @post	If the given toughness is in range of the toughness for a unit
	 *			the new toughness of this unit is equal to the given toughness.
	 *			| if ((toughness >= MIN_ATTRIBUTE) && (toughness <= MAX_ATTRIBUTE)
	 *			|	then new.getToughness == toughness
	 */
	public void setToughness(int toughness){
		if( (toughness >= MIN_ATTRIBUTE) && (toughness <= MAX_ATTRIBUTE)) {
			this.toughness = toughness;
		}
	}
	
	/**
	 * Inspect the current orientation of this unit.
	 */
	@Basic
	public float getOrientation (){
		return this.orientation;
	}

	/**
	 * Change the orientation of this unit to the specified angle.
	 * @param  	orientation
	 * 			The new angle of orientation for this unit.
	 * @post	If the specified angle is a double precision number between 0 and 2*PI, inclusively,
	 * 			the orientation of this unit will be changed to the specified angle.
	 */
	private void setOrientation(float angle){
		if( (angle >= 0) && (angle <= (float) 2*Math.PI)) {
			this.orientation = angle;
		}
		
	}

	/**
	 * Return the current amount of hitpoints of this unit.
	 */
	public int getHitpoints() {
		return this.hitpoints;
	}
	
	/**
	 * Inspect the maximal amount of hitpoints of this unit.
	 */
	@Basic @Immutable @Raw
	public int getMaxHitpoints(){
		return (int) Math.ceil(this.getWeight()*this.getToughness()* 0.02);
	}
	
	public int getMinHitpoints(){
		return 0;
	}
	
	public double getCurrentSpeed() {
		return this.speed;
	}
	
	public void setSpeed(int z) {	
		double basevel = 0.75 * (this.getStrength() + this.getAgility()) / this.getWeight();	
		double walkvel;

		if (z > 0) {
			walkvel = 1.2 * basevel;
		}
		else if (z < 0) {
			walkvel = 0.5 * basevel;
		}
		else {
			walkvel = basevel;
		}
		
		if (this.isSprinting()) {
			walkvel = walkvel * 2;
		}
		this.speed = walkvel;
	}
	
	

	private void setHitpoints(int hitpoints){
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
	 * Return the current amount of stamina of this unit.
	 */
	public int getStamina() {
		return this.stamina;
	}
	
	private void setStamina(int stamina){
		if ((stamina >= 0) && (stamina <= this.getMaxHitpoints()))
			this.stamina = stamina;
		
		else if (stamina > this.getMaxHitpoints())
			this.stamina = this.getMaxHitpoints();
		
		else if (stamina < getMinHitpoints())
			this.stamina = getMinHitpoints();
	}

	/**
	 * Return the current status of this unit.
	 */
	private String getStatus(){
		return this.status;
	}

	/**
	 * Set the units current status to the specified activity.
	 * 
	 * @post The units activity is changed to the given activity.
	 */
	private void setStatus(String activity){
		this.status = activity;
	}
	
	public double getActivityProgress() {
		return this.activityProgress;
	}
	
	public void setActivityProgress(double progress) {
		this.activityProgress = progress;
	}
	
	public int[] getDestination() {
		return this.destination;
	}
	
	public void setDestination(int[] destination) {
		this.destination = destination;
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
	 * @param 	status
	 * 			The status of this unit.
	 */
	public void setTimeNeeded(String status) {
		if (status == "Working")
			this.timeNeeded = 500 / this.getStrength();
			
		if ((status == "Resting") || (status == "InitResting"))
			this.timeNeeded = (0.2 * 200 / this.getToughness()) ;		
	}

	public double getCounter(){
		return this.counter;
	}
	
	private void setCounter(double time){
		this.counter = time;
	}
	
	public double[] getNextPosition() {
		return this.nextPosition;
	}
	
	public void setNextPosition(double[] nextPosition) {
		this.nextPosition = nextPosition;
	}

	public static String getRandomActivity(String[] activities) {
	    int rnd = new Random().nextInt(activities.length);
	    return activities[rnd];
	}
	
	/**
	 * Return the velocity of a Unit.
	 */
	public double[] getVelocity(){	
		return this.velocity;	
	}
	
	/**
	 *  Set the velocity of a unit.
	 */
	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Calculate the velocity of a unit.
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public double[] calcVelocity(int x, int y, int z) {
		double d = calcDistance(x, y, z);
		double curSpeed = this.getCurrentSpeed();
		double[] velocity = { curSpeed * x / d,
								curSpeed * y / d,
								curSpeed * z / d};		
		return velocity;
	}

	/**
	 * Calculate the distance between two points in the game world.
	 * @throws	OutOfBoundsException
	 * 			The given position is out of bounds.
	 * 			| ! isValidPosition(position)
	 */
	public double calcDistance(int x, int y, int z) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	public void attack(Unit defender){
		float attackerOr = (float)Math.atan2(defender.getPosition()[1]-this.getPosition()[1],defender.getPosition()[0]-this.getPosition()[0]);
		float defenderOr = (float)Math.atan2(this.getPosition()[1]-defender.getPosition()[1],this.getPosition()[0]-defender.getPosition()[0]);
		
		this.setOrientation(attackerOr);
		defender.setOrientation(defenderOr);
		
		for(int i=1; i<5; i++)
			try {
				this.advanceTime(0.2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		
		double dodgeProb = 0.2*defender.getAgility()/this.getAgility();
		boolean dodged = (new Random().nextDouble() <= dodgeProb);
		
		if (dodged == true){
			double[] pos = defender.getPosition();
			double[] evasion = {0,0,0};
			for(int i=0; i<2; i++){
				
				double plus = new Random().nextDouble();
				double randomValue = -1 + 2 * plus;
				evasion[i]= randomValue;
			}
			double [] newPos = new double[3];
			
			for (int i = 0; i < pos.length; i++)
				
				newPos[i] = pos[i] + evasion[i];
			
			defender.setPosition(newPos);
		}
		
		else{
			double blockProb = 0.25*(defender.getStrength()-defender.getAgility())/(this.getStrength()-this.getAgility());
			boolean blocked = (new Random().nextDouble() <= blockProb);
			if (blocked != true){
				double curHealth = defender.getHitpoints();
				double damage = this.getStrength()/10;
				defender.setHitpoints((int) (curHealth-damage));
			}
		}
	}
	/**
	 * This method will initiate resting.
	 * 
	 * @post The units current status will be resting.
	 */
	public void rest(){
		
		if (this.canBeInterrupted("Resting")) {
			this.setStatus("InitResting");
			this.setTimeNeeded("Resting");
			this.setActivityProgress(0);
		}
	}
	
	/**
	 * Restore hitpoints and stamina of a unit, when it is resting.
	 * 
	 * @post 	If this units current stamina is not equal to its maximal amount of stamina points,
	 * 			the amount of stamina is increased by 2. 
	 * 			| if (this.getStamina() != this.getMaxHitpoints())
	 * 			|	then new.getStamina() = this.getStamina() + 2
	 * @post	If this units current stamina is equal to its maximal stamina and this unit is
	 * 			at full health, its status will be set to default. Else its hitpoints will be
	 * 			increased by 1.
	 * 			| if ((this.getStamina() == this.getMaxHitpoints()) &&
	 * 			|		(this.getHitpoints != this.getMaxHitpoints()))
	 * 			|	then new.getStatus() = "Default"
	 * 			| else
	 * 			|	new.getHitpoints() = this.getHitpoints() + 1
	 */
	public void restore() {
	
		if (this.getHitpoints() == this.getMaxHitpoints()) {
			if (this.getStamina() == this.getMaxHitpoints()) {
				this.setStatus("Default");
			}
		
			else {
				this.setStamina(this.getStamina() + 2);
			}
		}
		else {	
			this.setHitpoints(this.getHitpoints() + 1);
		}
	}

	public void work() {
		
		if (this.canBeInterrupted("Working")) {
			this.setTimeNeeded("Working");
			this.setActivityProgress(0);
			this.setStatus("Working");
		}
	}

	/**
	 * Advance a unit's game time and update it's position, attributes and status 
	 * according to it's current status.
	 * @param 	dt
	 * 			The amount of time a unit will advance.
	 * @throws InterruptedException
	 * @throws 	IllegalArgumentException
	 * 			Throws an IllegalArgumentException if the given dt is larger than 0.2 .
	 */
	public void advanceTime(double dt) throws IllegalArgumentException, InterruptedException {
		if (!isValidDuration(dt)) {
			throw new IllegalArgumentException();
		}
		
		TimeUnit.SECONDS.sleep((long) dt);
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
		if (this.isFighting()) {
			return;
		}
		if (this.isMoving()) {
			
			if (this.getPosition() == this.getNextPosition()) {
				if (this.getCube() == this.getDestination()) {
					this.setStatus("Idle");
				}
				else {
					this.moveTo(this.getDestination());
				}
			}
			this.updatePosition(dt);
		}		
		if ((this.isInitResting()) || (this.isResting())) {		
			this.setActivityProgress(this.getActivityProgress() + dt);
			
			if (this.getActivityProgress() >= this.getTimeNeeded()) {
				this.setStatus("Resting");
				this.restore();
				this.setActivityProgress(0);
			}
		}	
		if (this.isWorking()) {
			// 'cast': if work isn't completed nothing happens, no progress is saved.
			// gain 10 xp if work is finished and change game world.
			this.setActivityProgress(this.getActivityProgress() + dt);
			if (this.getActivityProgress() >= this.getTimeNeeded()) {
				// break log / rock
				// update xp
				this.setActivityProgress(0);
				this.setStatus("Default");
			}
		}			
	}
	
	/**
	 * Constant reflecting the length of any side of a cube of the game world.
	 * 
	 * @return 	The length of all sides of all cubes of the game world is 1.
	 * 			| result == 1
	 */
	public static final int CUBE_LENGTH = 1;
	
	/**
	 * Check whether the given duration is a valid duration to advance the time.
	 * 
	 * @param 	duration
	 * 			The duration to check.
	 * @return	True if and only if the given duration is larger than or equal to zero, 
	 * 			and always smaller than 0.2.
	 * 			| result == ((duration < 0) || (duration >=0.2))
	 */
	public static boolean isValidDuration(double duration){
			if ((duration < 0) || (duration >=0.2))
				return false;
		return true;
	}
	
	public void updatePosition(double dt) {
		double[] oldPos = this.getPosition();
		double[] velocity = this.getVelocity();
		double[] newPos = { oldPos[0] + (dt * velocity[0]),
							oldPos[1] + (dt * velocity[1]),
							oldPos[2] + (dt * velocity[2])};
		
		if (this.destinationReached(newPos, this.getNextPosition())) {
			this.setPosition(this.getNextPosition());
		}
		else {
			this.setPosition(newPos);
		}
	}
	
	/**
	 * Initiate a more complex movement from the unit's current position to another
	 * arbitrary cube of the game world.
	 * @param 	destination
	 * 			The new location to which the unit has to move.
	 */
	public void moveTo(int[] location){
		if (this.canBeInterrupted("Moving")) {
			this.setStatus("Moving");
			this.setDestination(location);
			
			int[] nextPos = new int [3];
			
			for (int i = 0; i < 3; i++) {
				
				if (this.getCube()[i] == location[i]) {
					nextPos[i] = 0;
				}
				else if (this.getCube()[i] < location[i]) {
					nextPos[i] = 1;
				}
				else if (this.getCube()[i] > location[i]) {
					nextPos[i] = -1;
				}
			}
			this.moveToAdjacent(nextPos[0], nextPos[1], nextPos[2]);
		}
	}
	
	/**
	 * Initiate movement to a game world cube adjacent to the unit's current location.
	 * 
	 * @param 	targetPos
	 * 			The adjacent cube to which this unit has to move.
	 */
	public void moveToAdjacent(int x, int y, int z) {
		if (this.canBeInterrupted("Moving")) {
			this.setStatus("Moving");
			
			this.setSpeed(z);
			this.setVelocity(this.calcVelocity(x, y, z));
			
			float vy = (float) this.getVelocity()[1];
			float vx = (float) this.getVelocity()[0];
			this.setOrientation((float) Math.atan2(vy, vx));
			
			double[] oldPos = this.getPosition();
			this.setNextPosition(new double[] {oldPos[0] + x + 0.5, 
												oldPos[1] + y + 0.5, 
												oldPos[2] + z + 0.5});
		}
	}
	
	/**
	 * Check whether the given double precision number lies between the given borders.
	 * @param 	x
	 * 			The double precision number to be checked.
	 * @param 	a
	 * 			One of the borders of the interval.
	 * @param 	b
	 * 			The other border of the interval.
	 */
	public static boolean intervalContains(double x, double a, double b) {
		if ((x < (a - (int) a)) && (x > (b - (int) b)))
			return true;
		if ((x < (b - (int) b)) && (x > (a - (int) a)))
			return true;
		return false;
	}
	
	public boolean destinationReached(double[] newPos, double[] target) {
		double[] velocity = this.getVelocity();
		
		if (this.getCube(newPos) == this.getCube(target)) {
			for (int i = 0; i < 3; i++) {
				if ((velocity[i] < 0) && (newPos[i] > target[i])) {
					return false;				
				}
				if ((velocity[i] > 0) && (newPos[i]) < target[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean isInterrupted() {
		return this.interrupted;
	}
	
	public void setInterruption(boolean flag) {
		this.interrupted = flag;
	}
	
	public boolean isSprinting(){
		return this.movement == "Sprinting";
	}
	
	public void startSprinting() {
		if (this.getStamina() > 0)
			this.movement = "Sprinting";
	}
	
	public void stopSprinting() {
		this.movement = "Walking";
	}
	
	public boolean isIdle() {
		return this.getStatus() == "Idle";
	}
	
	public boolean isMoving() {
		return this.getStatus() == "Moving";
	}
	
	public boolean isWorking() {
		return this.getStatus() == "Working";
	}
	
	public boolean isResting() {
		return this.getStatus() == "Resting";
	}
	
	public boolean isInitResting() {
		return this.getStatus() == "Initial Resting";
	}
	
	public boolean isFighting() {
		return this.getStatus() == "Fighting";
	}
	
	public boolean isDefaultBehaviorEnabled() {
		return this.enableDefaultBehavior;
	}
	
	public void setDefaultBehaviorEnabled(boolean value) {
		this.enableDefaultBehavior = value;
	}
	
	/**
	 * Start default behavior for a unit. This unit will randomly choose one of three activities namely: 
	 * working, resting or moving to a random location in the game world. 
	 * This unit will keep choosing and finishing activities whenever default behavior is enabled
	 * and this units status becomes idle.
	 */
	public void startDefaultBehavior() {
		if ((this.isIdle()) && (this.isDefaultBehaviorEnabled())) {
			int rnd = ThreadLocalRandom.current().nextInt(0, 2 + 1);
			int[] randomLoc = new int[3];
			
			if (rnd == 0) {
				this.work();
			}
			else if (rnd == 1) {
				this.rest();
			}
			else if (rnd == 2) {
				for (int i = 0; i < 3; i++) {
					randomLoc[i] = ThreadLocalRandom.current().nextInt(0, 49 + 1);
				}
				this.moveTo(randomLoc);				
			}
		}
	}
	
	public void stopDefaultBehavior () {
		this.setDefaultBehaviorEnabled(false);
	}
	
	/**
	 * Check whether the units current activity can be interrupted by the given interruptor.
	 * 
	 * @param 	interruptor
	 * 			The interruptor
	 */
	public boolean canBeInterrupted(String interruptor) {
		if (this.isIdle()) {
			return true;
		}
		if ((this.isWorking()) && (interruptor != "Working")) {
			return true;
		}
		if ((this.isResting()) && (interruptor != "Resting")){
			return true;
		}		
		if ((this.isInitResting()) && (interruptor == "Fighting")) {
			return true;
		}	
		if ((this.isMoving()) && (interruptor != "Working")) {
			return true;
		}
		if (this.getStatus() == null) {
			return true;
		}
		return false;
	}
}


