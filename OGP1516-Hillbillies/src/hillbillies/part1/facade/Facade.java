package hillbillies.part1.facade;
import ogp.framework.util.ModelException;
import helperclasses.OutOfBoundsException;
import helperclasses.Vector3d;
import hillbillies.model.Unit;


public class Facade implements IFacade {
	public Facade(){		
	}

	@Override
	public Unit createUnit(String name, int[] initialPosition, int weight, int agility, int strength, int toughness,
			boolean enableDefaultBehavior) throws ModelException {
		Vector3d initialpos = new Vector3d(initialPosition);
		try {
			return new Unit(name, initialpos, weight, agility, strength, toughness, enableDefaultBehavior);
		} catch (IllegalArgumentException e) {
			throw new ModelException();
		} catch (OutOfBoundsException e) {
			throw new ModelException();
		}
	}
	
	@Override
	public double[] getPosition(Unit unit) throws ModelException {
		return unit.getPosition().getDoubleArray();
	}
	
	@Override
	public int[] getCubeCoordinate(Unit unit) throws ModelException {
		return unit.getPosition().getCube().getIntArray();
	}
	
	@Override
	public String getName(Unit unit) throws ModelException {
		return unit.getName();
	}
	
	@Override
	public void setName(Unit unit, String newName) throws ModelException {
		unit.setName(newName);
	}
	
	@Override
	public int getWeight(Unit unit) throws ModelException {
		return unit.getWeight();
	}
	
	@Override
	public void setWeight(Unit unit, int newValue) throws ModelException {
		unit.setWeight(newValue);
	}
	
	@Override
	public int getStrength(Unit unit) throws ModelException {
		return unit.getStrength();
	}
	
	@Override
	public void setStrength(Unit unit, int newValue) throws ModelException {
		unit.setStrength(newValue);
	}
	
	@Override
	public int getAgility(Unit unit) throws ModelException {
		return unit.getAgility();
	}
	
	@Override
	public void setAgility(Unit unit, int newValue) throws ModelException {
		unit.setAgility(newValue);
	}
	
	@Override
	public int getToughness(Unit unit) throws ModelException {
		return unit.getToughness();
	}
	
	@Override
	public void setToughness(Unit unit, int newValue) throws ModelException {
		unit.setToughness(newValue);
	}
	
	@Override
	public int getMaxHitPoints(Unit unit) throws ModelException {
		return unit.getMaxHitpoints();
	}
	
	@Override
	public int getCurrentHitPoints(Unit unit) throws ModelException {
		return unit.getHitpoints();
	}
	
	@Override
	public int getMaxStaminaPoints(Unit unit) throws ModelException {
		return unit.getMaxHitpoints();
	}
	
	@Override
	public int getCurrentStaminaPoints(Unit unit) throws ModelException {
		return unit.getStamina();
	}
	
	@Override
	public void advanceTime(Unit unit, double dt) throws ModelException {

				try {
					unit.advanceTime(dt);
				} catch (IllegalArgumentException | InterruptedException e) {
					throw new ModelException();
				}					
	}
	
	@Override
	public void moveToAdjacent(Unit unit, int dx, int dy, int dz) throws ModelException {
		Vector3d vector = new Vector3d(dx, dy, dz);
		unit.moveToAdjacent(vector);
	}
	
	@Override
	public double getCurrentSpeed(Unit unit) throws ModelException {
		return unit.getCurrentSpeed();
	}
	
	@Override
	public boolean isMoving(Unit unit) throws ModelException {
		return unit.isMoving();
	}
	
	@Override
	public void startSprinting(Unit unit) throws ModelException {
		unit.startSprinting();
	}
	
	@Override
	public void stopSprinting(Unit unit) throws ModelException {
		unit.stopSprinting();
	}
	
	@Override
	public boolean isSprinting(Unit unit) throws ModelException {
		return unit.isSprinting();
	}
	
	@Override
	public double getOrientation(Unit unit) throws ModelException {
		return (double) unit.getOrientation();
	}
	
	@Override
	public void moveTo(Unit unit, int[] cube) throws ModelException {
		unit.moveTo(new Vector3d(cube));
	}
	
	@Override
	public void work(Unit unit) throws ModelException {
		unit.work();
	}
	
	@Override
	public boolean isWorking(Unit unit) throws ModelException {
		return unit.isWorking();
	}
	
	@Override
	public void fight(Unit attacker, Unit defender) throws ModelException {
		attacker.attack(defender);
	}
	
	@Override
	public boolean isAttacking(Unit unit) throws ModelException {
		return unit.isAttacking();
	}
	
	@Override
	public void rest(Unit unit) throws ModelException {
		unit.rest();
	}
	
	@Override
	public boolean isResting(Unit unit) throws ModelException {
		return unit.isResting();
	}
	
	@Override
	public void setDefaultBehaviorEnabled(Unit unit, boolean value) throws ModelException {
		unit.setDefaultBehaviorEnabled(value);
	}
	
	@Override
	public boolean isDefaultBehaviorEnabled(Unit unit) throws ModelException {
		return unit.isDefaultBehaviorEnabled();
	}
}


