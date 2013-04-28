package uk.ac.soton.ecs.gp4j.gp.covariancefunctions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import Jama.Matrix;

public class HPConfigReader {

	private static Map<String, CovarianceFunction> sensorToCovTable = new HashMap<String, CovarianceFunction>();
	private static long configFileLastModified = 0;

	public static CovarianceFunction checkClassName(String className) {

		String packageName = "uk.ac.soton.ecs.gp4j.gp.covariancefunctions.";

		Class abc;

		try {
			abc = Class.forName(packageName + className);
			CovarianceFunction covFunc = (CovarianceFunction) abc.newInstance();
			Matrix testX = new Matrix(2, 2);

			System.out.println(covFunc.getHyperParameterCount(testX));
			return covFunc;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String args[]) {
		String sensorType = "Tide Height";
		// sensorType = "Air Temperature";
		loadUpdatedConfigFile();

		CovarianceFunction covFunc = getCovarianceFunctionForThisSensor(sensorType);

		System.out.println(covFunc);
		checkClassName("PeriodicCovarianceFunction");
		checkClassName("SquaredExponentialCovarianceFunction");
		checkClassName("NoiseCovarianceFunction");

	}

	public static CovarianceFunction getCovarianceFunctionForThisSensor(
			String sensorType) {

		File configFile = new File("CovConfig.xml");

		long last = configFile.lastModified();
		System.out.println(last);

		if (last != configFileLastModified) {
			System.out.println("Covariance Config File has changed");
			loadUpdatedConfigFile();
		}

		return sensorToCovTable.get(sensorType);
	}

	public static void loadUpdatedConfigFile() {
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			File configFile = new File("CovConfig.xml");

			Document doc = docBuilder.parse(configFile);
			// normalize text representation
			doc.getDocumentElement().normalize();

			System.out.println("Root element of the doc is "
					+ doc.getDocumentElement().getNodeName());

			NodeList listOfSensorTypes = doc
					.getElementsByTagName("sensorTypes");
			int totalTypes = listOfSensorTypes.getLength();

			System.out.println("Total no of sensorTypes : " + totalTypes);

			for (int s = 0; s < listOfSensorTypes.getLength(); s++) {

				Node firstSensorType = listOfSensorTypes.item(s);

				if (firstSensorType.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstSensorType;

					// -------
					NodeList typeNameList = firstElement
							.getElementsByTagName("typeName");
					Element typeNameElement = (Element) typeNameList.item(0);

					NodeList textList = typeNameElement.getChildNodes();

					String sensorType = ((Node) textList.item(0))
							.getNodeValue().trim();

					System.out.println("Type Name : "
							+ ((Node) textList.item(0)).getNodeValue().trim());

					// -------
					NodeList numberOfCovList = firstElement
							.getElementsByTagName("NumberOfCovFunctions");
					Element numberOfCovElement = (Element) numberOfCovList
							.item(0);
					textList = numberOfCovElement.getChildNodes();
					int numberOfCovFunctions = Integer
							.parseInt(((Node) textList.item(0)).getNodeValue()
									.trim());

					System.out.println("NumberOfCovFunctions : "
							+ numberOfCovFunctions);
					// ----
					// ------

					NodeList nodeList = firstElement
							.getElementsByTagName("covFunction");
					CovarianceFunction[] namesOfCovFunctions = new CovarianceFunction[numberOfCovFunctions];

					Element covElement;
					NodeList covList;

					for (int i = 0; i < numberOfCovFunctions; i++) {
						covElement = (Element) nodeList.item(i);
						covList = covElement.getChildNodes();

						CovarianceFunction temp = checkClassName(((Node) covList
								.item(0)).getNodeValue().trim());
						if (temp != null)
							namesOfCovFunctions[i] = temp;
					}

					covList = firstElement
							.getElementsByTagName("TypeOfCovariancesContribution");
					covElement = (Element) covList.item(0);

					textList = covElement.getChildNodes();
					String contributionCovFun = ((Node) textList.item(0))
							.getNodeValue().trim();

					for (int i = 0; i < namesOfCovFunctions.length; i++) {
						System.out.println(namesOfCovFunctions[i]);
					}

					System.out.println(contributionCovFun);

					CovarianceFunction toReturn = null;

					if (contributionCovFun.equalsIgnoreCase("Sum")) {
						sensorToCovTable.put(sensorType,
								new SumCovarianceFunction(
										PeriodicCovarianceFunction
												.getInstance(),
										SquaredExponentialCovarianceFunction
												.getInstance(),
										NoiseCovarianceFunction.getInstance()));

					}

				}
			}
		}

		catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

}