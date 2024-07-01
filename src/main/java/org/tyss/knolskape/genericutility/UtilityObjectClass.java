package org.tyss.knolskape.genericutility;

public class UtilityObjectClass {
	private String clientName;
	private String cycleId;
	private static UtilityObjectClass instance;

	private UtilityObjectClass() {

	}

	public static UtilityObjectClass getInstance() {
		if(instance == null) {
			instance = new UtilityObjectClass();
			return instance;
		}else {
			return instance;
		}
	}

	public String getClientName() {
		return clientName;
	}

	public String getCycleId() {
		return cycleId;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setCycleId(String cycleId) {
		this.cycleId = cycleId;
	}

}
