package hillbillies.model;

import helperclasses.OutOfBoundsException;
import helperclasses.Vector3d;

public class Log extends GameObject {
	/**
	 * Initialize a new Log on a given location with a random weight 
	 * between 10 and 50, inclusively
	 * 
	 * @param   position
	 * 			The starting postion of this Log
	 * @throws 	OutOfBoundsException
	 * 			The given postion is out of bounds.
	 * 			|!isValidPostion(position)
	 */
	public Log(Vector3d position) throws OutOfBoundsException{
		super(position);
		Log.type = TYPE;
		this.carrier = null;
		this.world = null;
	}
	
	private static int type;
	private Unit carrier;
	private World world;
	public static int TYPE = 0;
	public int getType() {
		return type;
	}
	
	public Unit getCarrier() {
		return this.carrier;
	}
	
	public void setCarrier(Unit unit) {
		this.carrier = unit;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	/**
	 * Advances the time with a given duration, between 0 and 0.2 seconds.
	 * 
	 * @param   duration
	 * 			The duration to advance the time with.
	 * @effect	if this Log is falling, the new Z-coordinate of this Log will be 3*duration lower.
	 */
	public void advanceTime(double duration){
		if (!this.canStand() && !this.isFalling()){
			this.startFalling();
		}
		
		this.updatePosition(duration);
		Vector3d curPos = this.getPosition();
		double newZ = curPos.getZ()-duration*3;
		Vector3d newPos = new Vector3d(curPos.getX(),curPos.getY(),newZ);
		try {
			this.setPosition(newPos);
		} catch (OutOfBoundsException e) {}
	}
	
}
