package uk.ac.soton.ecs.gp4j.gp;

import Jama.Matrix;

public class TestGaussianProcess extends TestGaussianProcessRegression {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testResetGP() throws Exception {
		regression1.reset();

		GaussianPredictor<?> currentPredictor = regression1
				.getCurrentPredictor();

		currentPredictor.getTrainX().print(10, 10);
		currentPredictor.getTrainY().print(10, 10);
	}

	public void testEmptyRegression() throws Exception {

	}

	public void testCalculatePredictionGP1() {
		Matrix testX = new Matrix(
				new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, 1).transpose();

		GaussianProcessPrediction prediction = gp1.calculatePrediction(testX);

		double[][] expectedMean = { { 3.199774942790113 },
				{ -0.047953423404378 }, { 0.002305480477057 },
				{ 0.283939798501610 }, { 0.038243526147335 },
				{ 0.734736227809643 }, { -0.503577475457732 },
				{ -2.980159376916102 }, { -1.170615200887395 },
				{ -3.468701949221678 } };

		assertEquals(expectedMean, prediction.getMean().getArray(), 1e-8);

		double[][] expectedVariance = { { 0.006668292493440 },
				{ 0.393190487978022 }, { 0.438460637535270 },
				{ 0.065883643025700 }, { 0.432847017936858 },
				{ 0.000432417232723 }, { 0.272463961137511 },
				{ 0.148995144454735 }, { 0.391931477522199 },
				{ 0.109585575063801 } };
		assertEquals(expectedVariance, prediction.getVariance().getArray(),
				1e-4);

	}

	public void testCalculatePredictionGP2() throws Exception {
		Matrix testX = new Matrix(
				new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, 1).transpose();

		GaussianProcessPrediction prediction = gp2.calculatePrediction(testX);

		double[][] expectedMean = { { 6.0000 }, { 4.0000 }, { 1.5601 },
				{ -0.1343 }, { -0.9217 }, { -1.1172 }, { -0.5028 }, { 1.5913 },
				{ 5.0000 }, { 8.0000 } };

		assertEquals(expectedMean, prediction.getMean().getArray(), 1e-4);

		double[][] expectedVariance = { { 0 }, { 0 }, { 0.0868 }, { 0.4269 },
				{ 0.7486 }, { 0.7486 }, { 0.4269 }, { 0.0868 }, { -0.0000 },
				{ 0.0000 } };
		assertEquals(expectedVariance, prediction.getVariance().getArray(),
				1e-4);
	}

	public void testCalculateCovarianceMatrix() throws Exception {
		Matrix testX = new Matrix(new double[] { 1, 2 }, 1).transpose();

		GaussianProcessPrediction calculatePrediction = gp1
				.calculatePrediction(testX, true);

		double[][] expectedVariance = { { 0.0066682925, 0.0122740849 },
				{ 0.0122740849, 0.3931904880 } };

		assertEquals(expectedVariance, calculatePrediction
				.getCovarianceMatrix().getArray(), 1e-7);
	}
}
