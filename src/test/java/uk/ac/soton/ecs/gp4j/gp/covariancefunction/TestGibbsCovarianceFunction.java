package uk.ac.soton.ecs.gp4j.gp.covariancefunction;

import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.GibbsCovarianceFunction;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.MultivariateRealFunction;
import uk.ac.soton.ecs.gp4j.util.ExtendedTestCase;
import uk.ac.soton.ecs.gp4j.util.MathUtils;
import Jama.Matrix;

public class TestGibbsCovarianceFunction extends ExtendedTestCase {

	private GibbsCovarianceFunction function;

	private double[] loghyper = new double[] {};

	@Override
	protected void setUp() throws Exception {
		function = new GibbsCovarianceFunction();

		MultivariateRealFunction lds = new DummyMultivariateFunction();

		function.setLds(lds, lds);
	}

	public void testCalculateCovarianceMatrix1() throws Exception {
		Matrix trainX = new Matrix(new double[][] { { 1, 3 }, { 4, 6 },
				{ 3, 1 } });

		assertEquals(0, function.getHyperParameterCount(trainX));

		double[][] expectedCovarianceMatrix = {
				{ 1.000000000000000, 0.002581341245064, 0.166438723059507 },
				{ 0.002581341245064, 1.000000000000000, 0.000192000748505 },
				{ 0.166438723059507, 0.000192000748505, 1.000000000000000 } };

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

		assertEquals(new double[][] { { 1 }, { 1 }, { 1 } },
				testCovarianceMatrix.getArray(), 1e-4);
	}

	public void testcalculateTrainTestCovarianceMatrix() throws Exception {
		Matrix trainX = new Matrix(new double[][] { { 1, 2 }, { 3, 4 },
				{ 3, 5 }, { 3, 1 } });

		Matrix testX = new Matrix(new double[][] { { 3, 4 }, { 3, 2 },
				{ 5, 7 }, { 7, 5 }, { 4, 1 }, { 6, 4 } });

		Matrix trainTestCovarianceMatrix = function
				.calculateTrainTestCovarianceMatrix(loghyper, trainX, testX);

		double[][] expected = {
				{ 0.116012713126809, 0.401506278423197, 0.000009744227677,
						0.000003171918304, 0.105894964168674, 0.000075451670218 },
				{ 1.000000000000000, 0.328552300939420, 0.009989632721754,
						0.002422771048876, 0.066449102717602, 0.020918660945689 },
				{ 0.700262643567460, 0.072059361487016, 0.048247121289449,
						0.002327784712719, 0.007826033540566, 0.009676233100530 },
				{ 0.088120253583173, 0.795333955179924, 0.000012362073575,
						0.000117682930926, 0.798329540242918, 0.002581341245064 } };

		assertEquals(expected, trainTestCovarianceMatrix.getArray(), 1e-4);
	}

	private class DummyMultivariateFunction implements MultivariateRealFunction {
		public double evaluate(double[] x) {
			double[] mu = { 5, 5 };
			double[][] sigma = { { 2, 0 }, { 0, 2 } };

			return 1.5 - 12 * MathUtils.mvnPDF(x, mu, sigma);
		}
	}
}
