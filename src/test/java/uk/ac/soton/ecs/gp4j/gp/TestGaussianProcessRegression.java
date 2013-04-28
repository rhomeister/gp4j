package uk.ac.soton.ecs.gp4j.gp;

import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SquaredExponentialCovarianceFunction;
import uk.ac.soton.ecs.gp4j.util.ExtendedTestCase;
import uk.ac.soton.ecs.gp4j.util.MatrixUtils;
import Jama.Matrix;

public class TestGaussianProcessRegression extends ExtendedTestCase {

	protected GaussianProcessRegression regression1;
	private GaussianProcessRegression regression2;
	protected GaussianProcess gp1;
	protected GaussianProcess gp2;
	private final double[] loghyper = new double[] { Math.log(2), Math.log(1) };
	private Matrix trainX2;
	private Matrix trainY2;
	private GaussianProcessRegression regression3;
	private Matrix trainX3;
	private Matrix trainY3;
	private GaussianProcess gp3;

	@Override
	protected void setUp() throws Exception {
		setupGP1();
		setupGP2();
		setupGP3();
	}

	public void testEmptyRegression() throws Exception {
		GaussianProcessRegression regression = new GaussianProcessRegression(
				new double[] { 0, 0 }, SquaredExponentialCovarianceFunction
						.getInstance());

		GaussianProcess currentPredictor = (GaussianProcess) regression
				.getCurrentPredictor();

		GaussianProcessPrediction prediction = currentPredictor
				.calculatePrediction(new Matrix(new double[][] { { 51.0000 } }));
	}

	private void setupGP1() {
		regression1 = new GaussianProcessRegression(new double[] { -1.0000,
				-0.412213335097881 }, SquaredExponentialCovarianceFunction
				.getInstance());

		double[] trainX = { 0.143294651245258, 1.068329171244070,
				1.400296638539415, 4.148441051573910, 5.899268208101073,
				6.073574352497997, 6.591907519159514, 7.691304822308147,
				8.437107751840202, 9.802721452635705 };

		double[] trainY = { 3.682506582087291, 3.094003344745013,
				1.578292600230846, 0.308023950785742, 0.705951974756290,
				0.716334400761197, 0.029153096737698, -2.767447747186930,
				-2.881933227098540, -4.007349623759650 };

		gp1 = regression1.calculateRegression(trainX, trainY);
	}

	private void setupGP2() {
		regression2 = new GaussianProcessRegression(loghyper,
				SquaredExponentialCovarianceFunction.getInstance());

		trainX2 = new Matrix(new double[][] { { 1 }, { 2 }, { 9 }, { 10 } });
		trainY2 = new Matrix(new double[][] { { 6 }, { 4 }, { 5 }, { 8 } });

		gp2 = regression2.calculateRegression(trainX2, trainY2);

	}

	/**
	 * Multi-dimensional training points
	 */
	private void setupGP3() {
		regression3 = new GaussianProcessRegression(loghyper,
				SquaredExponentialCovarianceFunction.getInstance());

		trainX3 = new Matrix(new double[][] { { 1, 1 }, { 2, 1 }, { 9, 4 },
				{ 10, 6 } });
		trainY3 = new Matrix(new double[][] { { 6 }, { 4 }, { 5 }, { 8 } });

		gp3 = regression3.calculateRegression(trainX3, trainY3);
	}

	public void testGPR1() {
		double[][] expectedAlpha = { { 8.073251614609459 },
				{ 7.780571249914467 }, { -1.602466755414315 },
				{ 0.702465093276948 }, { 0.354727623701490 },
				{ 1.496497277523218 }, { -0.484491096060702 },
				{ -5.556350491679856 }, { -5.851415213782748 },
				{ -9.133095778136587 } };

		assertEquals(expectedAlpha, gp1.getAlpha().getArray(), 1e-8);
	}

	public void testGPR2() {
		assertEquals(new double[][] { { 11.1183 }, { -5.7970 }, { -9.2783 },
				{ 16.1895 } }, gp2.getAlpha().getArray(), 1e-4);
	}

	public void testGPR3() throws Exception {
		assertEquals(new Matrix(new double[] { 11.169205071167482,
				-5.857616285435412, 1.010261014863868, 7.459312920911322 }, 4),
				gp3.getAlpha(), 1e-4);
	}

	public void testUpdate1() throws Exception {
		int sampleCount = regression1.getTrainX().getRowDimension();

		testUpdate(regression1, 10.520904370065907, -5.615556013689354);

		assertEquals(sampleCount + 1, regression1.getTrainX().getRowDimension());

		testUpdate(regression1,
				new double[] { 13.542138, 16.433782, 18.347834 }, new double[] {
						-7.273812, -8.47814728, -5.124781 });

		assertEquals(sampleCount + 4, regression1.getTrainX().getRowDimension());

		testUpdate(regression1, new double[] { 19.53214, -1.1247812 },
				new double[] { -6.123781, 5.123781 });

		assertEquals(sampleCount + 6, regression1.getTrainX().getRowDimension());
	}

	public void testUpdate2() throws Exception {
		int sampleCount = regression2.getTrainX().getRowDimension();

		testUpdate(regression2, 13.0, 3.0);

		assertEquals(sampleCount + 1, regression2.getTrainX().getRowDimension());
		testUpdate(regression2, new double[] { 12, 14, 17 }, new double[] { 8,
				10, 12 });

		assertEquals(sampleCount + 4, regression2.getTrainX().getRowDimension());
	}

	private void testUpdate(GaussianProcessRegression regression,
			double addedTrainX, double addedTrainY) throws Exception {
		testUpdate(regression, new double[] { addedTrainX },
				new double[] { addedTrainY });
	}

	private void testUpdate(GaussianProcessRegression regression,
			double[] addedTrainX, double[] addedTrainY) throws Exception {
		GaussianProcessRegression oneShot = new GaussianProcessRegression(
				regression.getLogHyperParameters(), regression.getFunction());

		Matrix updatedTrainX = MatrixUtils.append(regression.getTrainX(),
				addedTrainX);
		Matrix updatedTrainY = MatrixUtils.append(regression.getTrainY(),
				addedTrainY);

		GaussianProcess oneShotResult = oneShot.calculateRegression(
				updatedTrainX, updatedTrainY);

		GaussianProcess updatedResult = regression.updateRegression(
				addedTrainX, addedTrainY);

		assertEquals(oneShotResult.getCholTrainingCovarianceMatrix(),
				updatedResult.getCholTrainingCovarianceMatrix(), 1e-6);
		assertEquals(oneShotResult.getAlpha(), updatedResult.getAlpha(), 1e-6);
		assertEquals(oneShotResult.getLogLikelihood(), updatedResult
				.getLogLikelihood(), 1e-6);
	}

	public void testDownDate1() throws Exception {
		for (int i = regression1.getTrainingSampleCount(); i > 1; i--) {
			testDownDate(regression1);
		}
	}

	public void testDownDate2() throws Exception {
		for (int i = regression2.getTrainingSampleCount(); i > 1; i--) {
			testDownDate(regression2);
		}
	}

	public void testDownDate3() throws Exception {
		for (int i = regression3.getTrainingSampleCount(); i > 1; i--) {
			testDownDate(regression3);
		}
	}

	public void testDownDate(GaussianProcessRegression regression)
			throws Exception {
		int m = regression.getTrainX().getColumnDimension();

		GaussianProcessRegression oneShot = new GaussianProcessRegression(
				regression.getLogHyperParameters(), regression.getFunction());

		Matrix updatedTrainX = regression.getTrainX().getMatrix(1,
				regression.getTrainingSampleCount() - 1, 0, m - 1);
		Matrix updatedTrainY = regression.getTrainY().getMatrix(1,
				regression.getTrainingSampleCount() - 1, 0, 0);

		GaussianProcess oneShotResult = oneShot.calculateRegression(
				updatedTrainX, updatedTrainY);

		double oldLogLikelihood = regression.getLogLikelihood();

		GaussianProcess updatedResult = regression.downdateRegression();

		assertEquals(updatedTrainX, regression.getTrainX());
		assertEquals(updatedTrainY, regression.getTrainY());

		assertEquals(oneShotResult.getCholTrainingCovarianceMatrix(),
				updatedResult.getCholTrainingCovarianceMatrix(), 1e-6);
		assertEquals(oneShotResult.getAlpha(), updatedResult.getAlpha(), 1e-6);
		assertTrue(oneShotResult.getLogLikelihood() > updatedResult
				.getLogLikelihood());
		assertEquals(oldLogLikelihood, updatedResult.getLogLikelihood());
	}

	public void testGetLogLikelihoodGP1() throws Exception {
		assertEquals(-64.626784653987173, gp1.getLogLikelihood(), 1e-7);
	}

	public void testGetLogLikelihoodGP2() throws Exception {
		assertEquals(-65.490431977404000, gp2.getLogLikelihood(), 1e-7);
	}

	public void testGetLogLikelihoodGP3() throws Exception {
		assertEquals(-56.907904263131023, gp3.getLogLikelihood(), 1e-7);
	}
}
