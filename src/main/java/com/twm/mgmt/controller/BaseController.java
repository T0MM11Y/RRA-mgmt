package com.twm.mgmt.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.twm.mgmt.Enum.LogFormat;
import com.twm.mgmt.constant.RraConstants;
import com.twm.mgmt.enums.RraErrorCode;
import com.twm.mgmt.model.common.FileVo;
import com.twm.mgmt.model.common.MenuVo;
import com.twm.mgmt.model.common.RespVo;
import com.twm.mgmt.model.common.UserInfoVo;
import com.twm.mgmt.utils.DateUtilsEx;
import com.twm.mgmt.utils.MailUtils;


public abstract class BaseController {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected static final String ROOT_URI = "/";

	protected static final String LOADING_URI = "/loading";

	protected static final String LOGOUT_URI = "/logout";

	protected static final String ERROR_URI = "/error";

	protected static final String RRA_ERROR_URI = "/rra-error";

	protected static final String HC_ALIVE_URI = "/HC/alive";
	
protected static final List<String> excludedList = Arrays.asList("/css", "/js", "/img", "/favicon.ico", LOGOUT_URI, ERROR_URI, RRA_ERROR_URI, HC_ALIVE_URI, RedirectController.REDIRECT_URI, "/api/sessionTime");
	
	protected String commonFormat = LogFormat.CommonLog.getName();
	protected String errorFormat = LogFormat.ErrorLog.getName();
	
	protected SimpleDateFormat dateFormat() {
		return new SimpleDateFormat("yyyyMMdd");
	}
	
	@Autowired
	protected MailUtils mailUtils;

	/**
	 * 下載檔案
	 * 
	 * @param vo
	 * @param response
	 */
	protected void downloadFile(FileVo vo, HttpServletResponse response) {
		try {
			String fileName = URLEncoder.encode(vo.getFileName(), StandardCharsets.UTF_8.name());

			String contentType = StringUtils.isNotBlank(vo.getContentType()) ? vo.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;

			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8.name()), StandardCharsets.ISO_8859_1.name()));

			response.setContentType(contentType);

			OutputStream os = response.getOutputStream();

			os.write(vo.getFileContent());

			os.flush();

			os.close();
		} catch (IOException e) {
			log.error("BaseController downloadFile Error: {}", e.getMessage(), e);

		}
	}

	protected String fullUrl(String path, HttpServletRequest request) {
		URI uri = URI.create(request.getRequestURL().toString());

		String scheme = uri.getScheme();

		String host = uri.getHost();

		int port = uri.getPort();
		
		String showalertPath = "?showalert="+uri.getPath();

		if (port > 0) {

			return String.format("%s://%s:%s%s%s", scheme, host, port, path,showalertPath);
		}

		return String.format("%s://%s%s%s", scheme, host, path,showalertPath);
	}

	protected boolean isLocal(HttpServletRequest request) {
		URI uri = URI.create(request.getRequestURL().toString());

		String host = uri.getHost();

		return host.startsWith("127") || host.startsWith("localhost");
	}

	protected boolean isGet(HttpServletRequest request) {
		RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

		return RequestMethod.GET.compareTo(requestMethod) == 0;
	}

	protected boolean isPost(HttpServletRequest request) {
		RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

		return RequestMethod.POST.compareTo(requestMethod) == 0;
	}

	/**
	 * 系統錯誤
	 * 
	 * @return
	 */
	protected ResponseEntity<RespVo> getErrorResponse() {

		Map<String, List<String>> messages = new HashMap<String, List<String>>();
		List<String> stringList = new ArrayList<String>();
		stringList.add("系統發生錯誤，請聯繫系統管理員Yvette Yang。");
		messages.put("ErrorMessage", stringList);
		
		return getResponse(RraErrorCode.SYSTEM_ERROR, messages, HttpStatus.BAD_REQUEST);

	}
	
	/**
	 * 系統錯誤2
	 * 
	 * @return
	 */
	protected ResponseEntity<RespVo> getErrorResponse2() {
		return getResponse(RraErrorCode.SYSTEM_ERROR2, null, HttpStatus.BAD_REQUEST);

	}

	/**
	 * 帳號不存在
	 * 
	 * @return
	 */
	protected ResponseEntity<RespVo> getAccountNotFoundResponse() {

		return getResponse(RraErrorCode.ACCOUNT_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 檢核失敗
	 * 
	 * @param messages
	 * @return
	 */
	protected ResponseEntity<RespVo> getValidErrorResponse(Map<String, List<String>> messages) {

		return getResponse(RraErrorCode.VALIDATE_ERROR, messages, HttpStatus.OK);
	}

	/**
	 * 儲存成功
	 * 
	 * @return
	 */
	protected ResponseEntity<RespVo> getSuccessResponse(String message) {
		RespVo vo = RespVo.builder().code("000").message(message).dateTime(DateUtilsEx.formatDate(new Date())).build();

		return getResponse(vo, HttpStatus.OK);
	}

	/**
	 * 
	 * @param type
	 * @param messages
	 * @param status
	 * @return
	 */
	protected ResponseEntity<RespVo> getResponse(RraErrorCode type, Map<String, List<String>> messages, HttpStatus status) {
		RespVo vo = RespVo.builder().code(type.getCode()).message(type.getDesc()).errorMessages(messages).dateTime(DateUtilsEx.formatDate(new Date())).build();

		return getResponse(vo, status);
	}

	/**
	 * 
	 * @param vo
	 * @param status
	 * @return
	 */
	protected ResponseEntity<RespVo> getResponse(RespVo vo, HttpStatus status) {

		return new ResponseEntity<>(vo, status);
	}
	
	private List<MenuVo> getMenuList() {
		HttpSession session = (HttpSession) RequestContextHolder.getRequestAttributes().resolveReference(RequestAttributes.REFERENCE_SESSION);

		return (List<MenuVo>) session.getAttribute(RraConstants.MENU_LIST);
	}
	
	protected String getMenudata() {
		
		

		return Integer.toString((getMenuList().size() - 1));
	}

}
