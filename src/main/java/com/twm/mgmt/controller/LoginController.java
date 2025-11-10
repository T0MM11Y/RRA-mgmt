package com.twm.mgmt.controller;

import java.text.MessageFormat;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.twm.mgmt.constant.RraConstants;
import com.twm.mgmt.enums.RraErrorCode;
import com.twm.mgmt.model.common.ReceiveInfoVo;
import com.twm.mgmt.model.common.RespVo;
import com.twm.mgmt.persistence.entity.ProgramEntity;
import com.twm.mgmt.service.LoginService;
import com.twm.mgmt.utils.JsonUtil;
import com.twm.mgmt.utils.StringUtilsEx;

@Controller
public class LoginController extends BaseController {

	@Autowired
	private LoginService service;

	private Encoder encode = ESAPI.encoder();

	@GetMapping(ROOT_URI)
	public ModelAndView index(String showalert) {
		ModelAndView mv = new ModelAndView("index");
		ProgramEntity programEntity = service.getShowProgramName(showalert);
		if (programEntity != null) {
			String programName = programEntity.getProgramName();
			mv.addObject("programName", programName);
		}

		return mv;
	}

	/**
	 * 載入相關資料，導到對應功能
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping(LOADING_URI)
	public RedirectView loadInfo(HttpServletRequest request, RedirectAttributes attrs) {
		HttpSession session = request.getSession();

		// load menu
		if (session.getAttribute(RraConstants.MENU_LIST) == null) {
			session.setAttribute(RraConstants.MENU_LIST, service.findMenuVo());
		}

		// redirect view
		if (session.getAttribute(RraConstants.RECEIVE_INFO) != null) {
			try {
				ReceiveInfoVo receiveInfoVo = (ReceiveInfoVo) session.getAttribute(RraConstants.RECEIVE_INFO);

				String approvalId = receiveInfoVo.getId();

				// ApprovalMainEntity entity =
				// approvalservice.getApprovalMainEntity(approvalId);
				//
				// if (entity != null) {
				// Integer level = receiveInfoVo.getLevel();
				//
				// Long accountId = approvalservice.getAccountId();
				//
				// // 檢核能否簽核該單號
				// if (!(level == 1 && accountId == entity.getL1Account()) && !(level == 2 &&
				// accountId == entity.getL2Account())) {
				// log.debug("Not Has Approval Permission...");
				//
				// attrs.addFlashAttribute("errorVo",
				// MessageFormat.format(RraErrorCode.APPROVAL_ACCOUNT_NOT_FOUND.getDesc(),
				// approvalId));
				//
				// return new RedirectView(fullUrl(RRA_ERROR_URI, request));
				// }
				//
				// ApprovalStatus status = ApprovalStatus.find(entity.getStatus());
				//
				// if (status.isUnreceived() || status.isDecline()) {
				// // 更新簽收
				// if (status.isUnreceived()) {
				// approvalservice.updateApprovalStatus(receiveInfoVo);
				// }
				//
				// return new RedirectView(fullUrl(String.format("%s%s",
				// RedirectController.REDIRECT_URI, RedirectController.GO_APPRV_URI), request));
				// } else {
				// // 待簽核清單
				//
				// //return new RedirectView(fullUrl(String.format("%s%s",
				// ApprovalController.APPROVAL_URI,
				// ApprovalController.FIND_WAIT_APPROVAL_LIST_URI), request));
				// }
				// }
			} catch (Exception e) {
				log.error("LoginController loadInfo updateApprovalStatus Error: {}", e.getMessage(), e);

				return new RedirectView(fullUrl(RRA_ERROR_URI, request));
			}

		}

		String redirectUrl = (String) request.getSession().getAttribute("redirectAfterLogin");
		if (redirectUrl != null && !redirectUrl.isEmpty()) {
			request.getSession().removeAttribute("redirectAfterLogin");
			return new RedirectView(redirectUrl);
		}

		return new RedirectView(fullUrl(ROOT_URI, request));
	}

	/**
	 * 錯誤頁
	 * 
	 * @param vo
	 * @return
	 */
	@GetMapping(RRA_ERROR_URI)
	public ModelAndView error(@ModelAttribute("errorVo") RespVo vo) {

		// checkmarx弱掃
		vo = JsonUtil.jsonToPojo(
				ESAPI.encoder().decodeForHTML(ESAPI.encoder().encodeForHTML(JsonUtil.objectToJson(vo))), RespVo.class);

		ModelAndView mv = new ModelAndView("error");

		if (StringUtilsEx.isBlank(encode.encodeForHTML(vo.getCode()))) {
			vo = getErrorResponse2().getBody();
		}

		mv.addObject("errorVo", vo);

		return mv;
	}

	/**
	 * 登出
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping(LOGOUT_URI)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		// ModelAndView mv = new ModelAndView("logout");

		HttpSession session = request.getSession();

		if (session != null) {
			session.invalidate();
		}

		return new ModelAndView("redirect:" + SsoController.SSO_URI + SsoController.LOGIN_URI);
	}

}
