package uk.ac.soton.ecs.gp4j.gp.covariancefunction;

import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.CovarianceFunction;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.CovarianceFunctionFactory;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SquaredExponentialARDCovarianceFunction;
import uk.ac.soton.ecs.gp4j.util.ExtendedTestCase;
import Jama.Matrix;

public class Test2DTimeSquaredExponentialCovarianceFunction extends
		ExtendedTestCase {
	private CovarianceFunction function;
	private CovarianceFunction reference;
	private double[] logHyper;
	private double[] referenceLogHyper;

	@Override
	protected void setUp() throws Exception {
		function = CovarianceFunctionFactory
				.get2DTimeSquaredExponentialCovarianceFunction();
		reference = SquaredExponentialARDCovarianceFunction.getInstance();

		logHyper = new double[] { 1.0, 2.0, 3.0 };
		referenceLogHyper = new double[] { 1.0, 1.0, 2.0, 3.0 };
	}

	public void testCovarianceMatrix() throws Exception {
		Matrix mat = Matrix.random(10, 3);

		Matrix actual = function.calculateCovarianceMatrix(logHyper, mat);
		Matrix expected = reference.calculateCovarianceMatrix(
				referenceLogHyper, mat);

		assertEquals(expected, actual);

	}
}
