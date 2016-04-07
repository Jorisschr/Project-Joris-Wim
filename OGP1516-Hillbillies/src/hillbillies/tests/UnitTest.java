package hillbillies.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import helperclasses.NameException;
import helperclasses.OutOfBoundsException;
import helperclasses.Vector3d;
import hillbillies.model.Unit;

public class UnitTest {
	
	private Unit testUnit1;
	private Unit testUnit2;
	private Unit testUnit3;
	
	@Before
	public void initiateTestUnit() throws IllegalArgumentException, OutOfBoundsException{
		testUnit1 = new Unit("Phao",new Vector3d(15,15,15),50,50,50,50,false );
		testUnit2 = new Unit("Akela",new Vector3d(20,20,20),50,50,50,50,false );
		testUnit3 = new Unit("Baloe",new Vector3d(25,25,25),50,50,50,50,false );
	}
	
	
	
	///////////////////////////
	///	GENERAL POSITIONING ///
	///////////////////////////
	
	@Test
	public void testValidPosition() throws Exception{
		Vector3d position = new Vector3d(15.5,15.5,15.5);
		testUnit1.setPosition(position);
		
		assertEquals(position,testUnit1.getPosition());
	}
	
	@Test
	public void testValidCube() throws Exception{
		
		Vector3d position = new Vector3d(15.6,10.7,20.85);
		testUnit1.setPosition(position);
		
		assertTrue(Arrays.equals(new double[] {15.0,10.0,20.0},testUnit1.getPosition().getCube().getDoubleArray()));
	}
	@Test
	public void testInvalidPosition() throws Exception{
		Vector3d oldPos = testUnit1.getPosition();
		Vector3d position = new Vector3d(-15,15,-15);
		try {
			testUnit1.setPosition(position);
		} catch (OutOfBoundsException e) {
		}
		assertEquals(oldPos,testUnit1.getPosition());
	}
	
	@Test
	public void testAdjacentTo_bothCases() throws Exception{
		Vector3d pos1 = new Vector3d(15,15,15);
		testUnit1.setPosition(pos1);
		
		Vector3d pos2 = new Vector3d(15,15,16);
		testUnit2.setPosition(pos2);
		
		Vector3d pos3 = new Vector3d(15,15,17);
		testUnit3.setPosition(pos3);
		
		assertTrue(testUnit1.isAdjacentTo(testUnit2));
		assertFalse(testUnit1.isAdjacentTo(testUnit3));
	}
	
	@Test
	public void testOrientation() throws Exception{
		testUnit1.setOrientation(0);
		assertTrue(testUnit1.getOrientation() == 0);
	}
	/////////////////
	///	UNIT NAME ///
	/////////////////
	@Test
	public void testValidName() throws Exception{
		assertEquals("Phao",testUnit1.getName());
	}
	
	@Test
	public void testInvalidName() throws Exception{
		
		String wrongName = new String("this Is A False Name: 123");
		try {
		testUnit1.setName(wrongName);
		} catch (NameException e) {
		}
		
		assertFalse(testUnit1.canHaveAsName(wrongName));
		assertFalse(testUnit1.getName()==wrongName);
	}
	
	
	///////////////////////
	///	UNIT ATTRIBUTES ///
	///////////////////////
	@Test
	public void testValidAttributes() throws Exception{
		testUnit1.setAgility(50);
		testUnit1.setStrength(51);
		testUnit1.setToughness(52);
		testUnit1.setWeight(53);
		
		assertEquals(50,testUnit1.getAgility());
		assertEquals(51,testUnit1.getStrength());
		assertEquals(52,testUnit1.getToughness());
		assertEquals(53,testUnit1.getWeight());
	}
	@Test
	public void testInvalidAttributes() throws Exception{
		int oldAgi = testUnit1.getAgility();
		int oldStr = testUnit1.getStrength();
		int oldTgh = testUnit1.getToughness();
		
		//Negative Number
		testUnit1.setAgility(-1);
		//Too high number
		testUnit1.setStrength(1000);
		//Too low number
		testUnit1.setToughness(0);
		//Not following the Weight rule
		double minWgt = (oldAgi + oldStr)/2;
		testUnit1.setWeight(10);
		
		assertEquals(oldAgi,testUnit1.getAgility());
		assertEquals(oldStr,testUnit1.getStrength());
		assertEquals(oldTgh,testUnit1.getToughness());
		assertTrue(minWgt==testUnit1.getWeight());
	}
	
	
	/////////////////////////////
	///	HITPOINTS AND STAMINA ///
	/////////////////////////////
	
	@Test
	public void testMaxHitpionts() throws Exception{
		assertEquals(50,testUnit1.getMaxHitpoints());
	}
	
	///////////////////////////
	/// VELOCITY AND MOVING ///
	///////////////////////////
	
	@Test
	public void testSetSpeed() throws Exception{
		//Horizontal Movement
		testUnit1.setSpeed(0);
		assertTrue(1.5==testUnit1.getCurrentSpeed());
		
		//Downward Movement
		testUnit1.setSpeed(-1);
		assertTrue(1.2*1.5==testUnit1.getCurrentSpeed());
		
		//Upward Movement
		testUnit1.setSpeed(1);
		assertTrue(0.5*1.5==testUnit1.getCurrentSpeed());
	}
	
	@Test
	public void	testGetDestination() throws Exception{
		testUnit1.setDestination(new Vector3d(10,10,10));
		
		assertTrue(Arrays.equals(new double[] {10,10,10}, testUnit1.getDestination().getDoubleArray()));
	}
	
	@Test
	public void testGetNextPosition() throws Exception{
		Vector3d position = new Vector3d(10,10,10);
		testUnit1.setNextPosition(position);
		assertEquals(position,testUnit1.getNextPosition());
	}
	
	@Test
	public void testGetMovingTime() throws Exception{
		testUnit1.setMovingTime(1);
		assertTrue(1==testUnit1.getMovingTime());
	}
	
	@Test
	public void testGetSprintingTime() throws Exception{
		testUnit1.setSprintingTime(1);
		assertTrue(1==testUnit1.getSprintingTime());
	}
	
	@Test
	public void testUpdateSprinting() throws Exception{
		//preparation
		testUnit1.setSprintingTime(0.31);
		testUnit1.setStamina(testUnit1.getMaxHitpoints());
		
		//testing
		testUnit1.updateSprinting(0.2);
		assertTrue(Math.abs(testUnit1.getSprintingTime()-0.01) <= 1E-7);
		assertTrue(testUnit1.getStamina()==testUnit1.getMaxHitpoints()-5);
	}
	
	@Test
	public void testCalcVelocity() throws Exception{
		//Downwards movement
		Vector3d adjCube = new Vector3d(1,0,-1);
		testUnit1.setSpeed((int) adjCube.getZ());
		double[] expectedVel = adjCube.multiply(testUnit1.getCurrentSpeed()/Math.sqrt(2)).getDoubleArray();
		double[] velocity = testUnit1.calcVelocity(adjCube).getDoubleArray();
		
		assertTrue(Arrays.equals(velocity, expectedVel));
	}
	
	
	@Test
	public void testValidUpdatePosition() throws Exception{
		testUnit1.setVelocity(new Vector3d(1,1,1));
		
		testUnit1.updatePosition(0.5);
		double[] expectedPos = new double[] {15.5,15.5,15.5};
		
		assertTrue(Arrays.equals(testUnit1.getPosition().getDoubleArray(), expectedPos));
	}
	
	@Test
	public void testValidMoveToLocation() throws Exception {
		testUnit1.moveTo(new Vector3d(20,15,15));
		assertTrue(Arrays.equals(testUnit1.getDestination().getDoubleArray(), new double[] {20,15,15}));
		assertTrue(Arrays.equals(testUnit1.getNextPosition().getDoubleArray(),new double[] {16.5,15.5,15.5}));
	}
	
	@Test
	public void testDestinationReached_bothCases() throws Exception{
		//True
		testUnit1.setDestination(testUnit1.getPosition().add(-0.5));
		assertTrue(testUnit1.destinationReached());
		
		//False
		testUnit1.setDestination(testUnit1.getPosition().add(10));
		assertFalse(testUnit1.destinationReached());
	}
	
	@Test
	public void testNextPosReached_bothCases() throws Exception{
		//True
		testUnit1.setNextPosition(testUnit1.getPosition());
		assertTrue(testUnit1.nextPositionReached());
		
		//False
		testUnit1.setNextPosition(testUnit1.getPosition().add(10));
		assertFalse(testUnit1.nextPositionReached());
	}
	
	////////////////////////////////////
	/// STATUS AND ACTIVITY PROGRESS ///
	////////////////////////////////////
	@Test
	public void testStartDefaultBehaviour_allCases() throws Exception{
		
		//Idle and Default Behaviour is on:
		testUnit1.setStatus("Idle");
		testUnit1.setDefaultBehaviorEnabled(true);
		testUnit1.startDefaultBehavior();
		
		assertTrue(testUnit1.isMoving() || testUnit1.isWorking() || testUnit1.isInitResting());
		
		//Idle and Default Behaviour is off:
		testUnit1.setStatus("Idle");
		testUnit1.setDefaultBehaviorEnabled(false);
		testUnit1.startDefaultBehavior();
		
		assertTrue(testUnit1.isIdle());
		
		//Not Idle:
		testUnit1.setStatus("Moving");
		testUnit1.startDefaultBehavior();
		
		assertTrue(testUnit1.isMoving());
	}
	
	////////////////
	/// FIGHTING ///
	////////////////
	@Test
	public void testAttackAdjacentUnit() throws Exception{
		//Initiation
		testUnit1.setPosition(new Vector3d(15.5,15.5,15.5));
		testUnit2.setPosition(new Vector3d(16.5,15.5,15.5));
		testUnit1.attack(testUnit2);
		
		//Checks
		assertTrue(testUnit1.getOrientation()==0 && testUnit2.getOrientation()-Math.PI<=1E-7);
		assertTrue(testUnit1.isAttacking() && testUnit2.isDefending());
		assertTrue(testUnit1.getOpponent()==testUnit2 && testUnit2.getOpponent()==testUnit1);
	}
	
	@Test
	public void testAttackNonAdjUnit() throws Exception{
		//Initiation
		testUnit1.setPosition(new Vector3d(15.5,15.5,15.5));
		testUnit2.setPosition(new Vector3d(17.5,15.5,15.5));
		
		double oldOr1 = testUnit1.getOrientation();
		double oldOr2 = testUnit2.getOrientation();
		testUnit1.attack(testUnit2);
		
		//Checks
		assertTrue(testUnit1.getOrientation()==oldOr1 && testUnit2.getOrientation()==oldOr2);
		assertTrue(!testUnit1.isAttacking() && !testUnit2.isDefending());
	}
	
	@Test
	public void testAttackItself() throws Exception{
		//Initiation
		double oldOr = testUnit1.getOrientation();
		testUnit1.attack(testUnit1);
		
		assertTrue(testUnit1.getOrientation()==oldOr);
		assertTrue(!testUnit1.isAttacking() && !testUnit1.isDefending());
	}
	
	///////////////
	/// RESTING ///
	///////////////
	@Test
	public void testRestNotInteruptable() throws Exception{
		//Initiation
		testUnit1.setStatus("Attacking");
		testUnit1.rest();
		
		//Check
		assertTrue(testUnit1.isAttacking());
	}
	
	@Test
	public void testRestNotFull() throws Exception{
		//Initiation
		testUnit1.setHitpoints(testUnit1.getHitpoints()-10);
		testUnit1.rest();
		assertTrue(testUnit1.isInitResting());
		for(int i = 0; i <=20; i++)
			testUnit1.advanceTime(0.1);
		
		//Check
		assertTrue(testUnit1.isResting());
	}
	
	@Test
	public void testRestFull() throws Exception {
		//Initiation
		testUnit1.rest();
		assertTrue(testUnit1.isInitResting());
		for(int i = 0; i <=20; i++)
			testUnit1.advanceTime(0.1);

		//Check
		assertTrue(testUnit1.isIdle() || testUnit1.isMoving());
	}
	
	@Test
	public void testRestoreHitpoints() throws Exception{
		//Initiation
		testUnit1.setHitpoints(testUnit1.getHitpoints()-10);
		testUnit1.restore();
		//Check
		assertTrue(testUnit1.getHitpoints()==41);
	}
	
	@Test
	public void testRestoreStamina() throws Exception{
		//Initiation
		testUnit1.setStamina(testUnit1.getStamina()-10);
		testUnit1.restore();
		//Check
		assertTrue(testUnit1.getStamina()==42);
	}
	
	///////////////
	/// WORKING ///
	///////////////
	
	////////////////////
	/// ADVANCE TIME ///
	////////////////////
	@Test
	public void testAdvanceTimeInvalid() throws Exception{
		boolean error = false;
		try {
			testUnit1.advanceTime(0.5);
		} catch (IllegalArgumentException e){
			error = true;
		}
		
		assertTrue(error);
	}
	
	@Test
	public void testAdvanceTimeValid() throws Exception{
		boolean error = false;
		try {
			testUnit1.advanceTime(0.15);
		} catch (IllegalArgumentException e){
			error = true;
		}
		
		assertFalse(error);
	}
	
	@Test
	public void testIsValidDuration() throws Exception{
		//True
		assertTrue(Unit.isValidDuration(0.19));
		//False
		assertFalse(Unit.isValidDuration(-0.1));
		assertFalse(Unit.isValidDuration(0.5));
	}
	
	///////////////////////////////
	/// EXPERIENCE AND LEVELING ///
	///////////////////////////////
	@Test
	public void testExperienceNoLvlUp() throws Exception{
		testUnit1.setXP(5);
		assertTrue(testUnit1.getExperience()==5);
	}
	
	@Test
	public void testExperienceLvlUp() throws Exception{
		testUnit1.setXP(35);
		assertTrue(testUnit1.getExperience()==5);
		
		int totalAttributes = testUnit1.getAgility()+testUnit1.getStrength()+testUnit1.getToughness();
		assertTrue(totalAttributes == 153);
	}
	
	////////////////////
	/// UNIT FACTION ///
	////////////////////
	
	//@Test
	//public void testFaction() throws Exception{
	//	Faction testFaction = new Faction();
	//	testUnit1.setFaction(testFaction);
	//	
	//	assertTrue(testFaction == testUnit1.getFaction());
	//}
}
