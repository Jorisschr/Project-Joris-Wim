package hillbillies.model;

import java.util.Collections;
import java.util.Set;

import helperclasses.OutOfBoundsException;

public class Faction {
	public Faction(){
		this.unitSet = Collections.emptySet();
	}
	
	public static final int MAX_MEMBERS = 50;
	private Set<Unit> unitSet;
	
	public Set<Unit> getUnits() {
		return this.unitSet;
	}
	
	public void addUnit(Unit unit) {
		
			this.unitSet.add(unit);
			unit.setFaction(this);

	}
	
	public int getNbUnits() {
		return this.unitSet.size();
	}
	
	/**
	 * Check whether the given faction has reached its member limit.
	 * @return	True if and only if this faction currently has MAX_MEMBERS units
	 */
	public boolean isFactionFull() {
		return this.getNbUnits() == MAX_MEMBERS;
	}
}
