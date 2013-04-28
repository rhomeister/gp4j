package uk.ac.soton.ecs.gp4j.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import uk.ac.soton.ecs.gp4j.gp.GaussianProcess;
import uk.ac.soton.ecs.gp4j.gp.GaussianProcessPrediction;
import uk.ac.soton.ecs.gp4j.gp.GaussianProcessRegression;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.CovarianceFunction;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.CovarianceFunctionFactory;
import uk.ac.soton.ecs.gp4j.wrapper.FixedWindowRegression;
import Jama.Matrix;

public class SpaceTimeGaussianProcessSampler {
	private double[] logHyper;
	private CovarianceFunction function;
	private double[] xArray;
	private double[] yArray;
	private double[] tArray;
	private PrintWriter writer;

	public SpaceTimeGaussianProcessSampler(String fileName, double[] logHyper,
			CovarianceFunction function, double[] xArray, double[] yArray,
			double[] tArray) throws FileNotFoundException {
		this.writer = new PrintWriter(fileName);
		this.logHyper = logHyper;
		this.function = function;
		this.xArray = xArray;
		this.yArray = yArray;
		this.tArray = tArray;
	}

	public static void main(String[] args) throws FileNotFoundException {

		CovarianceFunction function = CovarianceFunctionFactory
				.getNoisy2DTimeSquaredExponentialCovarianceFunction();

		int sampleCount = 11;
		double[] xArray = ArrayUtils.linspace(0, 15, sampleCount);
		double[] yArray = ArrayUtils.linspace(0, 15, sampleCount);
		double[] tArray = ArrayUtils.linspace(0, 40, 31);

		SpaceTimeGaussianProcessSampler spaceTimeGaussianProcessSampler = new SpaceTimeGaussianProcessSampler(
				"shortest_ts_short_ls.txt", ArrayUtils.log(new double[] { 3, 5,
						1, 0.01 }), function, xArray, yArray, tArray);
		spaceTimeGaussianProcessSampler.execute();

		spaceTimeGaussianProcessSampler = new SpaceTimeGaussianProcessSampler(
				"shortest_ts_shortest_ls.txt", ArrayUtils.log(new double[] { 2,
						5, 1, 0.01 }), function, xArray, yArray, tArray);
		spaceTimeGaussianProcessSampler.execute();

		// spaceTimeGaussianProcessSampler.setFileName("long_ts_short_ls");
		// spaceTimeGaussianProcessSampler.setLogHyper(new double[] { 2, 15, 1,
		// 0.01 });
		// spaceTimeGaussianProcessSampler.execute();
		//
		// spaceTimeGaussianProcessSampler.setFileName("short_ts_short_ls");
		// spaceTimeGaussianProcessSampler.setLogHyper(new double[] { 2, 5, 1,
		// 0.01 });
		// spaceTimeGaussianProcessSampler.execute();
		//
		// spaceTimeGaussianProcessSampler.setFileName("short_ts_long_ls");
		// spaceTimeGaussianProcessSampler.setLogHyper(new double[] { 5, 5, 1,
		// 0.01 });
		// spaceTimeGaussianProcessSampler.execute();
	}

	public void setFileName(String fileName) throws FileNotFoundException {
		this.writer = new PrintWriter(fileName);

	}

	public void setLogHyper(double[] logHyper) {
		this.logHyper = logHyper;
	}

	private void execute() {
		Matrix testX = new Matrix(
				xArray.length * yArray.length * tArray.length, 3);

		int i = 0;

		for (double t : tArray) {
			for (double x : xArray) {
				for (double y : yArray) {
					testX.set(i, 0, x);
					testX.set(i, 1, y);
					// t
					testX.set(i, 2, t);
					i++;
				}
			}
		}

		// Matrix mean = GaussianProcessUtils
		// .drawSample(function, logHyper, testX);

		// printPredictions(writer, testX, mean);

		GaussianProcessRegression gpRegression = new GaussianProcessRegression(
				logHyper, function);

		FixedWindowRegression<GaussianProcess> fixedWindowRegression = new FixedWindowRegression<GaussianProcess>(
				gpRegression, 200);

		GaussianProcess gp = fixedWindowRegression.calculateRegression(
				new Matrix(0, 3), new Matrix(0, 1));

		GaussianProcessPrediction prediction = gp.calculatePrediction(testX,
				true);

		Matrix sample = drawSamples(prediction);
		printPredictions(writer, testX, sample);

		// for (int t = 0; t < 100; t++) {
		//
		// GaussianProcessPrediction prediction = gp.calculatePrediction(
		// testX, true);
		//
		// Matrix sample = drawSamples(prediction);
		//
		// printPredictions(writer, testX, sample);
		//
		// gp = fixedWindowRegression.updateRegression(testX, sample);
		//
		// Matrix newTemporalCoordinates = new Matrix(testX.getRowDimension(),
		// 1, t + 1);
		// testX.setMatrix(0, testX.getRowDimension() - 1, 2, 2,
		// newTemporalCoordinates);
		// }

		writer.flush();
		writer.close();

	}

	private static Matrix drawSamples(GaussianProcessPrediction prediction) {
		Matrix drawSample = GaussianProcessUtils.drawSample(prediction
				.getCovarianceMatrix(), prediction.getTestX());

		return drawSample.plus(prediction.getMean());

		// prediction.getCovarianceMatrix().print(10, 10);

		// drawSample.print(10, 10);
	}

	private static void printPredictions(PrintWriter writer, Matrix testX,
			Matrix mean) {
		int rows = mean.getRowDimension();
		Matrix m = new Matrix(rows, 4);
		m.setMatrix(0, rows - 1, 0, 2, testX);
		m.setMatrix(0, rows - 1, 3, 3, mean);

		m.print(writer, 10, 10);
		writer.flush();
	}
}
