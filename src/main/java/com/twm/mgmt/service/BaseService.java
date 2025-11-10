package com.twm.mgmt.service;

import java.net.URI;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.twm.mgmt.constant.RraConstants;
import com.twm.mgmt.model.common.UserInfoVo;
import com.twm.mgmt.persistence.entity.AccountEntity;
import com.twm.mgmt.persistence.repository.AccountRepository;
import com.twm.mgmt.persistence.repository.DepartmentRepository;
import com.twm.mgmt.persistence.repository.RoleRepository;

public abstract class BaseService {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Value("${spring.profiles.active}")
	public String active;

	@Value("${rc.recieve.secrect.key}")
	public String rcScrectKey;

	@Value("${rc.recieve.secrect.iv}")
	public String rcScrectIv;

	@Value("${rra.mail.from}")
	public String fromEmail;

	@Autowired
	protected AccountRepository accountRepo;

	@Autowired
	protected RoleRepository roleRepo;

	@Autowired
	protected DepartmentRepository departmentRepo;

	public AccountEntity getAccountEntity(Long accountId) {
		if (accountId != null) {
			Optional<AccountEntity> optional = accountRepo.findById(accountId);

			if (optional.isPresent()) {

				return optional.get();
			}
		}

		return null;
	}

	/**
	 * 送簽主檔歷程
	 * 
	 * @param approval
	 * @param recordReason
	 */
	/**
	 * 申裝類型
	 * 
	 * @param aq
	 * @param np
	 * @param rt
	 * @param separator
	 * @return
	 */
	/**
	 * 使用者ID
	 * 
	 * @return
	 */
	public Long getAccountId() {

		return getUserInfo().getAccountId();
	}

	/**
	 * 使用者角色ID
	 * 
	 * @return
	 */
	public Long getRoleId() {

		return getUserInfo().getRoleId();
	}

	/**
	 * 使用者角色名稱
	 * 
	 * @return
	 */
	protected String getRoleName() {

		return getUserInfo().getRoleName();
	}

	/**
	 * 使用者角色
	 * 
	 * @return
	 */
	/**
	 * 使用者部門別
	 * 
	 * @return
	 */
	public Long getDepartmentId() {

		return getUserInfo().getDepartmentId();
	}

	/**
	 * 使用者BuTag
	 * 
	 * @return
	 */
	public String getBuTag() {

		return getUserInfo().getBuTag();
	}

	/**
	 * 取得使用者資訊
	 * 
	 * @return
	 */
	private UserInfoVo getUserInfo() {
		HttpSession session = (HttpSession) RequestContextHolder.getRequestAttributes()
				.resolveReference(RequestAttributes.REFERENCE_SESSION);

		return (UserInfoVo) session.getAttribute(RraConstants.USER_INFO);
	}

	protected String fullUrl(String path, HttpServletRequest request) {
		URI uri = URI.create(request.getRequestURL().toString());

		String scheme = uri.getScheme();

		String host = uri.getHost();

		int port = uri.getPort();

		if (port > 0) {

			return String.format("%s://%s:%s%s", scheme, host, port, path);
		}

		return String.format("%s://%s%s", scheme, host, path);
	}

}
