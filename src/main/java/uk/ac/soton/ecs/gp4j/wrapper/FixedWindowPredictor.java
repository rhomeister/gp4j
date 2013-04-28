package uk.ac.soton.ecs.gp4j.wrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import uk.ac.soton.ecs.gp4j.gp.GaussianPrediction;
import uk.ac.soton.ecs.gp4j.gp.GaussianPredictor;
import uk.ac.soton.ecs.gp4j.gp.GaussianRegression;
import Jama.Matrix;

public class FixedWindowPredictor<T extends GaussianPredictor<?>> {

	private static Log log = LogFactory.getLog(FixedWindowPredictor.class);

	private GaussianRegression<T> regression;
	private int windowSize;

	// private T predictor;

	public FixedWindowPredictor(GaussianRegression<T> regression, int windowSize) {
		this.regression = regression;
		this.windowSize = windowSize;
	}

	public FixedWindowPredictor() {

	}

	@Required
	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	@Required
	public void setRegression(GaussianRegression<T> regression) {
		this.regression = regression;
	}

	public void addTrainingPoints(Matrix trainX, Matrix trainY) {
		regression.updateRegression(trainX, trainY);

		if (log.isDebugEnabled())
			log.debug("Current training sample count "
					+ regression.getTrainingSampleCount() + " Window size: "
					+ windowSize);

		if (regression.getTrainingSampleCount() > windowSize) {
			log.debug("Downdating regression");
			regression.downdateRegression(regression.getTrainingSampleCount()
					- windowSize);
		}
	}

	public GaussianPrediction getPrediction(Matrix testX) {
		return regression.getCurrentPredictor().calculatePrediction(testX);
	}

	public Matrix getTrainX() {
		return regression.getTrainX();
	}

	public Matrix getTrainY() {
		return regression.getTrainY();
	}

	public int getWindowSize() {
		return windowSize;
	}

	public GaussianRegression<T> copy() {
		return regression.copy();
	}

	public GaussianRegression<T> shallowCopy() {
		return regression.shallowCopy();
	}

	public GaussianRegression<T> getChildRegression() {
		return regression;
	}

	public GaussianPredictor<?> getCurrentPredictor() {
		return regression.getCurrentPredictor();
	}
}
