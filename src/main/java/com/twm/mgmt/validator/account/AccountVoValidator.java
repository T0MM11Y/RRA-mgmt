package com.twm.mgmt.validator.account;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.enums.RraErrorCode;
import com.twm.mgmt.model.account.AccountVo;
import com.twm.mgmt.model.common.ErrorVo;
import com.twm.mgmt.persistence.entity.AccountEntity;
import com.twm.mgmt.persistence.repository.AccountRepository;
import com.twm.mgmt.utils.SpringUtils;
import com.twm.mgmt.utils.StringUtilsEx;
import com.twm.mgmt.validator.BaseValidator;

public class AccountVoValidator extends BaseValidator {

	private AccountVo vo;

	public AccountVoValidator(AccountVo vo) {
		this.vo = vo;
	}

	@Override
	public List<ErrorVo> validate() {
		String userName = vo.getUserName();

		if (StringUtilsEx.isBlank(userName)) {
			addError("userName", RraErrorCode.IS_REQUIRED);
		} else {
			if (StringUtilsEx.hasUniqueSymbol(userName)) {
				addError("userName", RraErrorCode.INPUT_TEXT_UNIQUE_SYMBOL_ERROR);
			}
		}

		String email = vo.getEmail();

		if (StringUtilsEx.isBlank(email)) {
			addError("email", RraErrorCode.IS_REQUIRED);
		} else {
			String regex = "^[a-zA-Z0-9_.+-]+@taiwanmobile.com+$";

			if (!email.matches(regex)) {
				addError("email", RraErrorCode.INPUT_TEXT_FORMAT_ERROR);
			} else {
				AccountRepository repo = SpringUtils.getBean(AccountRepository.class);

				List<AccountEntity> result = repo.findByEmail(email);

				if (CollectionUtils.isNotEmpty(result)) {
					ActionType action = vo.getAction();

					if (action.isAdd() || (action.isEdit() && !vo.getAccountId().equals(result.get(0).getAccountId()))) {
						addError("email", RraErrorCode.EMAIL_IS_EXIST);
					}
				}
			}
		}

		String mobile = vo.getMobile();

		if (StringUtilsEx.isNotBlank(mobile)) {
			String regex = "^[\\d]{10}+$";

			if (!mobile.matches(regex)) {
				addError("mobile", RraErrorCode.INPUT_TEXT_FORMAT_ERROR);
			}
		}

//		String unitNo = vo.getUnitNo();
//
//		if (StringUtilsEx.isBlank(unitNo)) {
//			addError("unitNo", RraErrorCode.IS_REQUIRED);
//		} else {
//			String regex = "^[a-zA-Z0-9-_]+$";
//
//			if (!unitNo.matches(regex)) {
//				addError("unitNo", RraErrorCode.INPUT_TEXT_FORMAT_ERROR);
//			}
//		}

		String approvable = vo.getApprovable();

		if (StringUtilsEx.isBlank(approvable)) {
			addError("approvable", RraErrorCode.IS_REQUIRED);
		}

		Long roleId = vo.getRoleId();

		if (roleId == null) {
			addError("roleId", RraErrorCode.IS_REQUIRED);
		}

		Long departmentId = vo.getDepartmentId();

		if (departmentId == null) {
			addError("departmentId", RraErrorCode.IS_REQUIRED);
		}
		
		String requestId = vo.getRequestId();
		if(StringUtils.isBlank(requestId)) {
			addError("requestId", RraErrorCode.IS_REQUIRED);
		}

		return getResult();
	}

}
