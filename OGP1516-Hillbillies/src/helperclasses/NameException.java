package helperclasses;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A class for signaling non valid names.
 * 
 * @author Joris Schrauwen, Wim Schmitz
 *
 */
public class NameException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Variable registering the name involved in this NameException.
	 */
	private String name;
	
	/**
	 * Initialize this new out of bounds exception with given position.
	 * @post 	The position of this new out of bounds exception
	 * 			is equal to the given value.
	 * 			| new.getPosition() == position
	 */
	public NameException(String name) {
		this.name = name;
	}
	
	/**
	 * Return the position registered for this out of bounds exception. 
	 */
	@Basic @Immutable
	public String getName(){
		return this.name;
	}
}
