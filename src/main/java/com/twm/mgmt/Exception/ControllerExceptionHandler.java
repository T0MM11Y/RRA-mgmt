package com.twm.mgmt.Exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

import com.twm.mgmt.controller.BaseController;
import com.twm.mgmt.enums.RraErrorCode;
import com.twm.mgmt.model.common.RespVo;

@ControllerAdvice
public class ControllerExceptionHandler extends BaseController {
	
	@ExceptionHandler(AjaxException.class)
	public ResponseEntity<RespVo> handleRuntimeException(Exception e) {
		
		Map<String, List<String>> messages = new HashMap<String, List<String>>();
		List<String> stringList = new ArrayList<String>();
		stringList.add("系統發生錯誤，請聯繫系統管理員Yvette Yang。");
		messages.put("ErrorMessage", stringList);
		
		return getResponse(RraErrorCode.SYSTEM_ERROR, messages, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ModelAndViewException.class)
	public RedirectView handleRuntimeException2(Exception e,HttpServletRequest request) {
		
		return new RedirectView(fullUrl(RRA_ERROR_URI, request));
	}

}
