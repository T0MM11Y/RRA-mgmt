package com.twm.mgmt.validator.account;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.enums.RraErrorCode;
import com.twm.mgmt.model.account.PermissionVo;
import com.twm.mgmt.model.common.ErrorVo;
import com.twm.mgmt.persistence.entity.RoleEntity;
import com.twm.mgmt.persistence.repository.RoleRepository;
import com.twm.mgmt.utils.SpringUtils;
import com.twm.mgmt.utils.StringUtilsEx;
import com.twm.mgmt.validator.BaseValidator;

public class PermissionVoValidator extends BaseValidator {

	private PermissionVo vo;

	public PermissionVoValidator(PermissionVo vo) {
		this.vo = vo;
	}

	@Override
	public List<ErrorVo> validate() {
		ActionType action = ActionType.find(vo.getAction());

		if (action.isAdd()) {
			if (StringUtilsEx.isBlank(vo.getRoleName())) {

				addError("roleName", RraErrorCode.IS_REQUIRED);
			} else {
				String roleName = vo.getRoleName();

				if (roleName.length() > 20) {

					addError("roleName", RraErrorCode.INPUT_TEXT_OVER_MAXLENGTH);
				}

				String regex = "^[A-Z_]+$";

				if (!roleName.matches(regex)) {

					addError("roleName", RraErrorCode.INPUT_TEXT_FORMAT_ERROR);
				}

				RoleRepository repo = SpringUtils.getBean(RoleRepository.class);

				RoleEntity entity = repo.getByRoleName(String.format("ROLE_%s", roleName));

				if (entity != null) {
					addError("roleName", RraErrorCode.ROLE_IS_EXIST);
				}

			}
		} else if (action.isEdit()) {
			if (vo.getRoleId() == null) {

				addError("roleId", RraErrorCode.IS_REQUIRED);
			}
		}

		if (action.isAdd() || action.isEdit()) {
			List<List<String>> onPrograms = vo.getOnPrograms();

			if (CollectionUtils.isEmpty(onPrograms)) {
				addError("permission", RraErrorCode.PERMISSION_EMPTY_ERROR);
			}
		}

		return getResult();
	}

}
