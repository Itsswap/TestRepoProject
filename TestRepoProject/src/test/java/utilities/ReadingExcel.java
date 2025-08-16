package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadingExcel {

	public static List<String[]> readLoginData(String filePath) {

		List<String[]> dataList = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheetAt(0);

			// Skip header row (row 0)
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);

				if (row != null && row.getCell(1) != null && row.getCell(2) != null) {
					String username = row.getCell(1).getStringCellValue();
					String password = row.getCell(2).getStringCellValue();
					dataList.add(new String[] { username, password });
				}
			}

		} catch (IOException e) {
			throw new RuntimeException("❌ Failed to read Excel file at: " + filePath, e);
		}

		return dataList;
	}
}
