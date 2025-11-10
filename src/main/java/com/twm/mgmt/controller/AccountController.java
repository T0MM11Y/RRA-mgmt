package com.twm.mgmt.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.twm.mgmt.Enum.LogFormat;
import com.twm.mgmt.constant.RraConstants;
import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.model.account.AccountConditionVo;
import com.twm.mgmt.model.account.AccountVo;
import com.twm.mgmt.model.account.DepartmentConditionVo;
import com.twm.mgmt.model.account.DepartmentVo;
import com.twm.mgmt.model.account.PermissionVo;
import com.twm.mgmt.model.account.RoleVo;
import com.twm.mgmt.model.common.QueryResultVo;
import com.twm.mgmt.persistence.dto.AccountActionHistoryDto;
import com.twm.mgmt.persistence.entity.AccountEntity;
import com.twm.mgmt.persistence.entity.RoleEntity;
import com.twm.mgmt.service.AccountService;
import com.twm.mgmt.utils.CommonUtils;
import com.twm.mgmt.validator.account.AccountVoValidator;
import com.twm.mgmt.validator.account.DepartmentVoValidator;
import com.twm.mgmt.validator.account.PermissionVoValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/account")
@Controller
public class AccountController extends BaseController {

	private static final String FIND_ACCOUNT_LIST_URI = "/findAccount";
	private static final String ROLE_PERMISSION_URI = "/rolePermission";
	private static final String ACCOUNT_PERMISSION_URI = "/accountPermission";
	private static final String FIND_ROLE_MENU_URI = "/findRoleMenuList";
	private static final String FIND_ACCOUNT_MENU_URI = "/findAccountMenuList";
	private static final String SAVE_ROLE_PERMISSION_URI = "/saveRolePermission";
	private static final String SAVE_ACCOUNT_PERMISSION_URI = "/saveAccountPermission";
	private static final String ACCOUNT_SETTING_URI = "/setting";
	private static final String SAVE_ACCOUNT_SETTING_URI = "/saveAccountSetting";
	private static final String UPDATE_ACCOUNT_STATUS_URI = "/updateAccountStatus";
	private static final String FIND_DEPARTMENT_LIST_URI = "/findDepartment";
	private static final String DEPARTMENT_SETTING_URI = "/departmentSetting";
	private static final String SAVE_DEPARTMENT_SETTING_URI = "/saveDepartmentSetting";
	private static final String UPDATE_DEPARTMENT_STATUS_URI = "/updateDepartmentStatus";
	private static final String FIND_ACCOUNT_ACTION_HISTORY = "/findAccountActionHistory";

	private String commonFormat = LogFormat.CommonLog.getName();

	@Autowired
	private AccountService accountService;

	@GetMapping(FIND_ACCOUNT_LIST_URI)
	public ModelAndView findAccount() {
		ModelAndView mv = new ModelAndView("account/findAccount");
		mv.addObject("menudata", "6");
		mv.addObject("menuop", "account/findAccount");
		List<RoleVo> roleList = accountService.findRoleList();
		List<DepartmentVo> departmentList = accountService.findDepartmentList();
		mv.addObject("roleList", roleList);
		mv.addObject("departmentList", departmentList);
		return mv;
	}

	@PostMapping(FIND_ACCOUNT_LIST_URI)
	public ResponseEntity<?> findAccount(AccountConditionVo condition, HttpServletRequest request) {
		ActionType action = condition.getAction();
		HttpSession session = request.getSession();
		try {
			if (action.isQuery()) {
				session.setAttribute(RraConstants.QUERY_CONDITION, condition);
			} else {
				AccountConditionVo oriCondition = (AccountConditionVo) session
						.getAttribute(RraConstants.QUERY_CONDITION);
				accountService.copyQueryCondition(oriCondition, condition);
			}
			QueryResultVo result = accountService.findAccountList(condition);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("AccountController findAccount Error: {}", e.getMessage(), e);
			return getErrorResponse();
		}
	}

	@GetMapping(ROLE_PERMISSION_URI)
	public ModelAndView rolePermissionSetting() {
		ModelAndView mv = new ModelAndView("account/rolePermission");
		mv.addObject("menudata", "6");
		mv.addObject("menuop", "account/rolePermission");
		try {
			mv.addObject("action", ActionType.ADD.getCode());
			mv.addObject("roleList", accountService.findRoleList());
		} catch (Exception e) {
			log.error("AccountController rolePermissionSetting Error: {}", e.getMessage(), e);
		}
		return mv;
	}

	@GetMapping(ACCOUNT_PERMISSION_URI)
	public ModelAndView accountPermissionSetting() {
		ModelAndView mv = new ModelAndView("account/accountPermission");
		mv.addObject("menudata", "6");
		mv.addObject("menuop", "account/accountPermission");
		try {
			mv.addObject("accounts", accountService.findAccountList());
		} catch (Exception e) {
			log.error("AccountController accountPermissionSetting Error: {}", e.getMessage(), e);
		}
		return mv;
	}

	@PostMapping({ FIND_ROLE_MENU_URI, FIND_ACCOUNT_MENU_URI })
	public ModelAndView findMenuList(@RequestParam(name = "id", required = false) Long id, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("account/menuList :: permission_menu");
		try {
			String uri = request.getRequestURI();
			if (uri.indexOf(FIND_ROLE_MENU_URI) != -1) {
				mv.addObject("vos", accountService.findRoleMenu(id));
			} else {
				AccountEntity account = accountService.getAccountEntity(id);
				RoleEntity role = account.getRole();
				mv.addObject("roleName", account == null ? null : new RoleVo(role).getRoleName());
				mv.addObject("vos", accountService.findAccountMenu(id, role.getRoleId()));
			}
		} catch (Exception e) {
			log.error("AccountController findMenuList: {}", e.getMessage(), e);
		}
		return mv;
	}

	@PostMapping({ SAVE_ROLE_PERMISSION_URI, SAVE_ACCOUNT_PERMISSION_URI })
	public ResponseEntity<?> saveOrUpdatePermission(PermissionVo vo, HttpServletRequest request) {
		try {
			Map<String, List<String>> validErrors = CommonUtils.validate(Arrays.asList(new PermissionVoValidator(vo)));
			if (!validErrors.isEmpty()) {
				return getValidErrorResponse(validErrors);
			}
			accountService.saveOrUpdatePermission(vo);
		} catch (Exception e) {
			log.error("AccountController saveOrUpdatePermission: {}", e.getMessage(), e);
			return getErrorResponse();
		}
		return getSuccessResponse("儲存成功");
	}

	@RequestMapping(value = ACCOUNT_SETTING_URI, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView accountSetting(Long accountId, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("account/accountSetting");
		mv.addObject("menudata", "6");
		try {
			mv.addObject("vo", accountService.getAccount(accountId));
			mv.addObject("roleList", accountService.findRoleList());
			mv.addObject("departmentList", accountService.findDepartmentList());
			if (accountService.getAccount(accountId).getAccountId() == null)
				mv.addObject("menuop", "account/setting");
			else
				mv.addObject("menuop", "account/findAccount");
		} catch (Exception e) {
			mv.addObject("menuop", "account/setting");
			log.error("AccountController accountSetting: {}", e.getMessage(), e);
		}
		return mv;
	}

	@PostMapping(SAVE_ACCOUNT_SETTING_URI)
	public ResponseEntity<?> saveAccountSetting(AccountVo vo) {
		try {
			Map<String, List<String>> validErrors = CommonUtils.validate(Arrays.asList(new AccountVoValidator(vo)));
			if (!validErrors.isEmpty()) {
				return getValidErrorResponse(validErrors);
			}
			accountService.saveOrUpdateAccount(vo);
		} catch (Exception e) {
			log.error("AccountController saveAccountSetting: {}", e.getMessage(), e);
			return getErrorResponse();
		}
		return getSuccessResponse("儲存成功");
	}

	@PostMapping(UPDATE_ACCOUNT_STATUS_URI)
	public ResponseEntity<?> updateAccountStatus(Long accountId, String status, String requestId) {
		try {
			accountService.updateAccountStatus(accountId, status, requestId);
		} catch (Exception e) {
			log.error("AccountController updateAccountStatus: {}", e.getMessage(), e);
			return getErrorResponse();
		}
		return getSuccessResponse("更新成功");
	}

	@GetMapping(FIND_DEPARTMENT_LIST_URI)
	public ModelAndView findDepartment() {
		ModelAndView mv = new ModelAndView("account/findDepartment");
		mv.addObject("menudata", "6");
		mv.addObject("menuop", "account/findDepartment");
		return mv;
	}

	@PostMapping(FIND_DEPARTMENT_LIST_URI)
	public ResponseEntity<?> findDepartment(DepartmentConditionVo condition, HttpServletRequest request) {
		ActionType action = condition.getAction();
		HttpSession session = request.getSession();
		try {
			if (action.isQuery()) {
				session.setAttribute(RraConstants.QUERY_CONDITION, condition);
			} else {
				DepartmentConditionVo oriCondition = (DepartmentConditionVo) session
						.getAttribute(RraConstants.QUERY_CONDITION);
				accountService.copyQueryCondition(oriCondition, condition);
			}
			QueryResultVo result = accountService.findDepartmentList(condition);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("AccountController findDepartment Error: {}", e.getMessage(), e);
			return getErrorResponse();
		}
	}

	@PostMapping(DEPARTMENT_SETTING_URI)
	public ModelAndView departmentSetting(Long departmentId) {
		ModelAndView mv = new ModelAndView("account/departmentSetting");
		mv.addObject("menudata", "6");
		mv.addObject("menuop", "account/findDepartment");
		try {
			mv.addObject("vo", accountService.getDepartment(departmentId));
		} catch (Exception e) {
			log.error("AccountController departmentSetting Error: {}", e.getMessage(), e);
		}
		return mv;
	}

	@PostMapping(SAVE_DEPARTMENT_SETTING_URI)
	public ResponseEntity<?> saveDepartmentSetting(DepartmentVo vo) {
		try {
			Map<String, List<String>> validErrors = CommonUtils.validate(Arrays.asList(new DepartmentVoValidator(vo)));
			if (!validErrors.isEmpty()) {
				return getValidErrorResponse(validErrors);
			}
			accountService.saveOrUpdateDepartment(vo);
		} catch (Exception e) {
			log.error("AccountController saveDepartmentSetting: {}", e.getMessage(), e);
			return getErrorResponse();
		}
		return getSuccessResponse("儲存成功");
	}

	@PostMapping(UPDATE_DEPARTMENT_STATUS_URI)
	public ResponseEntity<?> updateDepartmentStatus(Long departmentId, String status) {
		try {
			accountService.updateDepartmentStatus(departmentId, status);
		} catch (Exception e) {
			log.error("AccountController updateDepartmentStatus: {}", e.getMessage(), e);
			return getErrorResponse();
		}
		return getSuccessResponse("更新成功");
	}

	@GetMapping(FIND_ACCOUNT_ACTION_HISTORY)
	public ModelAndView findAccountActionHistory() {
		ModelAndView mv = new ModelAndView("account/findAccountActionHistory");
		mv.addObject("menudata", "6");
		mv.addObject("menuop", "account/findAccountActionHistory");
		mv.addObject("executeAccountList", accountService.getExecuteAccountList(1L));
		mv.addObject("accountList", accountService.getAccountList());
		return mv;
	}

	@PostMapping(FIND_ACCOUNT_ACTION_HISTORY)
	public ResponseEntity<?> findAccountActionHistory(
			@RequestParam(value = "executeAccountId", defaultValue = "") Long executeAccountId,
			@RequestParam(value = "accountId", defaultValue = "") Long accountId,
			@RequestParam(value = "requestId", defaultValue = "") String requestId) {
		try {
			List<AccountActionHistoryDto> accountActionHistoryDto = accountService
					.getAccountActionHistoriesByCriteria(executeAccountId, accountId, requestId);
			return new ResponseEntity<>(accountActionHistoryDto, HttpStatus.OK);
		} catch (Exception e) {
			log.error("AccountController findAccountActionHistory Error: {}", e.getMessage(), e);
			return getErrorResponse();
		}
	}

}
