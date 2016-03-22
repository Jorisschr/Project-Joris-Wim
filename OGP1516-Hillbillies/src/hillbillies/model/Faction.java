package hillbillies.model;

import java.util.Collections;
import java.util.Set;

public class Faction {
	public Faction(){
		this.unitSet = Collections.emptySet();
	}
	
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
}
