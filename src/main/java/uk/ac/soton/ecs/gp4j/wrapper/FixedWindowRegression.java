package uk.ac.soton.ecs.gp4j.wrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import uk.ac.soton.ecs.gp4j.gp.GaussianPredictor;
import uk.ac.soton.ecs.gp4j.gp.GaussianRegression;
import Jama.Matrix;

public class FixedWindowRegression<T extends GaussianPredictor<?>> implements
		GaussianRegression<T> {

	private static Log log = LogFactory.getLog(FixedWindowPredictor.class);

	private GaussianRegression<T> regression;

	private int windowSize;

	public FixedWindowRegression(GaussianRegression<T> regression,
			int windowSize) {
		this.regression = regression;
		this.windowSize = windowSize;
	}

	public FixedWindowRegression() {

	}

	public void reset() {
		regression.reset();
	}

	public T calculateRegression(Matrix trainX, Matrix trainY) {
		return regression.calculateRegression(trainX, trainY);
	}

	public T downdateRegression() {
		return regression.downdateRegression();
	}

	public T downdateRegression(int i) {
		return regression.downdateRegression(i);
	}

	public GaussianPredictor<?> getCurrentPredictor() {
		return regression.getCurrentPredictor();
	}

	public Matrix getTrainX() {
		return regression.getTrainX();
	}

	public Matrix getTrainY() {
		return regression.getTrainY();
	}

	public int getTrainingSampleCount() {
		return regression.getTrainingSampleCount();
	}

	public GaussianRegression<T> shallowCopy() {
		return new FixedWindowRegression<T>(regression.shallowCopy(),
				windowSize);
	}

	public GaussianRegression<T> copy() {
		return new FixedWindowRegression<T>(regression.copy(), windowSize);
	}

	public T updateRegression(Matrix addedTrainX, Matrix addedTrainY) {
		return updateRegression(addedTrainX, addedTrainY, true);
	}

	public T updateRegression(Matrix addedTrainX, Matrix addedTrainY,
			boolean downDate) {
		T result = regression.updateRegression(addedTrainX, addedTrainY);

		log.debug("Current training sample count "
				+ regression.getTrainingSampleCount() + " Window size "
				+ windowSize);

		if (downDate && regression.getTrainingSampleCount() > windowSize) {
			log.debug("Downdating regression");

			result = regression.downdateRegression(regression
					.getTrainingSampleCount()
					- windowSize);
		}

		return result;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	@Required
	public void setRegression(GaussianRegression<T> regression) {
		this.regression = regression;
	}

}
