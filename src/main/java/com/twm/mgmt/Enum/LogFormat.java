package com.twm.mgmt.Enum;

public enum LogFormat {
	
	/** INFO 格式 **/
	CommonLog("{}|{}|{}|{}|{}|{}|{}|{}"),
	/** ERROR/Warn 格式 **/
	ErrorLog("{}|{}|{}|{}|{}|{}|{}"),
	/** ResponseLog 格式 **/
	ResponseLog("SESSION ID: {} | ResponseID: {} | SoueceId: {} | {}"),
	/** RequestSecretValidFilter 格式**/
	RequestSecretValidFilter("SESSION ID: {} | RequestID: {} | SoueceId: {} | {}"),
	/** RequestSecretDecryptFilter 格式**/
	RequestSecretDecryptFilter("SESSION ID: {} | RequestID: {} | SoueceId: {} | {} {} {}"),
	/** SQL ERROR **/
	SqlError("{}|{}|{}|{}|{}|{}|{}" ),
	/** DEBUG LOG**/
	DebugLog("{}|{}|{}|{}|{}|{}|{}|{}");
	
	private String name;
	
	LogFormat(String name) {
		this.name = name;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	@Override
	public String toString() {
		return this.name();
	}
}