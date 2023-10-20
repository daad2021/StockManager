package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.ObservableList;

public class ExcelService {
	
private String fileName;
	
	// Constructor
	ExcelService(String name){

		if (!name.equals("") && (name.indexOf(".xlsx") > 0)) {
			this.fileName = name ;
		}
		else if (!name.equals("") && (name.indexOf(".xlsx") < 1)){
			this.fileName = name + ".xlsx";
		}
		else {
			Message.showErrorAlert("You did not enter a file name. Please check and try again.");
		}
	}
	
	// Get the file name
	public String getFileName() {
		return fileName;
	}
	
	// 
	public void writeToFile(ObservableList<DailySales> list) throws IOException {
		try {
			// Create workbook
			XSSFWorkbook workBook = new XSSFWorkbook();
			
			// Create sheet
			XSSFSheet sheet = workBook.createSheet("Report");
			
			// Create row object
			XSSFRow row;
			
			// Creating the header row
			String[] header = {"Name", "Brand", "Category", "Opening Stock", "Quantity Sold", "Closing Stock", "Total Sales", "Date"};
			
			int rowId = 0;
			row = sheet.createRow(rowId);
			
			for (int i=0; i<header.length; i++) {
				row.createCell(i).setCellValue(header[i]);
			}
			
			for (int i=0; i<list.size(); i++) {
				int cellId = 0;
				row = sheet.createRow(++rowId);
				row.createCell(cellId++).setCellValue(list.get(i).getItemName());
				row.createCell(cellId++).setCellValue(list.get(i).getBrand());
				row.createCell(cellId++).setCellValue(list.get(i).getCategory());
				row.createCell(cellId++).setCellValue(list.get(i).getTotalStock());
				row.createCell(cellId++).setCellValue(list.get(i).getQuantitySold());
				row.createCell(cellId++).setCellValue(list.get(i).getRemainingStock());
				row.createCell(cellId++).setCellValue(list.get(i).getTotalSales());
				row.createCell(cellId++).setCellValue(list.get(i).getTransDate());
			}
			
			Path filePath = Path.of(this.getFileName());
			FileOutputStream outFile = new FileOutputStream(filePath.toFile());
			workBook.write(outFile);
			outFile.close();
			workBook.close();
			Message.showInfoAlert("Report successfully exported.");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
