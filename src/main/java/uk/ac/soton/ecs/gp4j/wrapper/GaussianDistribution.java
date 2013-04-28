/**
 * 
 */
package uk.ac.soton.ecs.gp4j.wrapper;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;

/**
 * This class represent the normal distribution of the data
 * The main purpose of this class is to facilitate the normalisation of data 
 * into and from Standard normal distribution.
 * @author Muddasser Alam ma2@ecs.soton.ac.uk
 *
 */
public class GaussianDistribution {

	double[] gDist = null, normDist=null;
	
	double sd=Double.NaN, mean=Double.NaN;
	
	
	public GaussianDistribution(double[] data) {
		gDist=data;	
		normDist=new double[data.length];
		
		sd = new StandardDeviation().evaluate(data);
		mean = new Mean().evaluate(data);
		
		for (int i = 0; i < data.length; i++) {
			normDist[i]= (data[i]-mean)/sd;
		}
		
	}

	public double getMean()
	{
		return mean;
	}

	public double getSD()
	{
		return sd;
	}
	
	public double[] getStandardNormalDistribuition() {
			
		return normDist;

	}
	
	public double[] toNormalDistribution(double[] standardData)
	{
		double[] normalData=new double[standardData.length];
		
		for (int i = 0; i < standardData.length; i++) {
			normalData[i]= (standardData[i]*sd)+mean;
		}			
		return normalData ;
	}





public static void main(String args[])
{
	double[] testData = {4,8,6,4,3,8,2,1,9,7,5,6,3,4};
	
	GaussianDistribution 	normalDistribution = new GaussianDistribution(testData);
	
	System.out.println(normalDistribution.getMean());
	System.out.println(normalDistribution.getSD());
	
	double[] s_data = normalDistribution.getStandardNormalDistribuition();
	
	for (double a:s_data)
		System.out.println(a);
	
	s_data = normalDistribution.toNormalDistribution(s_data);
	
	for (double a:s_data)
		System.out.println(a);
}
}