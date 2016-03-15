package hillbillies.model;

import helperclasses.OutOfBoundsException;
import helperclasses.Vector3d;

public class Boulder {
	/**
	 * Initialize a new boulder on a given location with a random weight 
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
	}
	
	private Vector3d position;
}
