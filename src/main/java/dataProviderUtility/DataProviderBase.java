package dataProviderUtility;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.DataProvider;

import masterWrapper.CaptainOfTheShip;

public class DataProviderBase extends CaptainOfTheShip{

	public Object[][] getDataForDataProvider(String fileLocation, String sheetName) throws IOException {

//		String[] workbookParams = m.getAnnotation(dataProviderParams.class).value();
//		String fileName = workbookParams[0];
//		String sheetName = workbookParams[1];
//		String fileLocation = System.getProperty("user.dir")+"\\src\\main\\resources\\utilities\\dataProviderFiles\\"+fileName+".xlsx";

		Workbook workbook = WorkbookFactory.create(new File(fileLocation));
		Sheet sheet = workbook.getSheet(sheetName);
		Iterable<Row> rows = sheet::rowIterator;
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		boolean header = true;
		List<String> keys = null;

		for (Row row : rows) {
			List<String> values = getValuesInEachRow(row);
			if (header) {
				header = false;
				keys = values;
				continue;
			}
			results.add(transformToMap(keys, values));
		}
		workbook.close();
		return asTwoDimensionalArray(results);
	}

	private static List<String> getValuesInEachRow(Row row) {
		List<String> data = new ArrayList<String>();
		Iterable<Cell> columns = row::cellIterator;
		for (Cell column : columns) {
			data.add(new DataFormatter().formatCellValue(column));
		}
		return data;
	}

	private static Map<String, String> transformToMap(List<String> columnNames, List<String> values) {
		Map<String, String> results = new HashMap<String, String>();

		for (int i = 0; i < columnNames.size(); i++) {
			String key = columnNames.get(i);
			String value = values.get(i);
			results.put(key, value);
		}
		return results;
	}

	private static Object[][] asTwoDimensionalArray(List<Map<String, String>> listOfMaps) {
		Object[][] results = new Object[listOfMaps.size()][1];
		int index = 0;
		for (Map<String, String> interimResult : listOfMaps) {
			results[index++] = new Object[] {interimResult};
		}
		return results;
	}
}
