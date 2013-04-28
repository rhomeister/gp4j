package uk.ac.soton.ecs.gp4j.gp.covariancefunction;

import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.CovarianceFunction;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.CovarianceFunctionFactory;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SquaredExponentialARDCovarianceFunction;
import uk.ac.soton.ecs.gp4j.util.ExtendedTestCase;
import Jama.Matrix;

public class TestSquaredExponentialARDCovarianceFunction extends
		ExtendedTestCase {
	private final CovarianceFunction function = new SquaredExponentialARDCovarianceFunction();

	public void testCalculateCovarianceFunction() throws Exception {
		Matrix trainX = new Matrix(new double[][] { { 1, 2 }, { 4, 6 },
				{ 8, 10 } });

		Matrix expected = new Matrix(new double[][] {
				{ 8.999999999999998, 0.013530952736798, 0.000000000000069 },
				{ 0.013530952736798, 8.999999999999998, 0.000408599367862 },
				{ 0.000000000000069, 0.000408599367862, 8.999999999999998 } });

		Matrix covMatrix = function.calculateCovarianceMatrix(new double[] {
				Math.log(1), Math.log(2), Math.log(3) }, trainX);

		assertEquals(expected, covMatrix, 1e-8);
	}

	public void testCalculateTrainTestCovarianceFunction() throws Exception {
		Matrix trainX = new Matrix(new double[][] { { 1, 2 }, { 4, 6 },
				{ 8, 10 } });

		Matrix testX = new Matrix(new double[][] { { 3, 4 }, { 5, 4 },
				{ 7, 1 }, { 4, 1 } });

		Matrix expected = new Matrix(new double[][] {
				{ 0.738764987615089, 0.001831215321096, 0.000000120963690,
						0.088232895322396 },
				{ 3.310914970542981, 3.310914970542981, 0.004392857191711,
						0.395432402610667 },
				{ 0.000000372594395, 0.001110688236780, 0.000218707481334,
						0.000000120963690 } });

		Matrix covMatrix = function.calculateTrainTestCovarianceMatrix(
				new double[] { Math.log(1), Math.log(2), Math.log(3) }, trainX,
				testX);

		assertEquals(expected, covMatrix, 1e-8);
	}

	public void testNoisyCovarianceFunction() throws Exception {
		CovarianceFunction function = CovarianceFunctionFactory
				.getNoisySquaredExponentialARDCovarianceFunction();

		Matrix testX = new Matrix(new double[][] { { 1, 3, 4 }, { 4, 5, 4 },
				{ 7, 4, 1 }, { 6, 4, 1 } });

		double lengthScale = 1.3;
		double timeScale = 1.7;
		double noise = 1.0;
		function.calculateCovarianceMatrix(new double[] { lengthScale,
				lengthScale, timeScale, 1.0, noise }, testX);

		function.calculateTrainTestCovarianceMatrix(new double[] { lengthScale,
				lengthScale, timeScale, 1.0, noise }, testX, testX);

		assertEquals(5, function.getHyperParameterCount(testX));

	}
}
