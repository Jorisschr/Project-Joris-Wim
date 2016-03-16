package hillbillies.model;

import helperclasses.OutOfBoundsException;
import helperclasses.Vector3d;

public class Boulder extends Object {
	/**
	 * Initialize a new Boulder on a given location with a random weight 
	 * between 10 and 50, inclusively
	 * 
	 * @param   position
	 * 			The starting postion of this Boulder
	 * @throws 	OutOfBoundsException
	 * 			The given postion is out of bounds.
	 * 			|!isValidPostion(position)
	 */
	public Boulder(Vector3d position) throws OutOfBoundsException{
		super(position);
	}
	
	/**
	 * Advances the time with a given duration, between 0 and 0.2 seconds.
	 * 
	 * @param   duration
	 * 			The duration to advance the time with.
	 * @effect	if this Boulder is falling, the new Z-coordinate of this Boulder will be 3*duration lower.
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
