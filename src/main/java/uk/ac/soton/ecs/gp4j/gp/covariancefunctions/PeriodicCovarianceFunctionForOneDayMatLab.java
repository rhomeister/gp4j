package uk.ac.soton.ecs.gp4j.gp.covariancefunctions;

import Jama.Matrix;

public class PeriodicCovarianceFunctionForOneDayMatLab implements
		CovarianceFunction {

	// FOR ONE DIEMENSIONAL DATA ONLY

	private static PeriodicCovarianceFunctionForOneDayMatLab instance;

	public Matrix calculateCovarianceMatrix(double[] loghyper, Matrix trainX) {
		return calculateTrainTestCovarianceMatrix(loghyper, trainX, trainX);
	}

	// Java Code for matlab-repmat
	public static Matrix repmat(Matrix a) {

		int length = Math.max(a.getColumnDimension(), a.getRowDimension());
		Matrix b = new Matrix(length, length);

		for (int i = 0; i < a.getRowDimension(); i++) {
			for (int j = 0; j < b.getColumnDimension(); j++) {
				b.set(i, j, a.get(j, 0));
			}
		}
		return b;

	}

	public Matrix calculateTrainTestCovarianceMatrix(double[] loghyper,
			Matrix trainX, Matrix testX) {

		int samplesTrain = trainX.getRowDimension();
		int samplesTest = testX.getRowDimension();

		if (samplesTrain == 0 || samplesTest == 0)
			return new Matrix(samplesTrain, samplesTest);

		Matrix trainX_repmat = repmat(trainX);
		Matrix testX_repmat = repmat(testX).transpose();

		Matrix result = testX_repmat.minus(trainX_repmat);

		for (int i = 0; i < result.getRowDimension(); i++)
			for (int j = 0; j < result.getColumnDimension(); j++) {
				result.set(i, j, getPeriodicValue(result.get(i, j), loghyper));

			}

		return result;
	}

	public Matrix calculateTestCovarianceMatrix(double[] loghyper, Matrix testX) {
		return new Matrix(testX.getRowDimension(), 1, Math
				.exp(2 * loghyper[loghyper.length - 1]));
	}

	public int getHyperParameterCount(Matrix testX) {
		return testX.getColumnDimension() + 1;
	}

	public static PeriodicCovarianceFunctionForOneDayMatLab getInstance() {
		if (instance == null)
			instance = new PeriodicCovarianceFunctionForOneDayMatLab();

		return instance;
	}

	public static double getPeriodicValue(double value, double[] loghyper) {
		return (Math.exp(2 * loghyper[1]) * Math.exp(-2
				* Math.pow((Math.sin(value * Math.PI) / Math.exp(loghyper[0])),
						2)));
	}

	// public static CovarianceFunction getInstance() {
	// if (instance == null)
	// instance = new SquaredExponentialARDCovarianceFunction();
	//
	// return instance;
	// }

	public static void main(String args[]) {

		Matrix trainX = new Matrix(new double[][] { { 0.3067, 0.7635, 0.3171,
				0.1184, 0.6425, 0.1959, 0.9482, 0.9679 } });
		trainX = trainX.transpose();
		Matrix temp = repmat(trainX);

		temp = temp.transpose().minus(temp);

		double[] loghyper = new double[] { 1, 3 };

		// temp = temp.times(Math.PI);
		// temp = temp.times(1/( Math.exp(loghyper[0])));

		temp.print(10, 5);

	}

}
