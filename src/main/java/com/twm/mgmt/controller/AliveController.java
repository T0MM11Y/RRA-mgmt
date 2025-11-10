package com.twm.mgmt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AliveController extends BaseController {

	@RequestMapping(value = HC_ALIVE_URI)
	public String checkAlive() {

		return "O";
	}

}
