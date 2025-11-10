package com.twm.mgmt.controller;

import java.net.URLDecoder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.twm.mgmt.constant.RraConstants;
import com.twm.mgmt.model.common.ReceiveInfoVo;
import com.twm.mgmt.utils.AESUtil;
import com.twm.mgmt.utils.JsonUtil;

@RequestMapping(RedirectController.REDIRECT_URI)
@Controller
public class RedirectController extends BaseController {

	public static final String REDIRECT_URI = "/rc";

	public static final String APPRV_URI = "/apprv";

	public static final String GO_APPRV_URI = "/goapprv";
	
	private Encoder encode=ESAPI.encoder();

	@GetMapping(APPRV_URI)
	public RedirectView redirect(String parm, HttpServletRequest request) {
		HttpSession session = request.getSession();

		try {
			String decodeParm = URLDecoder.decode(parm, "UTF-8");

			//String decryptStr = AESUtil.decryptStr(decodeParm, approvalService.rcScrectKey, approvalService.rcScrectIv);

			//ReceiveInfoVo receiveInfo = JsonUtil.jsonToPojo(decryptStr, ReceiveInfoVo.class);

			//session.setAttribute(RraConstants.RECEIVE_INFO, receiveInfo);
		} catch (Exception e) {
			log.error("RedirectController redirect Error: {}", e.getMessage(), e);

			return new RedirectView(fullUrl(RRA_ERROR_URI, request));
		}

		return new RedirectView(fullUrl(LOADING_URI, request));
	}

	@GetMapping(GO_APPRV_URI)
	public ModelAndView toApproval(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("approval/toApproval");

		HttpSession session = request.getSession();

		ReceiveInfoVo vo = (ReceiveInfoVo) session.getAttribute(RraConstants.RECEIVE_INFO);

		//mv.addObject("url", fullUrl(String.format("%s%s", ApprovalController.APPROVAL_URI, ApprovalController.DO_APPROVAL_URI), request));

		mv.addObject("approvalId", encode.encodeForHTML(vo.getId()));

		return mv;
	}

}
