package uk.ac.soton.ecs.gp4j.bmc;

import uk.ac.soton.ecs.gp4j.gp.GaussianProcess;
import uk.ac.soton.ecs.gp4j.gp.GaussianProcessRegression;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SquaredExponentialCovarianceFunction;
import uk.ac.soton.ecs.gp4j.util.ArrayUtils;
import uk.ac.soton.ecs.gp4j.util.ExtendedTestCase;
import uk.ac.soton.ecs.gp4j.util.MatrixUtils;
import Jama.Matrix;

public class TestGaussianProcessMixture extends ExtendedTestCase {
	private GaussianProcessMixture mixture;

	@Override
	protected void setUp() throws Exception {
		mixture = new GaussianProcessMixture();

		double[][] logHyperSamples = { { -1.0000, -0.412213335097881 },
				{ -1.0000, 1.587786664902119 }, { 1.0000, -0.412213335097881 },
				{ 1.0000, 1.587786664902119 } };

		double[] trainX = { 0.143294651245258, 1.068329171244070,
				1.400296638539415, 4.148441051573910, 5.899268208101073,
				6.073574352497997, 6.591907519159514, 7.691304822308147,
				8.437107751840202, 9.802721452635705 };

		double[] trainY = { 3.682506582087291, 3.094003344745013,
				1.578292600230846, 0.308023950785742, 0.705951974756290,
				0.716334400761197, 0.029153096737698, -2.767447747186930,
				-2.881933227098540, -4.007349623759650 };

		double[] weights = { 0.060239598729430, 0.875375370332205,
				0.004145432208934, 0.060239598729430 };

		for (int i = 0; i < logHyperSamples.length; i++) {
			GaussianProcessRegression regression = new GaussianProcessRegression(
					logHyperSamples[i], SquaredExponentialCovarianceFunction
							.getInstance());

			GaussianProcess result = regression.calculateRegression(trainX,
					trainY);

			mixture.addGaussianProcess(result, weights[i]);
		}
	}

	public void testCalculatePrediction() throws Exception {
		Matrix testX = MatrixUtils.createColumnVector(ArrayUtils.linspace(0,
				10, 11));

		GaussianProcessMixturePrediction prediction = mixture
				.calculatePrediction(testX);

		double[][] expectedMean = { { 3.230622312241901 },
				{ 3.215829154409750 }, { 0.001657738786024 },
				{ 0.135250293421391 }, { 0.306125311101709 },
				{ 0.018896311689699 }, { 0.734179136800384 },
				{ -0.540920780408800 }, { -2.983195547970595 },
				{ -1.263639704822526 }, { -3.536718409913008 } };

		double[][] expectedVariance = { { 3.080494356520392 },
				{ 0.322850571875865 }, { 18.851549563985778 },
				{ 21.238982797149088 }, { 3.159955857463649 },
				{ 20.718950536348743 }, { 0.020697454032821 },
				{ 13.058786426737434 }, { 7.130164795135363 },
				{ 18.881284016867518 }, { 5.311348848526237 } };

		assertEquals(expectedMean, prediction.getMean().getArray(), 1e-7);
		assertEquals(expectedVariance, prediction.getVariance().getArray(),
				1e-7);
	}
}
