package com.twm.mgmt.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twm.mgmt.interceptor.RraInterceptor;

@RestController
public class SessionController extends RraInterceptor {

	@GetMapping("/api/sessionTime")
	public boolean getSessionTime(HttpServletRequest request) {
		if (!isAuthorized(request)) {
			

			return false;
		}
		return true;
	}
}
