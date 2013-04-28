package uk.ac.soton.ecs.gp4j.util;

import Jama.Matrix;
import junit.framework.TestCase;

public abstract class ExtendedTestCase extends TestCase {
	public void assertEquals(double[][] expected, double[][] actual,
			double delta) {
		assertEquals("Arrays are of different size.", expected.length,
				actual.length);

		for (int i = 0; i < actual.length; i++) {
			assertEquals(expected[i], actual[i], delta);
		}
	}

	public void assertEquals(double[][] expected, double[][] actual) {
		assertEquals(expected, actual, 0);
	}

	public void assertEquals(double[] expected, double[] actual, double delta) {
		assertEquals("Arrays are of different size.", expected.length,
				actual.length);

		for (int i = 0; i < actual.length; i++) {
			assertEquals(expected[i], actual[i], delta);
		}
	}

	public void assertEquals(double[] expected, double[] actual) {
		assertEquals(expected, actual, 0);
	}

	public void assertEquals(Matrix expected, Matrix actual) {
		assertEquals(expected, actual, 0);
	}

	public void assertEquals(Matrix expected, Matrix actual, double delta) {
		assertEquals(expected.getArray(), actual.getArray(), delta);
	}
}
