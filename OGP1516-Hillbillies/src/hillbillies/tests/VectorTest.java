package hillbillies.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import helperclasses.Vector3d;

public class VectorTest {
	
	private static final Vector3d v1 = new Vector3d(1, 2, 3);
	private static final Vector3d v2 = new Vector3d(4, 5, 6);
	private static final Vector3d v3 = new Vector3d(new double[] {1, 2, 3});
	private static final Vector3d v4 = new Vector3d(-1, -2, -3);
	private static final Vector3d v5 = new Vector3d(new double[] {2, 4, 6});
	private static final Vector3d intv = new Vector3d((int) 1, (int) 1, (int) 1);
	private static final Vector3d nullVector = new Vector3d (0, 0, 0);

	
	
	@Test
	public void testEquals() {
		assert(v1.equals(v3));
		assert(!v1.equals(v2));
		assert(v3.equals(v1));
	}
	
	@Test
	public void testIsNullVector() {
		assert(nullVector.isNullVector());
	}
	
	@Test
	public void testGetX() {
		assert(v1.getX() == 1);
		assert(nullVector.getX() == 0);
	}
	
	@Test
	public void testGetY() {
		assert(v1.getY() == 2);
		assert(nullVector.getY() == 0);
	}
	
	@Test
	public void testGetZ() {
		assert(v1.getZ() == 3);
		assert(nullVector.getZ() == 0);
	}
	
	@Test
	public void testGetDimension() {
		assert(v3.getDimension(2) == 3);
		assert(nullVector.getDimension(1) == 0);
		assert(v4.getDimension(2) == -3);
	}
	
	@Test
	public void testGetDouble() {
		assert(v2.equals(new Vector3d(v2.getDouble())));
		assert(nullVector.equals(new Vector3d(nullVector.getDouble())));
	}
	
	@Test
	public void testAdd() {
		assert (nullVector.equals(v1.add(v4)));
		assert (v1.add(v3).equals(v5));
		assert (v3.add(v1).add(v4).equals(v3));
	}
	
	@Test
	public void testSubtract() {
		assert(nullVector.equals(v1.subtract(v3)));
		assert(v5.equals(v1.subtract(v4)));
	}
	
	@Test
	public void testMultiply() {
		assert(nullVector.equals(nullVector.multiply(0)));
		assert(v5.equals(v3.multiply(2)));
	}
	
	@Test
	public void testDivide() {
		assert(v1.divide(0).equals(v1));
		assert(v1.equals(v5.divide(2)));
	}
	
	@Test
	public void testScalarProduct() {
		assert(v1.scalarProduct(nullVector) == 0);
		assert(v1.scalarProduct(v1) == 14);
	}
	
	@Test
	public void testCalcNorm() {
		assert(v1.calcNorm() == Math.sqrt(14));
		assert(intv.calcNorm() == Math.sqrt(3));
	}
	
	@Test
	public void testNormalize() {
		double norm = v1.calcNorm();
		assert(v1.normalize().equals(new Vector3d(1/norm, 2/norm, 3/norm)));
		assert(nullVector.normalize().equals(nullVector));
	}
	
	@Test
	public void testCrossProduct() {
		assert(v1.crossProduct(v2).equals(new Vector3d(-3, 6, -3)));
	}

}
