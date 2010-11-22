package dto;

public class PrepstmtDTO {
//public static String STRING="STRING";
//public static String INT="INT";
//public static String DATE="DATE";
//public static String DOUBLE="DOUBLE";
//public static String FLOAT="FLOAT";
	
public PrepstmtDTO(DataType type,String data){
	this.type = type;
	this.data = data;
}
public DataType type;
	public String getData() {
	return data;
}
public void setData(String data) {
	this.data = data;
}

	public static enum DataType {
		STRING,INT,DATE,FLOAT,DOUBLE
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	
public String data;

}
