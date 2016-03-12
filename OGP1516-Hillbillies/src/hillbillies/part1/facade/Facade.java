package hillbillies.part1.facade;
import ogp.framework.util.ModelException;
import helperclasses.OutOfBoundsException;
import hillbillies.model.Unit;


public class Facade implements IFacade {
	public Facade(){		
	}
	
	public Unit createUnit(String name, int[] initialPosition, int weight, int agility, int strength, int toughness,
			boolean enableDefaultBehavior) throws ModelException {
		try {
			return new Unit(name, initialPosition, weight, agility, strength, toughness, enableDefaultBehavior);
		} catch (IllegalArgumentException e) {
			throw new ModelException();
		} catch (OutOfBoundsException e) {
			throw new ModelException();
		}
	}
	
	public double[] getPosition(Unit unit) throws ModelException {
		return unit.getPosition();
	}
	
	public int[] getCubeCoordinate(Unit unit) throws ModelException {
		return unit.getCube();
	}
	
	public String getName(Unit unit) throws ModelException {
		return unit.getName();
	}
	
	public void setName(Unit unit, String newName) throws ModelException {
		unit.setName(newName);
	}
	
	public int getWeight(Unit unit) throws ModelException {
		return unit.getWeight();
	}
	
	public void setWeight(Unit unit, int newValue) throws ModelException {
		unit.setWeight(newValue);
	}
	
	public int getStrength(Unit unit) throws ModelException {
		return unit.getStrength();
	}
	
	public void setStrength(Unit unit, int newValue) throws ModelException {
		unit.setStrength(newValue);
	}
	
	public int getAgility(Unit unit) throws ModelException {
		return unit.getAgility();
	}
	
	public void setAgility(Unit unit, int newValue) throws ModelException {
		unit.setAgility(newValue);
	}
	
	public int getToughness(Unit unit) throws ModelException {
		return unit.getToughness();
	}
	
	public void setToughness(Unit unit, int newValue) throws ModelException {
		unit.setToughness(newValue);
	}
	
	public int getMaxHitPoints(Unit unit) throws ModelException {
		return unit.getMaxHitpoints();
	}
	
	public int getCurrentHitPoints(Unit unit) throws ModelException {
		return unit.getHitpoints();
	}
	
	public int getMaxStaminaPoints(Unit unit) throws ModelException {
		return unit.getMaxHitpoints();
	}
	
	public int getCurrentStaminaPoints(Unit unit) throws ModelException {
		return unit.getStamina();
	}
	
	public void advanceTime(Unit unit, double dt) throws ModelException {

				try {
					unit.advanceTime(dt);
				} catch (IllegalArgumentException | InterruptedException e) {
					throw new ModelException();
				}					
	}
	
	public void moveToAdjacent(Unit unit, int dx, int dy, int dz) throws ModelException {
	//	unit.moveToAdjacent(new double[] {dx, dy, dz});
	}
	
	public double getCurrentSpeed(Unit unit) throws ModelException {
		return unit.getCurrentSpeed();
	}
	
	public boolean isMoving(Unit unit) throws ModelException {
		return unit.isMoving();
	}
	
	public void startSprinting(Unit unit) throws ModelException {
		unit.startSprinting();
	}
	
	public void stopSprinting(Unit unit) throws ModelException {
		unit.stopSprinting();
	}
	
	public boolean isSprinting(Unit unit) throws ModelException {
		return unit.isSprinting();
	}
	
	public double getOrientation(Unit unit) throws ModelException {
		return (double) unit.getOrientation();
	}
	
	public void moveTo(Unit unit, int[] cube) throws ModelException {
		unit.moveTo(cube);
	}
	
	public void work(Unit unit) throws ModelException {
		unit.work();
	}
	
	public boolean isWorking(Unit unit) throws ModelException {
		return unit.isWorking();
	}
	
	public void fight(Unit attacker, Unit defender) throws ModelException {
		attacker.attack(defender);
	}
	
	public boolean isAttacking(Unit unit) throws ModelException {
		return unit.isFighting();
	}
	
	public void rest(Unit unit) throws ModelException {
		unit.rest();
	}
	
	public boolean isResting(Unit unit) throws ModelException {
		return unit.isResting();
	}
	
	public void setDefaultBehaviorEnabled(Unit unit, boolean value) throws ModelException {
		unit.setDefaultBehaviorEnabled(value);
	}
	
	public boolean isDefaultBehaviorEnabled(Unit unit) throws ModelException {
		return unit.isDefaultBehaviorEnabled();
	}

}


