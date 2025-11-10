package com.twm.mgmt.validator.account;

import java.util.List;
import java.util.Optional;

import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.enums.RraErrorCode;
import com.twm.mgmt.model.account.DepartmentVo;
import com.twm.mgmt.model.common.ErrorVo;
import com.twm.mgmt.persistence.entity.DepartmentEntity;
import com.twm.mgmt.persistence.repository.DepartmentRepository;
import com.twm.mgmt.utils.SpringUtils;
import com.twm.mgmt.utils.StringUtilsEx;
import com.twm.mgmt.validator.BaseValidator;

public class DepartmentVoValidator extends BaseValidator {

	private DepartmentVo vo;

	public DepartmentVoValidator(DepartmentVo vo) {
		this.vo = vo;
	}

	@Override
	public List<ErrorVo> validate() {
		Long departmentId = vo.getDepartmentId();

		if (departmentId == null) {
			addError("departmentId", RraErrorCode.IS_REQUIRED);
		} else {
			DepartmentRepository repo = SpringUtils.getBean(DepartmentRepository.class);

			Optional<DepartmentEntity> optional = repo.findById(departmentId);

			if (optional.isPresent()) {
				ActionType action = vo.getAction();

				if (action.isAdd()) {
					addError("departmentId", RraErrorCode.DEPARTMENT_ID_IS_EXIST);
				}
			}
		}

		String departmentName = vo.getDepartmentName();

		if (StringUtilsEx.isBlank(departmentName)) {
			addError("departmentName", RraErrorCode.IS_REQUIRED);
		} else {
			if (StringUtilsEx.hasUniqueSymbol(departmentName)) {
				addError("departmentName", RraErrorCode.INPUT_TEXT_UNIQUE_SYMBOL_ERROR);
			}
		}

		return getResult();
	}

}
