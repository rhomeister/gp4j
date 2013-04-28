package uk.ac.soton.ecs.gp4j.gp.covariancefunction;

import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.CovarianceFunction;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.Matern3ARDCovarianceFunction;
import uk.ac.soton.ecs.gp4j.util.ExtendedTestCase;
import Jama.Matrix;

public class TestMatern3ARDCovarianceFunction extends ExtendedTestCase {
	private CovarianceFunction function;

	@Override
	protected void setUp() throws Exception {
		function = new Matern3ARDCovarianceFunction();
	}

	public void testCalculateCovarianceFunction() throws Exception {
		Matrix trainX = new Matrix(new double[][] { { 1, 2 }, { 4, 6 },
				{ 8, 10 } });

		Matrix expected = new Matrix(new double[][] {
				{ 8.999999999999998, 0.126506432593857, 0.000116065936684 },
				{ 0.126506432593857, 8.999999999999998, 0.034042366027649 },
				{ 0.000116065936684, 0.034042366027649, 8.999999999999998 } });

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
				{ 0.912057335892890, 0.058004295898358, 0.003042708674464,
						0.290784570014503 },
				{ 2.680386911366683, 2.680386911366683, 0.080687748191146,
						0.631582077878401 },
				{ 0.004105696604645, 0.048355928178793, 0.027552834079961,
						0.003042708674464 } });

		Matrix covMatrix = function.calculateTrainTestCovarianceMatrix(
				new double[] { Math.log(1), Math.log(2), Math.log(3) }, trainX,
				testX);

		assertEquals(expected, covMatrix, 1e-8);
	}
}
