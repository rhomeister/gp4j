package uk.ac.soton.ecs.gp4j.gp.covariancefunctions;

//The wrapper class for PeriodicCovarainceFunctionForOneDay
//FOR ONE-DIMENSIONAL DATA ONLY

import java.util.Arrays;

import Jama.Matrix;

public class PeriodicCovarianceFunction implements CovarianceFunction {

	private PeriodicCovarianceFunctionForOneDay function = new PeriodicCovarianceFunctionForOneDay();

	private static PeriodicCovarianceFunction instance;

	protected PeriodicCovarianceFunction() {
	}

	public static PeriodicCovarianceFunction getInstance() {
		if (instance == null)
			instance = new PeriodicCovarianceFunction();

		return instance;
	}

	public Matrix calculateCovarianceMatrix(double[] loghyper, Matrix trainX) {
		return function.calculateCovarianceMatrix(translateHyperParameters(
				loghyper, trainX.getColumnDimension()), trainX);
	}

	public Matrix calculateTrainTestCovarianceMatrix(double[] loghyper,
			Matrix trainX, Matrix testX) {
		return function
				.calculateTrainTestCovarianceMatrix(translateHyperParameters(
						loghyper, trainX.getColumnDimension()), trainX, testX);
	}

	public Matrix calculateTestCovarianceMatrix(double[] loghyper, Matrix testX) {
		return function.calculateTestCovarianceMatrix(translateHyperParameters(
				loghyper, testX.getColumnDimension()), testX);
	}

	private double[] translateHyperParameters(double[] loghyper,
			int inputDimension) {
		double[] result = new double[inputDimension + 1];
		Arrays.fill(result, loghyper[0]);
		result[result.length - 1] = loghyper[1];
		return result;
	}

	public int getHyperParameterCount(Matrix testX) {
		return 2;
	}
}
