package uk.ac.soton.ecs.gp4j.bmc;

public class TestBasicPrior extends uk.ac.soton.ecs.gp4j.util.ExtendedTestCase {
	BasicPrior basicPrior1;

	BasicPrior basicPrior2;

	@Override
	protected void setUp() throws Exception {
		basicPrior1 = new BasicPrior(1, 5.0, 4.0, 0.25);
		basicPrior2 = new BasicPrior(5, 1.0, 0.5, 0.25);
	}

	public void testGetSamples() throws Exception {
		assertEquals(new double[] { 5 }, basicPrior1.getSamples());
		assertEquals(new double[] { 0.3679, 0.6065, 1.0000, 1.6487, 2.7183 },
				basicPrior2.getSamples(), 1e-4);
	}

	public void testGetLogSamples() throws Exception {
		assertEquals(new double[] { Math.log(5) }, basicPrior1.getLogSamples());
		assertEquals(new double[] { -1.0000, -0.5000, 0, 0.5000, 1.0000 },
				basicPrior2.getLogSamples(), 1e-4);
	}
}
