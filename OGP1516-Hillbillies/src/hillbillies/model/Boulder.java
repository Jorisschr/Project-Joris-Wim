package hillbillies.model;

import helperclasses.OutOfBoundsException;
import helperclasses.Vector3d;

public class Boulder {
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
		Vector3d middle = new Vector3d(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5 );
		
		if (!position.isValidPosition()){
			throw new OutOfBoundsException(position.getDouble());
		}
		
		this.position = middle;
		this.velocity = new Vector3d(0,0,0);
	}
	
	private Vector3d position;
	private Vector3d velocity;
	
	/**
	 * Set a new position for this Boulder.
	 * @param   position
	 * 			The new position for this boulder to be.
	 * @throws  OutOfBoundsException
	 * 			The new position is not within the boundairies of the game World.
	 * 			|!isValidPosition(position)
	 */
	private void setPosition(Vector3d position) throws OutOfBoundsException{
		if(!position.isValidPosition()){
			throw new OutOfBoundsException(position.getDouble());
		}
		this.position = position;
	}
	
	/**
	 * Return the current position of this Boulder
	 * @return  The position of this Boulder
	 */
	public Vector3d getPosition(){
		return this.position;
	}
	
	/**
	 * Set a new velocity for this Boulder.
	 * @param   velocity
	 * 			The new velocity for this boulder to have.
	 */
	private void setVelocity(Vector3d velocity){
		this.velocity = velocity;
	}
	
	/**
	 * Return the current velocity of this Boulder
	 * @return  The velocity of this Boulder.
	 */
	public Vector3d getVelocity(){
		return this.velocity;
	}
	
	/**
	 * Checks if this Boulder has solid ground beneath it.
	 * @return	true if the cube below this boulder is solid.
	 * 			| blockType(x,y,z-1) == Solid
	 */
	public boolean canStand(){
		Vector3d position = this.getPosition();
		position.setZ(position.getZ()-1);
		return World.isSolid(position);
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
	
	private boolean isFalling() {
		return (this.getVelocity().getZ() == -3 );
	}

	public void startFalling(){
		double[] newVel = {this.getVelocity().getX(),this.getVelocity().getY(),this.getVelocity().getZ()-3};
		
		Vector3d velocity = new Vector3d(newVel);
		this.setVelocity(velocity);
	}
	
	public void stopFalling(){
		double[] newVel = {this.getVelocity().getX(),this.getVelocity().getY(),this.getVelocity().getZ()+3};
		
		Vector3d velocity = new Vector3d(newVel);
		this.setVelocity(velocity);
	}
	
	public void updatePosition(double dt) {
		Vector3d oldPos = this.getPosition();
		Vector3d velocity = this.getVelocity();

		Vector3d newPos = new Vector3d(oldPos.getX() + (dt * velocity.getX()),
							oldPos.getY() + (dt * velocity.getY()),
							oldPos.getZ() + (dt * velocity.getZ())); 
		try {
			this.setPosition(newPos);
		} catch (OutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
