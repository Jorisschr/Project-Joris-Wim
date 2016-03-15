package helperclasses;

import hillbillies.model.World;

public class Vector3d {
	
	/**
	 * Initialize a new three dimensional vector with the given dimensions.
	 * 
	 * @param 	vector
	 * 			An array of doubles of length 3. 
	 * 			Containing, in order, the x, y and z dimensions.
	 */
	public Vector3d(double[] vector) {
		if (vector.length == 3) {		
			this.x = vector[0];
			this.y = vector[1];
			this.z = vector[2];
		}
	}
	
	/**
	 * Initialize a new three dimensional vector with the given dimensions.
	 * 
	 * @param 	x
	 * 			The x-coordinate for this new vector.
	 * @param 	y
	 * 			The y-coordinate for this new vector.
	 * @param 	z
	 * 			The z-coordinate for this new vector.
	 */
	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Variable registering the x-coordinate of this vector.
	 */
	private double x;
	
	/**
	 * Variable registering the y-coordinate of this vector.
	 */
	private double y;
	
	/**
	 * Variable registering the z-coordinate of this vector.
	 */
	private double z;
	
	/**
	 * Return the x coordinate of this vector.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Return the y coordinate of this vector.
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Return the z coordinate of this vector.
	 */
	public double getZ() {
		return this.z;
	}
	
	/**
	 * Return the specified coordinate of this vector
	 * @param   dimension
	 * 			the axis of which the coordinate must be given. 0 is X, 1 is Y, and 2 is Z.
	 * @return	X-coordinate of this vector
	 * 			| dimension = 0
	 * @return	Y-coordinate of this vector
	 * 			| dimension = 1
	 * @return	Z-coordinate of this vector
	 * 			| dimension = 2
	 */
	public double getDimension(int dimension){
		double[] vector = this.getDouble();
		return vector[dimension];
	}
	
	/**
	 * Return a double array with the coordinates of this vector in the right order: {X,Y,Z}
	 * @return  An array of three doubles with the coordinates of this vector.
	 */
	public double[] getDouble(){
		double[] vector = {this.getX(),this.getY(),this.getZ()};
		return vector;
	}
	
	/**
	 * Set the x coordinate of this vector to the given value.
	 * 
	 * @param 	x
	 * 			The new x coordinate of this vector.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Set the y coordinate of this vector to the given value.
	 * 
	 * @param 	y
	 * 			The new y coordinate of this vector.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Set the z coordinate of this vector to the given value.
	 * 
	 * @param 	z
	 * 			The new z coordinate of this vector.
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Check whether this vector is a null vector.
	 */
	public boolean isNullVector() {
		if ((this.getX() == 0) && (this.getY() == 0) && (this.getZ() ==0)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Calculate the norm of this vector.
	 */
	public double calcNorm() {
		return Math.sqrt(Math.pow(this.getX(), 2) +
								Math.pow(this.getY(), 2) +
								Math.pow(this.getZ(), 2));
	}
	
	/**
	 * Normalize this vector.
	 */
	public Vector3d normalize() {
		if (!this.isNullVector()) {
			double norm = this.calcNorm();
			return new Vector3d(this.getX()/norm, this.getY()/norm, this.getZ()/norm);
		}
		return this;
	}
	
	public boolean isValidPosition(double[] position) {
		for (int i = 0; i < position.length; i++) {
			if ((position[i] < World.getLowerBound()) || (position[i] > World.getUpperBound())) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isValidPosition() {
		
		for (int i = 0; i <= 2; i++) {
			double coordinate = this.getDimension(i);
			if ((coordinate < World.getLowerBound()) || (coordinate> World.getUpperBound())) {
				return false;
			}
		}
		return true;
	}

}
