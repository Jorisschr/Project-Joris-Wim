package helperclasses;

import java.util.Random;

import hillbillies.model.World;

public class Vector3d {
	
	/**
	 * Initialize a new three dimensional vector with the given dimensions.
	 * 
	 * @param 	vector
	 * 			An array of doubles of length 3. 
	 * 			Containing, in order, the x, y and z dimensions of the new vector.
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
	 * @param 	vector
	 * 			An array of integers of length 3.
	 * 			Containing, in order, the x, y and z dimensions of the new vector.
	 */
	public Vector3d(int[] vector) {
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
	 * Initialize a new three dimensional vector with the given dimensions.
	 * 
	 * @param 	x
	 * 			The x-coordinate for this new vector.
	 * @param 	y
	 * 			The y-coordinate for this new vector.
	 * @param 	z
	 * 			The z-coordinate for this new vector.
	 */
	public Vector3d(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Initialize a new three dimensional vector with 0 on all the coordinates
	 */
	public Vector3d(){
		this.x = 0;
		this.y = 0;
		this.y = 0;
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
		double[] vector = this.getDoubleArray();
		return vector[dimension];
	}

	/**
	 * Return a double array with the coordinates of this vector in the right order: {X, Y, Z}
	 * 
	 * @return  An array of three doubles with the coordinates of this vector.
	 */
	public double[] getDoubleArray() {
		return new double[] {this.getX(),this.getY(),this.getZ()};
	}
	
	public void setDimension(int i, double value) {
		switch(i){
		case 0: this.x = value;
		case 1: this.y = value;
		case 2: this.z = value;
		}
	}
	
	/**
	 * Return an int array with the coordinates of this vector rounded down and
	 * in their general order {X, Y, Z}
	 * 
	 * @return An array of three integers with the coordinates of this vector rounded down.
	 */
	public int[] getIntArray() {
		return new int[] {(int) this.getX(), (int) this.getY(), (int) this.getZ()};
	}
	
	/**
	 * Return a vector containing the floor of every coordinate of this vector. 
	 * @ return  A new Vector3d with three doubles as it's coordinates, which are the floor of the old Vector3d
	 */
	public Vector3d getCube(){
		Vector3d cube = new Vector3d(Math.floor(this.getX()), Math.floor(this.getY()), Math.floor(this.getZ()));
		return cube;
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
	 * Return a new vector with as coordinates the sum of this and the other vector.
	 * 
	 * @param 	other
	 * 			The vector to add.
	 */
	public Vector3d add(Vector3d other) {
		return new Vector3d(this.getX() + other.getX(), 
							this.getY() + other.getY(), 
							this.getZ() + other.getZ());
	}
	
	/**
	 * Add 'a' to every coordinate of this vector.
	 * 
	 * @param 	a
	 * 			The number to add to every coordinate.
	 */
	public Vector3d add(double a) {
		return new Vector3d(this.getX() + a,
							this.getY() + a,
							this.getZ() + a);
	}


	/**
	 * Return a new vector with as coordinates the subtraction of 
	 * the corresponding coordinates of this vector and the other.
	 * 
	 * @param 	other
	 * 			The vector on the right hand side of the subtraction.
	 */
	public Vector3d subtract(Vector3d other) {
		return new Vector3d(this.getX() - other.getX(),
							this.getY() - other.getY(),
							this.getZ() - other.getZ());
	}

	/**
	 * Multiply all coordinates of this vector by the given double precision number a.
	 * 
	 * @param 	a
	 * 			The double with which every coordinate of this vector will be multiplied.
	 */
	public Vector3d multiply(double a) {
		return new Vector3d(this.getX() * a, this.getY() * a, this.getZ() * a);
	}

	/**
	 * Divide all coordinates of this vector by the given double precision number a.
	 * 
	 * @param 	a
	 * 			The double by which every coordinate of this vector will be divided.
	 */
	public Vector3d divide(double a) {
		if (a != 0) {
			return new Vector3d(this.getX() / a, this.getY() / a, this.getZ() / a);
		}
		return this;
	}

	/**
	 * Return the scalar product of this vector and the given other vector.
	 * 
	 * @param 	other
	 * 			The other vector needed to calculate the scalar product.
	 * @return
	 */
	public double scalarProduct(Vector3d other) {
		return (this.getX() * other.getX() +
				this.getY() * other.getY() +
				this.getZ() * other.getZ());
	}

	/**
	 * Return the cross product of this vector with the given other vector.
	 * 
	 * @param 	other
	 * 			The other vector on the right hand side of the cross product.
	 */
	public Vector3d crossProduct(Vector3d other) {
		return new Vector3d((this.getY() * other.getZ()) - (this.getZ() * other.getY()),
							(this.getZ() * other.getX()) - (this.getX() * other.getZ()),
							(this.getX() * other.getY()) - (this.getY() * other.getX()));
	}

	/**
	 * Check whether this vector is equal to the given other vector.
	 * 
	 * @param 	other
	 * 			The vector with which this vector will be compared.
	 * @return	True if and only if all corresponding coordinates of the given vectors are equal.
	 */
	public boolean equals(Vector3d other) {
		return ((this.getX() == other.getX()) && 
				(this.getY() == other.getY()) && 
				(this.getZ() == other.getZ()));
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
			return this.divide(norm);
		}
		return this;
	}
	
	public boolean isValidPosition(double[] position) {
		for (int i = 0; i < position.length; i++) {
			if ((position[i] < 0.5) || (position[i] > 49.5)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isValidPosition() {
		
		for (int i = 0; i <= 2; i++) {
			double coordinate = this.getDimension(i);
			if ((coordinate < 0.5) || (coordinate > 49.5)) {
				return false;
			}
		}
		return true;
	}	
	
	public Vector3d genRndVector3d(int upperX, int upperY, int upperZ) {
		Random r = new Random();
		return new Vector3d(r.nextInt(upperX),r.nextInt(upperY),r.nextInt(upperZ));
	}
}
