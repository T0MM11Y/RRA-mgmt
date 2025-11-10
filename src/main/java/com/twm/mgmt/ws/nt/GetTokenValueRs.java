package com.twm.mgmt.ws.nt;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@XmlRootElement(name = "XML")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("serial")
public class GetTokenValueRs extends NtSsoRs {

	/** 登入帳號(英文名稱) */
	@XmlElement(name = "UserID")
	private String userId;

	/** 中文姓名 */
	@XmlElement(name = "UserName")
	private String userName;

	/** 公司名稱 */
	@XmlElement(name = "Company")
	private String company;

	/** Email */
	@XmlElement(name = "Email")
	private String email;

	/** 公司代碼 1:台灣大哥大 3:企業用戶事業群 6:台灣客服 */
	@XmlElement(name = "CompanyID")
	private Integer companyId;

	/** 行動電話 */
	@XmlElement(name = "Mobile")
	private String mobile;

	/** 電話號碼 */
	@XmlElement(name = "TelephoneNumber")
	private String telephoneNumber;

	/** 辦公室 */
	@XmlElement(name = "OfficeName")
	private String officeName;

	/** HR 員工編號 */
	@XmlElement(name = "EmployeeID")
	private String employeeId;

	/** 使用者類型 1:正職 2:非正職 */
	@XmlElement(name = "UserType")
	private Integer userType;

	/** 部門名稱 */
	@XmlElement(name = "Department")
	private String department;

	/** 職稱 */
	@XmlElement(name = "Title")
	private String title;

	/** HR 部門代碼 */
	@XmlElement(name = "HRDeptID")
	private String hrDeptId;

}
