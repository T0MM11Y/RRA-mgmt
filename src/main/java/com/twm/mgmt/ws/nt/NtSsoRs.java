package com.twm.mgmt.ws.nt;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@SuppressWarnings("serial")
public class NtSsoRs implements Serializable {

	/** 傳回碼(0成功 1失敗) */
	@XmlElement(name = "Code")
	private Integer code;

	/** 錯誤訊息 */
	@XmlElement(name = "ErrorMsg")
	private String errorMsg;

}
