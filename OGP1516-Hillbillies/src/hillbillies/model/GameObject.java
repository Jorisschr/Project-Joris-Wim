package hillbillies.model;

import java.util.Random;

import helperclasses.OutOfBoundsException;
import helperclasses.Vector3d;

public abstract class GameObject {
	
	public GameObject(Vector3d position) throws OutOfBoundsException{
		Vector3d middle = new Vector3d(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5 );
		
		if (!position.isValidPosition()){
			throw new OutOfBoundsException(position.getDoubleArray());
		}
		Random r = new Random();
		int weight = (int) (10 + 40 * r.nextDouble());
		this.weight = weight;
		this.position = middle;
		this.velocity = new Vector3d(0,0,0);
		this.falling = false;
		this.world = null;
	}
	
	private int weight;
	private Vector3d position;
	private Vector3d velocity;
	private boolean falling;
	private World world;
	
	/**
	 * Return the weight of this game object.
	 * @return this.weight
	 */
	public int getWeight(){
		return this.weight;
	}
	
	/**
	 * Set a new position for this game .
	 * @param   position
	 * 			The new position for this  to be.
	 * @throws  OutOfBoundsException
	 * 			The new position is not within the boundaries of the game World.
	 * 			|!isValidPosition(position)
	 */
	public void setPosition(Vector3d position) throws OutOfBoundsException{
		if(!position.isValidPosition()){
			throw new OutOfBoundsException(position.getDoubleArray());
		}
		this.position = position;
	}
	
	/**
	 * Return the current position of this 
	 * @return  The position of this 
	 */
	public Vector3d getPosition(){
		return this.position;
	}
	
	/**
	 * Set a new velocity for this .
	 * @param   velocity
	 * 			The new velocity for this  to have.
	 */
	public void setVelocity(Vector3d velocity){
		this.velocity = velocity;
	}
	
	/**
	 * Return the current velocity of this 
	 * @return  The velocity of this .
	 */
	public Vector3d getVelocity(){
		return this.velocity;
	}
	
	public abstract int getType();
	public abstract void setCarrier(Unit unit);
	public abstract Unit getCarrier();
	public abstract void setWorld(World world);
	public abstract World getWorld();

	
	/**
	 * Checks if this  has solid ground beneath it.
	 * @return	true if the cube below this game object is solid.
	 * 			| blockType(x,y,z-1) == Solid
	 */
	public boolean canStand(){
		Vector3d position = this.getPosition();
		position.setZ(position.getZ()-1);
		return this.getWorld().isSolid(position);
	}
	
	/**
	 * Check if the specified game object is falling
	 * @return	True if the game objects falling state is true.
	 * 			| this.falling == true
	 */
	protected boolean isFalling() {
		return this.falling;
	}
	
	/**
	 * Set the current state of this game object to falling.
	 * @effect 	The current vertical velocity of this game object will be subtracted with 3m/s
	 * 			| this.velocity = (oldX,oldY,oldZ-3)
	 * @effect	The falling state will be true.
	 * 			| this.falling = true
	 */
	public void startFalling(){
		double[] newVel = {this.getVelocity().getX(),this.getVelocity().getY(),this.getVelocity().getZ()-3};
		
		Vector3d velocity = new Vector3d(newVel);
		this.setVelocity(velocity);
		this.falling = true;
	}
	
	/**
	 * Set the current state of this game object to 'not falling'.
	 * @effect 	the current vertical velocity of this game object will be added with 3m/s.
	 * 			| this.velocity = (oldX,oldY,oldZ+3)
	 * @effect	The falling state will be false.
	 * 			| this.falling = false
	 */
	public void stopFalling(){
		double[] newVel = {this.getVelocity().getX(),this.getVelocity().getY(),this.getVelocity().getZ()+3};
		
		Vector3d velocity = new Vector3d(newVel);
		this.setVelocity(velocity);
		this.falling = false;
	}
	
	/**
	 * Update this game objects location, caused by its velocity and a specified duration.
	 * @param   dt
	 * 			The period that this  has moved.
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
