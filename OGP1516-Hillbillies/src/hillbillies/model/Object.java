package hillbillies.model;

import java.util.Random;

import helperclasses.OutOfBoundsException;
import helperclasses.Vector3d;

public abstract class Object {
	
	public Object(Vector3d position) throws OutOfBoundsException{
		Vector3d middle = new Vector3d(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5 );
		
		if (!position.isValidPosition()){
			throw new OutOfBoundsException(position.getDouble());
		}
		Random r = new Random();
		double weight = 10 + 40 * r.nextDouble();
		this.weight = weight;
		this.position = middle;
		this.velocity = new Vector3d(0,0,0);
		this.falling = false;
	}
	
	private double weight;
	private Vector3d position;
	private Vector3d velocity;
	private boolean falling;
	
	/**
	 * Return the weight of this object
	 * @return this.weight
	 */
	public double getWeight(){
		return this.weight;
	}
	/**
	 * Set a new position for this Object.
	 * @param   position
	 * 			The new position for this Object to be.
	 * @throws  OutOfBoundsException
	 * 			The new position is not within the boundairies of the game World.
	 * 			|!isValidPosition(position)
	 */
	public void setPosition(Vector3d position) throws OutOfBoundsException{
		if(!position.isValidPosition()){
			throw new OutOfBoundsException(position.getDouble());
		}
		this.position = position;
	}
	
	/**
	 * Return the current position of this Object
	 * @return  The position of this Object
	 */
	public Vector3d getPosition(){
		return this.position;
	}
	
	/**
	 * Set a new velocity for this Object.
	 * @param   velocity
	 * 			The new velocity for this Object to have.
	 */
	protected void setVelocity(Vector3d velocity){
		this.velocity = velocity;
	}
	
	/**
	 * Return the current velocity of this Object
	 * @return  The velocity of this Object.
	 */
	public Vector3d getVelocity(){
		return this.velocity;
	}
	
	/**
	 * Checks if this Object has solid ground beneath it.
	 * @return	true if the cube below this Object is solid.
	 * 			| blockType(x,y,z-1) == Solid
	 */
	public boolean canStand(){
		Vector3d position = this.getPosition();
		position.setZ(position.getZ()-1);
		return World.isSolid(position);
	}
	/**
	 * Check if the specified object is falling
	 * @return	True if the objects falling state is true
	 * 			|this.falling == true
	 */
	protected boolean isFalling() {
		return this.falling;
	}
	/**
	 * Set the current state of this object to falling.
	 * @effect 	the current vertical velocity of this object will be substracted with 3m/s
	 * 			|this.velocity = (oldX,oldY,oldZ-3)
	 * @effect	The falling state will be true.
	 * 			|this.falling = true
	 */
	public void startFalling(){
		double[] newVel = {this.getVelocity().getX(),this.getVelocity().getY(),this.getVelocity().getZ()-3};
		
		Vector3d velocity = new Vector3d(newVel);
		this.setVelocity(velocity);
		this.falling = true;
	}
	
	/**
	 * Set the current state of this object to 'not falling'.
	 * @effect 	the current vertical velocity of this object will be added with 3m/s
	 * 			|this.velocity = (oldX,oldY,oldZ+3)
	 * @effect	The falling state will be false.
	 * 			|this.falling = false
	 */
	public void stopFalling(){
		double[] newVel = {this.getVelocity().getX(),this.getVelocity().getY(),this.getVelocity().getZ()+3};
		
		Vector3d velocity = new Vector3d(newVel);
		this.setVelocity(velocity);
		this.falling = false;
	}
	
	/**
	 * Update this objects location, caused by its velocity and a specified duration.
	 * @param   dt
	 * 			The period that this Object has moved.
	 */
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
