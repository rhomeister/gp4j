package uk.ac.soton.ecs.gp4j.gp.covariancefunction;

import junit.framework.TestCase;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.PeriodicCovarianceFunction;
import Jama.Matrix;

public class TestPeriodicCovarianceFunction extends TestCase {

	private PeriodicCovarianceFunction function;

	@Override
	protected void setUp() throws Exception {
		function = (PeriodicCovarianceFunction) PeriodicCovarianceFunction
				.getInstance();
	}

	public void testCalculateCovarianceMatrix1() throws Exception {
		Matrix trainX = new Matrix(new double[][] { { 0.3067, 0.7635, 0.3171,
				0.1184, 0.6425, 0.1959, 0.9482, 0.9679 } });
		trainX = trainX.transpose();

		assertEquals(2, function.getHyperParameterCount(trainX));

		double[][] expectedCovarianceMatrix = { { 9.0000, 0.3080, 1.0749 },
				{ 0.3080, 9.0000, 0.3490 }, { 1.0749, 0.3490, 9.0000 } };

		Matrix covarianceMatrix = function.calculateTrainTestCovarianceMatrix(
				new double[] { 0, 4 }, trainX, trainX);

		//covarianceMatrix.print(10, 10);
	}
}
