package hillbillies.model;

import java.util.HashSet;
import java.util.Set;

public class Faction {
	public Faction(){
		this.unitSet = new HashSet<Unit>();
	}
	
	public static final int MAX_MEMBERS = 50;
	private Set<Unit> unitSet;
	
	public Set<Unit> getUnits() {
		return this.unitSet;
	}
	
	public void addUnit(Unit unit) {
		unit.setFaction(this);
		this.unitSet.add(unit);
	}
	
	public void removeUnit(Unit unit){
		try { 
			this.unitSet.remove(unit);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		unit.setFaction(null);
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
