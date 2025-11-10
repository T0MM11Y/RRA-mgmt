package com.twm.mgmt.interceptor;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.twm.mgmt.constant.RraConstants;
import com.twm.mgmt.controller.BaseController;
import com.twm.mgmt.controller.RedirectController;
import com.twm.mgmt.controller.SsoController;
import com.twm.mgmt.model.common.MenuVo;
import com.twm.mgmt.model.common.UserInfoVo;

@Component
public class RraInterceptor extends BaseController implements HandlerInterceptor {

	//private static final List<String> IGNORE_URIS = Arrays.asList("/css", "/js", "/img", "/favicon.ico", LOGOUT_URI, ERROR_URI, RRA_ERROR_URI, HC_ALIVE_URI, RedirectController.REDIRECT_URI,GET_MOMOID_CHANGE_SMS);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		URI uri = URI.create(request.getRequestURL().toString());

		String path = uri.getPath();

		boolean isIgnore = excludedList.stream().anyMatch(ignore -> path.contains(ignore));

		if (!isIgnore) {
			log.debug("request isIgnore: {}, path: {}", isIgnore, path);
			
			if(!path.contains(SsoController.SSO_URI)&& !path.equals(ROOT_URI) && !path.contains(LOADING_URI)) {
			    String fullRequestedUrl = getFullURL(request);
			    log.debug(fullRequestedUrl);	   
			    request.getSession().setAttribute("redirectAfterLogin", fullRequestedUrl);	
			}
		}
 		

		if (!isIgnore) {
			if (path.contains(SsoController.SSO_URI)) {
				if (isAuthorized(request)) {
					response.sendRedirect(fullUrl(ROOT_URI, request));

					return false;
				}

				return true;
			}

			if (!isAuthorized(request)) {
				response.sendRedirect(fullUrl(String.format("%s%s", SsoController.SSO_URI, SsoController.LOGIN_URI), request));

				return false;
			}

			if (isGet(request)) {
				clearAttributes(request);

				if ((!path.equals(ROOT_URI) && !path.contains(LOADING_URI)) && !isPermission(request)) {
					response.sendRedirect(fullUrl(ROOT_URI, request));
				
					
					return false;
				}
			}
		}

		return true;
	}

	protected boolean isAuthorized(HttpServletRequest request) {
		HttpSession session = request.getSession();

		UserInfoVo userInfo = (UserInfoVo) session.getAttribute(RraConstants.USER_INFO);

		return userInfo != null && userInfo.getAccountId() != null;
	}

	/**
	 * 檢核使用者能否使用該功能的權限
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isPermission(HttpServletRequest request) {
		HttpSession session = request.getSession();

		String uri = request.getRequestURI();
		
		List<MenuVo> menuVos = (List<MenuVo>) session.getAttribute(RraConstants.MENU_LIST);

		return menuVos.stream().anyMatch(vo -> vo.getSubVos().stream().anyMatch(subVo -> uri.contains(subVo.getUrl())));
	}

	private void clearAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession();

		session.removeAttribute(RraConstants.QUERY_CONDITION);
	}

//	private void setAjaxStatus(HttpServletRequest request, HttpServletResponse response) {
//		String ajaxRq = request.getHeader("X-Requested-With");
//
//		log.debug("request AJAX: {}", ajaxRq);
//
//		if (StringUtils.isNotBlank(ajaxRq)) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		}
//	}
	
	private String getFullURL(HttpServletRequest request) {
	    StringBuffer requestURL = request.getRequestURL();
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}

}
