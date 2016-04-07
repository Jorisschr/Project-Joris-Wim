package hillbillies.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import helperclasses.OutOfBoundsException;
import helperclasses.Vector3d;
import hillbillies.model.Boulder;
import hillbillies.model.Log;


public class GameObjectTest {
	private Boulder testBoulder;
	private Log testLog;
	
	@Before
	public void initiateTestObjects() throws OutOfBoundsException {
		testBoulder = new Boulder(new Vector3d(10,10,10));
		testLog = new Log(new Vector3d(20,20,10));
	}
	
	
	@Test
	public void testPositionValid() {
		//Initiation
		boolean error = false;
		Vector3d boulderPos = new Vector3d(15,15,10);
		Vector3d logPos = new Vector3d(16,16,10);
		
		//Excecution
		try {
			testBoulder.setPosition(boulderPos);
			testLog.setPosition(logPos);
		} catch (OutOfBoundsException e){
			error = true;
		}
		
		//Checks
		assertTrue(!error);
		assertTrue(testBoulder.getPosition() == boulderPos && testLog.getPosition() == logPos);
	}

	@Test
	public void testPositionInvalid(){
		boolean error = false;
		Vector3d oldPos = testBoulder.getPosition();
		Vector3d illegalPos = new Vector3d(-1,1000,-1);
		
		try{
			testBoulder.setPosition(illegalPos);
		} catch (OutOfBoundsException e){
			error = true;
		}
		
		assertTrue(error);
		assertTrue(testBoulder.getPosition() == oldPos);
	}
	
	@Test
	public void testGetWeight(){
		double weight = testBoulder.getWeight();
		assertTrue(weight<=50 && weight>=10);
	}
	
	@Test	
	public void testCanStandFalse(){
		// TODO: Have To create TestWorld??
		assert true;
	}
	
	@Test
	public void testVelocity() {
		Vector3d fallingVel = new Vector3d(0,0,-3);
		testBoulder.setVelocity(fallingVel);
		assertTrue(testBoulder.getVelocity() == fallingVel);
	}
	
	@Test
	public void testFalling(){
		//Falling
		testBoulder.startFalling();
		double[] velocity = testBoulder.getVelocity().getDoubleArray();
		assertTrue(Arrays.equals(velocity,new Vector3d(0,0,-3).getDoubleArray()));
		
		//Not Falling
		testBoulder.stopFalling();
		double[] velocityStopped = testBoulder.getVelocity().getDoubleArray();
		assertTrue(Arrays.equals(velocityStopped,new Vector3d(0,0,0).getDoubleArray()));
	}
	
	@Test
	public void testUpdatePositionFalling(){
		testBoulder.startFalling();
		Vector3d oldPos = testBoulder.getPosition();
		
		testBoulder.updatePosition(0.5);
		Vector3d newPos = new Vector3d(oldPos.getX(),oldPos.getY(),oldPos.getZ()-1.5);
		
		assertTrue(Arrays.equals(newPos.getDoubleArray(), testBoulder.getPosition().getDoubleArray()));
	}
}
	
