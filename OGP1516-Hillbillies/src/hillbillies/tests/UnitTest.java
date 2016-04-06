package hillbillies.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import helperclasses.NameException;
import helperclasses.OutOfBoundsException;
import helperclasses.Vector3d;
import hillbillies.model.Unit;

public class UnitTest {
	
	private static Unit testUnit1;
	private static Unit testUnit2;
	private static Unit testUnit3;
	
	@BeforeClass
	public static void initiateTestUnit() throws IllegalArgumentException, OutOfBoundsException{
		testUnit1 = new Unit("Phao",new Vector3d(15,15,15),50,50,50,50,false );
		testUnit2 = new Unit("Akela",new Vector3d(20,20,20),50,50,50,50,false );
		testUnit3 = new Unit("Baloe",new Vector3d(25,25,25),50,50,50,50,false );
	}
	
	
	
	///////////////////////////
	///	GENERAL POSITIONING ///
	///////////////////////////
	
	@Test
	public void getPosition_valid() throws Exception{
		Vector3d position = new Vector3d(15.5,15.5,15.5);
		testUnit1.setPosition(position);
		assertEquals(position,testUnit1.getPosition());
		assertEquals(new int[] {15,15,15}, testUnit1.getCube(testUnit1.getPosition()));
	}
	
	@Test
	public void getPosition_nonvalid() throws Exception{
		Vector3d oldPos = testUnit1.getPosition();
		Vector3d position = new Vector3d(-15,15,-15);
		try {
			testUnit1.setPosition(position);
		} catch (OutOfBoundsException e) {
		}
		assertEquals(oldPos,testUnit1.getPosition());
	}
	
	@Test
	public void AdjacentTo_general() throws Exception{
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
	public void Orientation_general() throws Exception{
		assertTrue(testUnit1.getOrientation() ==0);
	}
	/////////////////
	///	UNIT NAME ///
	/////////////////
	@Test
	public void getName_valid() throws Exception{
		assertEquals("Phao",testUnit1.getName());
	}
	
	@Test
	public void getName_invalid() throws Exception{
		
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
	public void getAttribute_valid() throws Exception{
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
	public void getAttribute_nonvalid() throws Exception{
		int oldAgi = testUnit1.getAgility();
		int oldStr = testUnit1.getAgility();
		int oldTgh = testUnit1.getAgility();
		int oldWgt = testUnit1.getAgility();
		
		//Negative Number
		testUnit1.setAgility(-1);
		//Too high number
		testUnit1.setStrength(1000);
		//Too low number
		testUnit1.setToughness(0);
		//Not following the Weight rule
		testUnit1.setWeight(10);
		
		assertEquals(oldAgi,testUnit1.getAgility());
		assertEquals(oldStr,testUnit1.getStrength());
		assertEquals(oldTgh,testUnit1.getToughness());
		assertEquals(oldWgt,testUnit1.getWeight());
	}
	
	
	/////////////////////////////
	///	HITPOINTS AND STAMINA ///
	/////////////////////////////
	
	@Test
	public void getMaxHitpoints_general() throws Exception{
		testUnit1.setWeight(50);
		testUnit1.setToughness(50);	
		assertEquals(50,testUnit1.getMaxHitpoints());
	}
	
	///////////////////////////
	/// VELOCITY AND MOVING ///
	///////////////////////////
	
	@Test
	public void getSpeed_general() throws Exception{
		testUnit1.setAgility(50);
		testUnit1.setStrength(50);
		testUnit1.setWeight(50);
		
		//Horizontal Movement
		testUnit1.setSpeed(0);
		assertTrue(1.5==testUnit1.getCurrentSpeed());
		
		//Downward Movement
		testUnit1.setSpeed(-1);
		assertTrue(1.8==testUnit1.getCurrentSpeed());
		
		//Upward Movement
		testUnit1.setSpeed(1);
		assertTrue(0.75==testUnit1.getCurrentSpeed());
	}
	
	@Test
	public void	getDestination_general() throws Exception{
		testUnit1.setDestination(new Vector3d(10,10,10));
		assertEquals(new Vector3d (10,10,10),testUnit1.getDestination());
	}
	
	@Test
	public void getNextPosition_general() throws Exception{
		Vector3d position = new Vector3d(10,10,10);
		testUnit1.setNextPosition(position);
		assertEquals(position,testUnit1.getNextPosition());
	}
	
	@Test
	public void getMovingTime_general() throws Exception{
		testUnit1.setMovingTime(1);
		assertTrue(1==testUnit1.getMovingTime());
	}
	
	@Test
	public void getSprintingTime_general() throws Exception{
		testUnit1.setSprintingTime(1);
		assertTrue(1==testUnit1.getSprintingTime());
	}
	
	@Test
	public void UpdateSprinting_overLimit() throws Exception{
		//preparation
		testUnit1.setSprintingTime(0.31);
		testUnit1.setStamina(testUnit1.getMaxHitpoints());
		
		//testing
		testUnit1.updateSprinting(0.2);
		assertTrue(testUnit1.getSprintingTime()==0.01);
		assertTrue(testUnit1.getStamina()==testUnit1.getMaxHitpoints()-5);
	}
	
}
