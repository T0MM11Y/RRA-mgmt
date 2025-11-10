package com.twm.mgmt.aspect;

import java.util.Date;
import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.model.account.AccountVo;
import com.twm.mgmt.persistence.entity.AccountActionHistoryEntity;
import com.twm.mgmt.persistence.entity.AccountEntity;
import com.twm.mgmt.persistence.entity.DepartmentEntity;
import com.twm.mgmt.persistence.entity.RoleEntity;
import com.twm.mgmt.persistence.repository.AccountActionHistoryRepository;
import com.twm.mgmt.service.BaseService;

@Aspect
@Component
public class AccountActionHistoryAspect extends BaseService {

	private static final String AUTO_DISABLE_REQUEST_ID = "AUTO_DISABLE_90_DAYS";

	private final AccountActionHistoryRepository accountActionHistoryRepo;

	@Autowired
	public AccountActionHistoryAspect(AccountActionHistoryRepository accountActionHistoryRepo) {
		this.accountActionHistoryRepo = accountActionHistoryRepo;
	}

	@AfterReturning("execution(* com.twm.mgmt.service.AccountService.saveOrUpdateAccount(..))")
	public void afterSaveOrUpdateAccount(JoinPoint joinPoint) {
		AccountVo vo = (AccountVo) joinPoint.getArgs()[0];
		ActionType action = vo.getAction();
		if (action == null || action.isUnknown()) {
			return;
		}

		Long targetAccountId = resolveAccountId(vo, action);
		if (targetAccountId == null) {
			return;
		}

		String roleName = roleRepo.findById(vo.getRoleId()).map(RoleEntity::getRoleName).orElse("-");
		String deptName = departmentRepo.findById(vo.getDepartmentId()).map(DepartmentEntity::getDepartmentName)
				.orElse("-");
		String content = action.isAdd()
				? String.format("[Account Created] approvable=%s; role=%s; department=%s", vo.getApprovable(), roleName,
						deptName)
				: String.format("[Account Updated] approvable=%s; role=%s; department=%s", vo.getApprovable(),
						roleName, deptName);

		saveHistoryEntry(vo.getRequestId(), targetAccountId, content, getAccountId());
	}

	@AfterReturning("execution(* com.twm.mgmt.service.AccountService.updateAccountStatus(..))")
	public void afterUpdateAccountStatus(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		Long accountId = (Long) args[0];
		String status = (String) args[1];
		String requestId = (String) args[2];

		String userName = accountRepo.findById(accountId).map(AccountEntity::getUserName).orElse("-");
		String content = String.format("[Account Status Updated] user=%s; status=%s", userName, status);

		saveHistoryEntry(requestId, accountId, content, getAccountId());
	}

	@AfterReturning("execution(* com.twm.mgmt.service.AlertFor90dayLoginService.disableAccount(..))")
	public void afterDisableAccount(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		if (args.length == 0 || !(args[0] instanceof Number)) {
			return;
		}

		Long accountId = ((Number) args[0]).longValue();
		String userName = accountRepo.findById(accountId).map(AccountEntity::getUserName).orElse("-");
		String content = String.format("[Account Disabled - 90 day policy] user=%s; status=N", userName);

		saveHistoryEntry(AUTO_DISABLE_REQUEST_ID, accountId, content, 0L);
	}

	private Long resolveAccountId(AccountVo vo, ActionType action) {
		if (action.isAdd()) {
			return accountRepo.findByEmail(vo.getEmail()).stream().findFirst().map(AccountEntity::getAccountId)
					.orElse(null);
		}

		return vo.getAccountId();
	}

	private void saveHistoryEntry(String requestId, Long accountId, String content, Long executorId) {
		if (accountId == null) {
			return;
		}

		AccountActionHistoryEntity entity = AccountActionHistoryEntity.builder().requestId(requestId)
				.accountId(accountId).executeAccountId(Optional.ofNullable(executorId).orElse(0L)).executeDate(new Date())
				.executeContent(content).build();

		accountActionHistoryRepo.save(entity);
	}
}
