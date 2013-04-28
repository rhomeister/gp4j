package uk.ac.soton.ecs.gp4j.example;

import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import uk.ac.soton.ecs.gp4j.bmc.BasicPrior;
import uk.ac.soton.ecs.gp4j.bmc.GaussianProcessMixture;
import uk.ac.soton.ecs.gp4j.bmc.GaussianProcessRegressionBMC;
import uk.ac.soton.ecs.gp4j.gp.GaussianProcess;
import uk.ac.soton.ecs.gp4j.gp.GaussianProcessPrediction;
import uk.ac.soton.ecs.gp4j.gp.GaussianProcessRegression;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.NoiseCovarianceFunction;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SquaredExponentialCovarianceFunction;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SumCovarianceFunction;
import uk.ac.soton.ecs.gp4j.util.MatrixUtils;

public class GuassianProcessRegressionExampleOriginal {

	public static void main(String[] args) {
		// normalGPExample();

		learningGPExample();

	}

	private static void learningGPExample() {
		GaussianProcessRegressionBMC regression = new GaussianProcessRegressionBMC();
		regression.setCovarianceFunction(new SumCovarianceFunction(
				SquaredExponentialCovarianceFunction.getInstance(),
				NoiseCovarianceFunction.getInstance()));

		regression.setPriors(new BasicPrior(11, 1.0, 2.0), new BasicPrior(11,
				1.0, 2.0), new BasicPrior(1, .01, 1.0));

		double[] trainX = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
		double[] trainY = new double[] { 1.0, 1.0, 1.0, 1.5, .5, 0.0 };
		GaussianProcessMixture predictor = regression.calculateRegression(
				trainX, trainY);

		Map<Double[], Double> hyperParameterWeights = regression
				.getHyperParameterWeights();

		for (Double[] hyperparameterValue : hyperParameterWeights.keySet()) {
			System.out.println("hyperparameter value: "
					+ ArrayUtils.toString(hyperparameterValue) + ", weight: "
					+ hyperParameterWeights.get(hyperparameterValue));
		}

		double[] testX = new double[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0,
				8.0, 9.0, 10.0 };

		GaussianProcessPrediction prediction = predictor
				.calculatePrediction(MatrixUtils.createColumnVector(testX));

		prediction.getMean().print(10, 10);
		prediction.getVariance().print(10, 10);

	}

	private static void normalGPExample() {
		GaussianProcessRegression regression = new GaussianProcessRegression(
				new double[] { 0.0, 0.0 }, SquaredExponentialCovarianceFunction
						.getInstance());

		double[] trainX = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
		double[] trainY = new double[] { 1.0, 4.0, 3.0, 7.0, 6.0, 5.0 };

		GaussianProcess predictor = regression.calculateRegression(trainX,
				trainY);

		double[] testX = new double[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0,
				8.0, 9.0, 10.0 };

		GaussianProcessPrediction prediction = predictor
				.calculatePrediction(MatrixUtils.createColumnVector(testX));

		prediction.getMean().print(10, 10);
		prediction.getVariance().print(10, 10);
	}
}
