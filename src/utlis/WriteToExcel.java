package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteToExcel {
	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet = workbook.createSheet("Results");
	
	public WriteToExcel(List<Object[]> results){
		int rowNum = 0;
		for(Object[] result: results) {
			Row row = sheet.createRow(rowNum++);
			int cellNum = 0;
			for(Object type : result) {
				Cell cell = row.createCell(cellNum++);
				if (type instanceof String) {
					cell.setCellValue((String) type);
				} else if (type instanceof Integer) {
					cell.setCellValue((Integer) type);
				}
			}
		}
		try {
			FileOutputStream outputFile = new FileOutputStream("Results.xlsx");
			workbook.write(outputFile);
			workbook.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}