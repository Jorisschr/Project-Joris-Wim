package helperclasses;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A class for signaling positions that are out of bounds for units.
 * 
 * @author Joris Schrauwen, Wim Schmitz
 *
 */
public class OutOfBoundsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Initialize this new out of bounds exception with given position.
	 * @post 	The position of this new out of bounds exception
	 * 			is equal to the given value.
	 * 			| new.getPosition() == position
	 */
	public OutOfBoundsException(double[] position) {
		this.position = position;
	}
	
	/**
	 * Return the position registered for this out of bounds exception. 
	 */
	@Basic @Immutable
	public double[] getPosition(){
		return this.position;
	}
	/**
	 * Variable registering the position involved in this 
	 * out of bounds exception.
	 */
	private final double[] position;
	
	
	

}
