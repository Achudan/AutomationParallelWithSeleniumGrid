package propertyReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import masterWrapper.CaptainOfTheShip;

public class PropertyFileController extends CaptainOfTheShip {

	public Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\utilities\\propertyFiles\\"+fileName+".properties");
			prop = new Properties();
			prop.load(fis);
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}

	public String readPropertiesFile(String fileName, String keyAttribute) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		String returnValue = null;
		try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\utilities\\propertyFiles\\"+fileName+".properties");
			prop = new Properties();
			prop.load(fis);
			returnValue = prop.getProperty(keyAttribute);

		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return returnValue;
	}

	public void deletePropertyFileIfExists(String fileName) {
		try {
			File file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\utilities\\propertyFiles\\"+fileName+".properties");
			Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void createDynamicProprtyFile(String fileName) {
		try {
			File newFile = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\utilities\\propertyFiles\\"+fileName+".properties");
			if (newFile.createNewFile()) {
				System.out.println("File created: " + newFile.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void writeIntoPropertyFile(String fileName, Properties props) {
		try {
			String path = System.getProperty("user.dir")+"\\src\\main\\resources\\utilities\\propertyFiles\\"+fileName+".properties";
			FileOutputStream outputStream = new FileOutputStream(path);
			props.store(outputStream, "This is a property file which contains listeners and testng xml suite file data");
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void appendIntoPropertyFile(String fileName, String key, String value) {
		FileOutputStream fileOut = null;
		FileInputStream fileIn = null;
		try {
			String path = System.getProperty("user.dir")+"\\src\\main\\resources\\utilities\\propertyFiles\\"+fileName+".properties";
			Properties configProperty = new Properties();
			File file = new File(path);
			fileIn = new FileInputStream(file);
			configProperty.load(fileIn);
			configProperty.setProperty(key, value);
			fileOut = new FileOutputStream(file);
			configProperty.store(fileOut, "sample properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				fileIn.close();
				fileOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
