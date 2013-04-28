package uk.ac.soton.ecs.gp4j.bmc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.soton.ecs.gp4j.gp.GaussianProcess;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SquaredExponentialCovarianceFunction;
import uk.ac.soton.ecs.gp4j.util.ExtendedTestCase;
import uk.ac.soton.ecs.gp4j.util.MatrixUtils;
import Jama.Matrix;

public class TestGaussianProcessRegressionBMC extends ExtendedTestCase {
	private GaussianProcessRegressionBMC regressionBMC;

	double[] trainX = { 0.143294651245258, 1.068329171244070,
			1.400296638539415, 4.148441051573910, 5.899268208101073,
			6.073574352497997, 6.591907519159514, 7.691304822308147,
			8.437107751840202, 9.802721452635705 };

	double[] trainY = { 3.682506582087291, 3.094003344745013,
			1.578292600230846, 0.308023950785742, 0.705951974756290,
			0.716334400761197, 0.029153096737698, -2.767447747186930,
			-2.881933227098540, -4.007349623759650 };

	@Override
	protected void setUp() throws Exception {
		List<BasicPrior> priors = Arrays.asList(new BasicPrior(1, 5, 1, 0.25),
				new BasicPrior(2, 1, 1, 0.25), new BasicPrior(3, 9, 1, 0.25));

		regressionBMC = new GaussianProcessRegressionBMC(
				SquaredExponentialCovarianceFunction.getInstance(), priors);
	}

	public void testCheckSampleCount() throws Exception {
		assertEquals(6, regressionBMC.getGpRegressions().size());
	}

	public void testKSinv_NS_KSinv() throws Exception {
		double[][] expectedResult = {
				{ 0.001492391615319, 0.000100238387129, -0.000000067252450,
						0.000102700024585, 0.000006897978196,
						-0.000000004628027 },
				{ 0.000100238387129, 0.008830292856068, 0.000100238387129,
						0.000006897978196, 0.000607663085278, 0.000006897978196 },
				{ -0.000000067252450, 0.000100238387129, 0.001492391615319,
						-0.000000004628027, 0.000006897978196,
						0.000102700024585 },
				{ 0.000102700024585, 0.000006897978196, -0.000000004628027,
						0.001492391615319, 0.000100238387129,
						-0.000000067252450 },
				{ 0.000006897978196, 0.000607663085278, 0.000006897978196,
						0.000100238387129, 0.008830292856068, 0.000100238387129 },
				{ -0.000000004628027, 0.000006897978196, 0.000102700024585,
						-0.000000067252450, 0.000100238387129,
						0.001492391615319 } };

		assertEquals(expectedResult, regressionBMC.getKSinv_NS_KSinv()
				.getArray(), 1e-8);
	}

	public void testCalculateRegression() throws Exception {
		List<BasicPrior> priors = new ArrayList<BasicPrior>();

		priors.add(new BasicPrior(3, 5, .1, 0.25));
		priors.add(new BasicPrior(2, 1, 1, 0.25));

		regressionBMC = new GaussianProcessRegressionBMC(
				SquaredExponentialCovarianceFunction.getInstance(), priors);

		GaussianProcessMixture result = regressionBMC.calculateRegression(
				trainX, trainY);

		List<GaussianProcess> gaussianProcesses = result.getGaussianProcesses();
		assertEquals(6, gaussianProcesses.size());

		double weightSum = 0.0;

		double[] expectedWeights = { 0.060335255439459, 0.876765411596289,
				0.004052494419149, 0.058889067619210, -0.000002718920243,
				-0.000039510153864 };

		double[][] expectedHyperParameters = {
				{ 1.409437912434100, -2.000000000000000 },
				{ 1.409437912434100, 2.000000000000000 },
				{ 1.609437912434100, -2.000000000000000 },
				{ 1.609437912434100, 2.000000000000000 },
				{ 1.809437912434100, -2.000000000000000 },
				{ 1.809437912434100, 2.000000000000000 } };

		for (int i = 0; i < gaussianProcesses.size(); i++) {
			GaussianProcess process = gaussianProcesses.get(i);

			double weight = result.getWeight(process);
			assertEquals(expectedWeights[i], weight, 1e-6);
			assertEquals(expectedHyperParameters[i], process
					.getLogHyperParameters(), 1e-6);

			weightSum += weight;
		}

		assertEquals(1.0, weightSum, 1e-8);
	}

	public void testUpdateRegression() throws Exception {
		List<BasicPrior> priors = new ArrayList<BasicPrior>();

		Matrix trainX = MatrixUtils.createColumnVector(this.trainX);
		Matrix trainY = MatrixUtils.createColumnVector(this.trainY);
		int nSamples = trainX.getRowDimension();

		priors.add(new BasicPrior(3, 5, .1, 0.25));
		priors.add(new BasicPrior(2, 1, 1, 0.25));

		GaussianProcessRegressionBMC oneShot = new GaussianProcessRegressionBMC(
				SquaredExponentialCovarianceFunction.getInstance(), priors);

		oneShot.calculateRegression(trainX, trainY);

		GaussianProcessRegressionBMC updated = new GaussianProcessRegressionBMC(
				SquaredExponentialCovarianceFunction.getInstance(), priors);

		updated.calculateRegression(trainX.getMatrix(0, nSamples - 2, 0, 0),
				trainY.getMatrix(0, nSamples - 2, 0, 0));

		updated.updateRegression(trainX.getMatrix(nSamples - 1, nSamples - 1,
				0, 0), trainY.getMatrix(nSamples - 1, nSamples - 1, 0, 0));

		assertEquals(oneShot.getWeights(), updated.getWeights());
	}

	public void testCopy() throws Exception {
		List<BasicPrior> priors = new ArrayList<BasicPrior>();

		priors.add(new BasicPrior(3, 5, .1, 0.25));
		priors.add(new BasicPrior(2, 1, 1, 0.25));

		regressionBMC = new GaussianProcessRegressionBMC(
				SquaredExponentialCovarianceFunction.getInstance(), priors);

		regressionBMC.calculateRegression(trainX, trainY);

		Matrix q = MatrixUtils.createColumnVector(this.trainX);
		Matrix d = MatrixUtils.createColumnVector(this.trainY);

		regressionBMC.updateRegression(q, d);

		GaussianProcessRegressionBMC copy = regressionBMC.copy();

		copy.updateRegression(q, d);
	}
}
