package com.twm.mgmt.constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class RraConstants {

	public static final String USER_INFO = "userInfo";

	public static final String MENU_LIST = "menuList";

	public static final String QUERY_CONDITION = "condition";

	public static final Integer AES_KEY_LEN = 32;

	public static final Integer AES_IV_LEN = 16;

	public static final String RECEIVE_INFO = "receiveInfo";

	public static final Integer DAYS = 31;

	public static final Integer HOURS = 24;

	public static final Integer MINUTES = 60;

	public static final Integer SECONDS = 60;

	public static final Integer MILLISECONDS = 1000;

	public static final String CAMPAIGNFILEPATH = File.separator + "home" + File.separator + "tpsacct" + File.separator
			+ "rraUpload" + File.separator + "campaignProject";

	public static final String SIGNFILEPATH = File.separator + "home" + File.separator + "tpsacct" + File.separator
			+ "rraUpload" + File.separator + "signOff" + File.separator + "campaignfiles"+File.separator+new SimpleDateFormat("yyyy").format(new Date());
	
	
	public static final String REPORTS = File.separator + "home" + File.separator + "tpsacct" + File.separator + "Reports" + File.separator + new SimpleDateFormat("yyyy").format(new Date()) ;

}
