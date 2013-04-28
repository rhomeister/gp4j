package uk.ac.soton.ecs.gp4j.gp.covariancefunction;

import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.CovarianceFunction;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.NoiseCovarianceFunction;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SquaredExponentialCovarianceFunction;
import uk.ac.soton.ecs.gp4j.gp.covariancefunctions.SumCovarianceFunction;
import uk.ac.soton.ecs.gp4j.util.ExtendedTestCase;
import Jama.Matrix;

public class TestSumCovarianceFunction extends ExtendedTestCase {

	CovarianceFunction function1;
	CovarianceFunction function2;
	private SumCovarianceFunction sumCovarianceFunction;
	private double[] loghyper;
	private SumCovarianceFunction sumCovarianceFunctionReverse;
	private Matrix x;
	private double[] loghyperReverse;

	@Override
	protected void setUp() throws Exception {
		function1 = SquaredExponentialCovarianceFunction.getInstance();
		function2 = NoiseCovarianceFunction.getInstance();
		loghyper = new double[] { 1, 2, 3 };
		loghyperReverse = new double[] { 3, 1, 2 };

		sumCovarianceFunction = new SumCovarianceFunction(function1, function2);
		sumCovarianceFunctionReverse = new SumCovarianceFunction(function2,
				function1);
		x = new Matrix(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, 10);
	}

	public void testHyperParameterCount() throws Exception {
		assertEquals(3, sumCovarianceFunction
				.getHyperParameterCount(new Matrix(1, 2)));
	}

	public void testGetTestCovarianceMatrix() throws Exception {
		Matrix covMatrix1 = function1.calculateCovarianceMatrix(loghyper, x);
		Matrix covMatrix2 = function2.calculateCovarianceMatrix(
				new double[] { loghyper[2] }, x);

		Matrix sumMatrix = sumCovarianceFunction.calculateCovarianceMatrix(
				loghyper, x);

		assertEquals(covMatrix1.plus(covMatrix2), sumMatrix);
	}

	public void testEquivalence() throws Exception {
		assertEquals(sumCovarianceFunction.calculateCovarianceMatrix(loghyper,
				x), sumCovarianceFunctionReverse.calculateCovarianceMatrix(
				loghyperReverse, x));
	}
}
