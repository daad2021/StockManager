package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.ObservableList;

public class CreateFile {
	private String fileName;
	
	// Constructor
	CreateFile(String name){
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
		String current = sdf1.format(new Date());
		if (name.indexOf(".txt") > 0) {
			this.fileName = name + current;
		}
		else {
			this.fileName = name + current + ".txt";
		}
	}
	
	// Get the file name
	public String getFileName() {
		return this.fileName;
	}
	

	// Write to a txt file
	public void writeToFile(ObservableList<Cart> itemList) throws IOException {
		PrintWriter printWriter = null;
		try {
			File file = new File(this.getFileName());
			Path filePath = Path.of(this.getFileName());
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeStamp = sdf2.format(new Date());
			if (file.createNewFile()) {
				FileWriter fileWriter = new FileWriter(filePath.toFile());
				printWriter = new PrintWriter(fileWriter);
				String dateAndTime = "Date/Time:";
				String transactionNumber = "Trans Num:";
				String col1 = "Item";
				String col2 = "Unit price";
				String col3 = "Qty";
				String col4 = "Total price";
				String invoiceTotalLabel = "Invoice Total:";
				double invoiceTotal = 0.0;
				printWriter.printf("%-20s %-15s %-10s %-10s %n", col1, col2, col3, col4);
				printWriter.println("------------------------------------------------------------");
				for(Cart item : itemList ) {
					printWriter.printf("%-20s %-15s %-10s %-10s %n",
							item.getName(),
							item.getUnitPrice(),
							item.getQuantity(),
							item.getTotalPrice());
					invoiceTotal = item.getInvoiceTotal();
				}
//				printWriter.println("------------------------------------------------------------");
				printWriter.printf("%45s %7s %n", invoiceTotalLabel, invoiceTotal);
				printWriter.println("------------------------------------------------------------");
				printWriter.println();
				printWriter.printf("%-1s %2s %n", dateAndTime, timeStamp);
				printWriter.printf("%-1s %2s %n", transactionNumber, SalesPage.getTransNo());
				System.out.println("Writing to " + file.getName() + " was successful.");
				System.out.println("File path: " + filePath.toAbsolutePath());
				Message.showInfoAlert("Invoice successfully generated.");
			}
			else {
				System.out.println("The file " + file.getName() + " already exist.");
			}
		}catch(IOException e) {
			e.printStackTrace();
			Message.showErrorAlert("Error!: Could not generate the invoice.");
		}
		finally {
			if (printWriter != null) {
				printWriter.close();
			}			
		}
	}
}
