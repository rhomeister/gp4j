package uk.ac.soton.ecs.gp4j.gp.covariancefunctions;

import Jama.Matrix;

public class PeriodicCovarianceFunctionForOneDay implements CovarianceFunction {
	private static PeriodicCovarianceFunctionForOneDay instance;

	public Matrix calculateCovarianceMatrix(double[] loghyper, Matrix trainX) {
		return calculateTrainTestCovarianceMatrix(loghyper, trainX, trainX);
	}

	/**
	 * @Author Muddasser
	 * @param loghyper
	 *            loghyper[0] to loghyper[n-2] are lengthscales for every input
	 *            dimension, loghyper[n-1] is the signal variance
	 */
	public Matrix calculateTrainTestCovarianceMatrix(double[] loghyper,
			Matrix trainX, Matrix testX) {
		int samplesTrain = trainX.getRowDimension();
		int samplesTest = testX.getRowDimension();

		if (samplesTrain == 0 || samplesTest == 0)
			return new Matrix(samplesTrain, samplesTest);

		double signalVariance = Math.exp(2 * loghyper[loghyper.length - 1]);

		// double[][] trainXVals = scaleValues(trainX, loghyper);
		// double[][] testXVals = scaleValues(testX, loghyper);
		double[][] trainXVals = trainX.getArray();
		double[][] testXVals = testX.getArray();

		double[][] result = new double[samplesTrain][samplesTest];

		for (int i = 0; i < samplesTrain; i++) {
			for (int j = 0; j < samplesTest; j++) {

				double dist = calculateDistance(trainXVals[i], testXVals[j]);
				result[i][j] = signalVariance
						* Math.exp(-2
								* Math.pow((Math.sin(dist * Math.PI) / Math
										.exp(loghyper[0])), 2));

			}
		}

		return new Matrix(result);
	}

	private double[][] scaleValues(Matrix matrix, double[] loghyper) {
		double[][] array = matrix.getArrayCopy();

		for (int i = 0; i < loghyper.length - 1; i++) {
			double lengthScale = Math.exp(loghyper[i]);

			for (int j = 0; j < array.length; j++) {
				array[j][i] /= lengthScale;
			}
		}

		return array;
	}

	public Matrix calculateTestCovarianceMatrix(double[] loghyper, Matrix testX) {
		return new Matrix(testX.getRowDimension(), 1, Math
				.exp(2 * loghyper[loghyper.length - 1]));
	}

	public int getHyperParameterCount(Matrix testX) {
		return testX.getColumnDimension() + 1;
	}

	private double calculateDistance(double[] ds, double[] ds2) {
		double sq_dist = 0;

		for (int i = 0; i < ds.length; i++) {
			double diff = ds[i] - ds2[i];
			sq_dist += diff;
		}

		return sq_dist;
	}

	public static CovarianceFunction getInstance() {
		if (instance == null)
			instance = new PeriodicCovarianceFunctionForOneDay();

		return instance;
	}

	public static void main(String args[]) {

		Matrix trainX = new Matrix(new double[][] { { 0.3067, 0.7635, 0.3171,
				0.1184, 0.6425, 0.1959, 0.9482, 0.9679 } });
		trainX = trainX.transpose();

		double[] loghyper = new double[] { 1, 3 };

		// temp = temp.times(Math.PI);
		// temp = temp.times(1/( Math.exp(loghyper[0])));

		// temp.print(10, 5);

	}

}