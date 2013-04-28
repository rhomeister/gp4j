package uk.ac.soton.ecs.gp4j.gp.covariancefunction;

import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SquaredExponentialCovarianceFunction;
import uk.ac.soton.ecs.gp4j.util.ExtendedTestCase;
import Jama.Matrix;

public class TestSquaredExponentialCovarianceFunction extends ExtendedTestCase {
	private SquaredExponentialCovarianceFunction function;

	private static double[] loghyper = { Math.log(2), Math.log(3) };

	@Override
	protected void setUp() throws Exception {
		function = SquaredExponentialCovarianceFunction.getInstance();
	}

	public void testCalculateCovarianceMatrix1() throws Exception {
		Matrix trainX = new Matrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 },
				{ 3, 5, 1 } });

		assertEquals(2, function.getHyperParameterCount(trainX));

		double[][] expectedCovarianceMatrix = { { 9.0000, 0.3080, 1.0749 },
				{ 0.3080, 9.0000, 0.3490 }, { 1.0749, 0.3490, 9.0000 } };

		Matrix covarianceMatrix = function.calculateCovarianceMatrix(loghyper,
				trainX);

		assertEquals(expectedCovarianceMatrix, covarianceMatrix.getArray(),
				1e-4);
	}

	public void testCalculateCovarianceMatrix2() throws Exception {
		Matrix trainX = new Matrix(new double[][] { { 1, 2, 3 } });

		double[][] expectedCovarianceMatrix = { { 9.0000 } };

		Matrix covarianceMatrix = function.calculateCovarianceMatrix(loghyper,
				trainX);

		assertEquals(expectedCovarianceMatrix, covarianceMatrix.getArray(),
				1e-4);
	}

	public void testCalculateCovarianceMatrix3() throws Exception {
		Matrix trainX = new Matrix(new double[][] { { 1 }, { 2 }, { 3 } });

		double[][] expectedCovarianceMatrix = {
				{ 8.999999999999998, 7.942472123261357, 5.458775937413700 },
				{ 7.942472123261357, 8.999999999999998, 7.942472123261357 },
				{ 5.458775937413700, 7.942472123261357, 8.999999999999998 } };

		Matrix covarianceMatrix = function.calculateCovarianceMatrix(loghyper,
				trainX);

		assertEquals(expectedCovarianceMatrix, covarianceMatrix.getArray(),
				1e-4);
	}

	public void testCalculateTestCovarianceMatrix() throws Exception {
		Matrix testX = new Matrix(
				new double[][] { { 5, 3 }, { 6, 1 }, { 7, 3 } });

		Matrix testCovarianceMatrix = function.calculateTestCovarianceMatrix(
				loghyper, testX);

		assertEquals(new double[][] { { 9 }, { 9 }, { 9 } },
				testCovarianceMatrix.getArray(), 1e-4);
	}

	public void testCalculateTestCovarianceMatrix2() throws Exception {
		Matrix testX = new Matrix(new double[][] { { 0.143294651245258 },
				{ 1.068329171244070 }, { 1.400296638539415 },
				{ 4.148441051573910 }, { 5.899268208101073 },
				{ 6.073574352497997 }, { 6.591907519159514 },
				{ 7.691304822308147 }, { 8.437107751840202 },
				{ 9.802721452635705 } });

		Matrix testCovarianceMatrix = function.calculateTestCovarianceMatrix(
				new double[] { -1, -0.412213335097881 }, testX);

		assertEquals(new double[][] { { 0.438486317686625 },
				{ 0.438486317686625 }, { 0.438486317686625 },
				{ 0.438486317686625 }, { 0.438486317686625 },
				{ 0.438486317686625 }, { 0.438486317686625 },
				{ 0.438486317686625 }, { 0.438486317686625 },
				{ 0.438486317686625 } }, testCovarianceMatrix.getArray(), 1e-4);
	}

	public void testcalculateTrainTestCovarianceMatrix() throws Exception {
		Matrix trainX = new Matrix(new double[][] { { 1, 2 }, { 3, 4 },
				{ 3, 5 }, { 3, 1 } });

		Matrix testX = new Matrix(new double[][] { { 3, 4 }, { 3, 2 },
				{ 5, 7 }, { 7, 5 }, { 4, 1 }, { 6, 4 } });

		Matrix trainTestCovarianceMatrix = function
				.calculateTrainTestCovarianceMatrix(loghyper, trainX, testX);

		double[][] expected = {
				{ 3.3109, 5.4588, 0.0535, 0.0325, 2.5785, 0.2398 },
				{ 9.0000, 5.4588, 1.7722, 1.0749, 2.5785, 2.9219 },
				{ 7.9425, 2.9219, 3.3109, 1.2180, 1.0749, 2.5785 },
				{ 2.9219, 7.9425, 0.0606, 0.1648, 7.9425, 0.9486 } };

		assertEquals(expected, trainTestCovarianceMatrix.getArray(), 1e-4);
	}
}
