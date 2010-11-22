package dto;

import java.io.Serializable;
import java.util.HashMap;

public class ApplicationDTO implements Serializable {
private String appid;
private String workflowName;
private String currStage;
private String currentAction;
private int currentActionId;
private HashMap<String,Integer> wflactions =null;

public HashMap<String, Integer> getWflactions() {
	return wflactions;
}

public void setWflactions(HashMap<String, Integer> wflactions) {
	this.wflactions = wflactions;
}

public int getCurrentActionId() {
	return currentActionId;
}

public void setCurrentActionId(int i) {
	this.currentActionId = i;
}

private long wflid;

public long getWflid() {
	return wflid;
}

public void setWflid(long wflid2) {
	this.wflid = wflid2;
}

public String getAppid() {
	return appid;
}

public void setAppid(String appid) {
	this.appid = appid;
}

public String getCurrentAction() {
	return currentAction;
}

public void setCurrentAction(String currentAction) {
	this.currentAction = currentAction;
}

public String getWorkflowName() {
	return workflowName;
}

public void setWorkflowName(String workflowName) {
	this.workflowName = workflowName;
}

public String getCurrStage() {
	return currStage;
}

public void setCurrStage(String currStage) {
	this.currStage = currStage;
}

}
