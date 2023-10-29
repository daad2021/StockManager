package application;

public enum StockLevel {
	
	NONE(0), lIMITED(2), LOW(8), MEDIUM(13), HIGH(20);
	
	int number;
	
	StockLevel(int number){
		this.number = number;
	}
}
