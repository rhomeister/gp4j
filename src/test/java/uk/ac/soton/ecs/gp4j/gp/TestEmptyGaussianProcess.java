package uk.ac.soton.ecs.gp4j.gp;

import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SquaredExponentialCovarianceFunction;
import uk.ac.soton.ecs.gp4j.util.ExtendedTestCase;
import Jama.Matrix;

public class TestEmptyGaussianProcess extends ExtendedTestCase {

	private GaussianProcessRegression regression;
	private GaussianProcess process;

	@Override
	protected void setUp() throws Exception {
		regression = new GaussianProcessRegression(new double[] { 0, 0 },
				SquaredExponentialCovarianceFunction.getInstance());

		regression = regression.copy();

		process = regression.calculateRegression(new double[0], new double[0]);
	}

	public void testTrainingPointCount() throws Exception {
		assertEquals(0, process.getTrainX().getRowDimension());
		assertEquals(0, process.getTrainY().getRowDimension());
	}

	public void testAlpha() throws Exception {
		assertEquals(0, process.getAlpha().getRowDimension());
	}

	public void testLogLikelihood() throws Exception {
		assertEquals(0, process.getLogLikelihood(), 1e-7);
	}

	public void testPrediction() throws Exception {
		GaussianProcessPrediction prediction = process
				.calculatePrediction(new Matrix(1, 3, 1));

		assertEquals(0, prediction.getMean().get(0, 0), 1e-7);
		assertEquals(1, prediction.getStandardDeviation().get(0, 0), 1e-7);
	}

}
