package utilities;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dataBaseConnection.AtlasMongoDBConnection;
import masterWrapper.CaptainOfTheShip;
import propertyReader.PropertyFileController;

public class SuiteListenerClass extends CaptainOfTheShip implements ISuiteListener{

	public void onStart(ISuite suite) {	
		System.out.println("Suite executed onStart"  + suite.getName());
		System.out.println("getName"  + suite.getName());
		System.out.println("getOutputDirectory"  + suite.getOutputDirectory());
		System.out.println("getParentModule"  + suite.getParentModule());
		System.out.println(suite.getXmlSuite().getFileName());

		List<XmlTest> tests = suite.getXmlSuite().getTests();
		Set<String> testCaseClassNames = new HashSet<String>(); 
		for(XmlTest test : tests) {
			List<XmlClass> classes = test.getClasses();
			for(XmlClass aclass: classes) {
				testCaseClassNames.add(aclass.getName());
				System.out.println(aclass.getName());
			}
		}
		System.out.println(testCaseClassNames);


		String xmlFileNameWithFormat = suite.getXmlSuite().getFileName();
		String[] xmlFile = xmlFileNameWithFormat .split("\\\\");
		String xmlFileName = xmlFile[xmlFile.length-1].trim();
		xmlFileName = xmlFileName.substring(0, xmlFileName.length()-4).trim();


		String parallelMode = getParallelExecutionMode(xmlFileNameWithFormat);

		writeIntoListenerPropertyFile(parallelMode);
		appendIntoListenerPropertyFile("classesCount", String.valueOf(testCaseClassNames.size()));

		mongoDBConnection = new AtlasMongoDBConnection();
		mongoDBConnection.createCollection(xmlFileName,testCaseClassNames);

	}
	private void writeIntoListenerPropertyFile(String parallelMode) {
		// TODO Auto-generated method stub

		Properties props= new Properties();
		props.put("parallelMode", parallelMode);

		String fileName = "listernerAndTestngXmlProp";
		PropertyFileController propFileController = new PropertyFileController();
		propFileController.deletePropertyFileIfExists(fileName);
		propFileController.createDynamicProprtyFile(fileName);
		propFileController.writeIntoPropertyFile(fileName, props);
	}
	private void appendIntoListenerPropertyFile(String key, String value) {
		// TODO Auto-generated method stub

		Properties props= new Properties();
		props.put(key, value);

		String fileName = "listernerAndTestngXmlProp";
		PropertyFileController propFileController = new PropertyFileController();
		propFileController.appendIntoPropertyFile(fileName, key, value);
	}
	private String getParallelExecutionMode(String xmlFileNameWithFormat) {
		// TODO Auto-generated method stub
		String parallelMethodName = null;
		try {
			File fXmlFile = new File(xmlFileNameWithFormat);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("test");
			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					parallelMethodName = eElement.getAttribute("parallel");
					System.out.println("parallel method name: "+ parallelMethodName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parallelMethodName;
	}
	public void onFinish(ISuite suite) {
		System.out.println("Suite executed onFinish"  + suite.getName());
	}
}
