package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelData {

	public static FileInputStream file;
	public static String[][] excelData;

	public ReadExcelData() {

		try {
			file = new FileInputStream(new File("./src/test/resources/TestData/Load.xlsx"));
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheetAt(0);

			// storing the data from excel file to Map
			int i = 0;
			int rowNum = sheet.getLastRowNum();
			int cellNum = sheet.getRow(0).getPhysicalNumberOfCells();
			excelData = new String[rowNum][cellNum];
			// Reading the data in
			for (Row row : sheet) {
				int j = 0;
				if (row.getRowNum() == 0) {
					continue;
				}
				for (Cell cell : row) {
					switch (cell.getCellType()) {
					case STRING:
						excelData[i][j] = cell.getRichStringCellValue().getString();
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							excelData[i][j] = cell.getDateCellValue() + "";
						} else {
							excelData[i][j] = cell.getNumericCellValue() + "";
						}
						break;
					case BOOLEAN:
						excelData[i][j] = cell.getBooleanCellValue() + "";

						break;
					case FORMULA:
						excelData[i][j] = cell.getCellFormula() + "";
						break;

					default:
					}
					j++;
				}
				i++;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}