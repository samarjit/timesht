package com.ycs.fe.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ResultDTO {

private List<String> messages;
private List<String> errors;
private JSONObject data;

 

public ResultDTO() {
	errors = new ArrayList<String>();
	messages = new ArrayList<String>();
}
public List<String> getMessages() {
	return messages;
}
public void setMessages(List<String> messages) {
	this.messages = messages;
}
public List<String> getErrors() {
	return errors;
}
public void setErrors(List<String> errors) {
	this.errors = errors;
}
public JSONObject getData() {
	return data;
}
public void setData(JSONObject data) {
	this.data = data;
}

public void addError(String e){
	errors.add(e);
}

public void addMessage(String m){
	messages.add(m);
}

public String toString(){
	return "MESSAGE: from toString()";//+messages+",ERRORS:"+errors+",DATA:"+data;
}

}
