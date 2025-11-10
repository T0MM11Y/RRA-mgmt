package com.twm.mgmt.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.model.account.AccountConditionVo;
import com.twm.mgmt.model.account.AccountResultVo;
import com.twm.mgmt.model.account.AccountVo;
import com.twm.mgmt.model.account.DepartmentConditionVo;
import com.twm.mgmt.model.account.DepartmentResultVo;
import com.twm.mgmt.model.account.DepartmentVo;
import com.twm.mgmt.model.account.PermissionVo;
import com.twm.mgmt.model.account.RoleVo;
import com.twm.mgmt.model.common.MenuVo;
import com.twm.mgmt.model.common.QueryResultVo;
import com.twm.mgmt.model.common.SubMenuVo;
import com.twm.mgmt.persistence.dto.AccountActionHistoryDto;
import com.twm.mgmt.persistence.dto.AccountDto;
import com.twm.mgmt.persistence.entity.AccountEntity;
import com.twm.mgmt.persistence.entity.AccountPermissionProgramEntity;
import com.twm.mgmt.persistence.entity.DepartmentEntity;
import com.twm.mgmt.persistence.entity.MenuEntity;
import com.twm.mgmt.persistence.entity.ProgramEntity;
import com.twm.mgmt.persistence.entity.RoleEntity;
import com.twm.mgmt.persistence.entity.RolePermissionProgramEntity;
import com.twm.mgmt.persistence.repository.AccountActionHistoryRepository;
import com.twm.mgmt.persistence.repository.AccountPermissionProgramRepository;
import com.twm.mgmt.persistence.repository.MenuRepository;
import com.twm.mgmt.persistence.repository.RolePermissionProgramRepository;
import com.twm.mgmt.persistence.repository.RoleRepository;
import com.twm.mgmt.utils.StringUtilsEx;

@Service
public class AccountService extends BaseService {

	private final RoleRepository roleRepo;
	private final MenuRepository menuRepo;
	private final RolePermissionProgramRepository rolePermissionProgramRepo;
	private final AccountPermissionProgramRepository accountPermissionProgramRepo;
	private final AccountActionHistoryRepository accountActionHistoryRepo;

	@Autowired
	public AccountService(RoleRepository roleRepo, MenuRepository menuRepo,
			RolePermissionProgramRepository rolePermissionProgramRepo,
			AccountPermissionProgramRepository accountPermissionProgramRepo,
			AccountActionHistoryRepository accountActionHistoryRepo) {
		this.roleRepo = roleRepo;
		this.menuRepo = menuRepo;
		this.rolePermissionProgramRepo = rolePermissionProgramRepo;
		this.accountPermissionProgramRepo = accountPermissionProgramRepo;
		this.accountActionHistoryRepo = accountActionHistoryRepo;
	}

	@Transactional(readOnly = true)
	public List<RoleVo> findRoleList() {
		return roleRepo.findAll().stream().map(RoleVo::new).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<AccountVo> findAccountList() {
		return accountRepo.findEnabledAccount().stream().map(AccountVo::new).collect(Collectors.toList());
	}

	public void copyQueryCondition(AccountConditionVo oriCondition, AccountConditionVo newCondition) {
		newCondition.setRoleId(oriCondition.getRoleId());
		newCondition.setDepartmentId(oriCondition.getDepartmentId());
	}

	@Transactional(readOnly = true)
	public QueryResultVo findAccountList(AccountConditionVo condition) {
		QueryResultVo resultVo = new QueryResultVo(condition);

		List<AccountDto> dtos = accountRepo.findByCondition(condition);
		Integer total = accountRepo.countByCondition(condition);

		List<AccountResultVo> result = dtos.stream().map(AccountResultVo::new).collect(Collectors.toList());

		resultVo.setTotal(total);
		resultVo.setResult(result);
		return resultVo;
	}

	public AccountVo getAccount(Long accountId) {
		AccountVo vo = new AccountVo();

		if (accountId == null) {
			vo.setAction(ActionType.ADD);
			vo.setApprovable("N");
			return vo;
		}

		AccountEntity entity = getAccountEntity(accountId);
		if (entity != null) {
			vo = new AccountVo(entity);
			vo.setApprovable(entity.getApprovable());
			vo.setAction(ActionType.EDIT);
		}

		return vo;
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdateAccount(AccountVo vo) throws Exception {
		ActionType action = vo.getAction();
		if (action == null || action.isUnknown()) {
			throw new IllegalArgumentException("Unsupported account action");
		}

		Date now = new Date();
		AccountEntity entity;

		if (action.isAdd()) {
			entity = new AccountEntity();
			entity.setCreateDate(now);
			entity.setCreateAccount(getAccountId());
			entity.setEnabled("Y");
		} else {
			entity = accountRepo.findById(vo.getAccountId())
					.orElseThrow(() -> new IllegalArgumentException("Account not found: " + vo.getAccountId()));
			entity.setUpdateDate(now);
			entity.setUpdateAccount(getAccountId());
		}

		String email = vo.getEmail();
		if (StringUtilsEx.isBlank(email) || !email.contains("@")) {
			throw new IllegalArgumentException("Email format is invalid");
		}

		entity.setUserId(email.substring(0, email.indexOf("@")));
		entity.setUserName(vo.getUserName());
		entity.setEmail(email);
		entity.setMobile(vo.getMobile());
		entity.setRoleId(vo.getRoleId());
		entity.setDepartmentId(vo.getDepartmentId());
		entity.setApprovable(StringUtilsEx.isNotBlank(vo.getApprovable()) ? vo.getApprovable() : "N");

		accountRepo.save(entity);
	}

	@Transactional
	public void updateAccountStatus(Long accountId, String status, String requestId) {
		accountRepo.updateEnabledByAccountId(accountId, status, getAccountId());
	}

	public void copyQueryCondition(DepartmentConditionVo oriCondition, DepartmentConditionVo newCondition) {
		newCondition.setDepartmentName(oriCondition.getDepartmentName());
	}

	@Transactional(readOnly = true)
	public QueryResultVo findDepartmentList(DepartmentConditionVo condition) {
		QueryResultVo resultVo = new QueryResultVo(condition);

		List<DepartmentEntity> entities = departmentRepo.findByCondition(condition);
		Integer total = departmentRepo.countByCondition(condition);

		List<DepartmentResultVo> result = entities.stream().map(DepartmentResultVo::new).collect(Collectors.toList());

		resultVo.setTotal(total);
		resultVo.setResult(result);
		return resultVo;
	}

	@Transactional(readOnly = true)
	public List<DepartmentVo> findDepartmentList() {
		return departmentRepo.findEnabledDepartment().stream().map(DepartmentVo::new).collect(Collectors.toList());
	}

	public DepartmentVo getDepartment(Long departmentId) {
		DepartmentVo vo = new DepartmentVo();

		if (departmentId == null) {
			vo.setAction(ActionType.ADD);
			return vo;
		}

		Optional<DepartmentEntity> optional = departmentRepo.findById(departmentId);
		if (optional.isPresent()) {
			vo = new DepartmentVo(optional.get());
			vo.setAction(ActionType.EDIT);
		} else {
			vo.setAction(ActionType.ADD);
		}

		return vo;
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdateDepartment(DepartmentVo vo) throws Exception {
		ActionType action = vo.getAction();
		if (action == null || action.isUnknown()) {
			throw new IllegalArgumentException("Unsupported department action");
		}

		Date now = new Date();
		DepartmentEntity entity;

		if (action.isAdd()) {
			entity = new DepartmentEntity();
			entity.setCreateDate(now);
			entity.setEnabled("Y");
		} else {
			entity = departmentRepo.findById(vo.getDepartmentId()).orElseThrow(
					() -> new IllegalArgumentException("Department not found: " + vo.getDepartmentId()));
			entity.setUpdateDate(now);
		}

		entity.setDepartmentId(vo.getDepartmentId());
		entity.setDepartmentName(vo.getDepartmentName());

		departmentRepo.save(entity);
	}

	@Transactional
	public void updateDepartmentStatus(Long departmentId, String status) {
		departmentRepo.updateEnabledByDepartmentId(departmentId, status);
	}

	@Transactional(readOnly = true)
	public List<MenuVo> findRoleMenu(Long roleId) {
		List<MenuVo> menus = composeMenu();

		if (roleId == null) {
			return menus;
		}

		Set<Long> roleProgramIds = rolePermissionProgramRepo.findByRoleId(roleId).stream()
				.map(RolePermissionProgramEntity::getProgramId).collect(Collectors.toSet());

		menus.forEach(menu -> menu.getSubVos().forEach(sub -> {
			Long programId = sub.getProgramId();
			sub.setPermission(roleProgramIds.contains(programId) ? Boolean.TRUE : null);
		}));

		return menus;
	}

	@Transactional(readOnly = true)
	public List<MenuVo> findAccountMenu(Long accountId, Long roleId) {
		List<MenuVo> menus = composeMenu();

		Set<Long> roleProgramIds = roleId == null ? new HashSet<>()
				: rolePermissionProgramRepo.findByRoleId(roleId).stream()
						.map(RolePermissionProgramEntity::getProgramId).collect(Collectors.toSet());

		menus.forEach(menu -> menu.getSubVos().forEach(sub -> {
			Long programId = sub.getProgramId();
			sub.setStatusName(roleProgramIds.contains(programId) ? "Enabled" : "Disabled");
		}));

		if (accountId == null) {
			return menus;
		}

		accountPermissionProgramRepo.findByAccountId(accountId).forEach(permission -> menus.stream()
				.flatMap(menu -> menu.getSubVos().stream())
				.filter(sub -> sub.getProgramId().equals(permission.getProgramId())).findFirst()
				.ifPresent(sub -> sub.setPermission("Y".equalsIgnoreCase(permission.getEnabled()))));

		return menus;
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdatePermission(PermissionVo vo) throws Exception {
		if (vo.getAccountId() == null) {
			saveRolePermissions(vo);
		} else {
			saveAccountPermissions(vo);
		}
	}

	@Transactional(readOnly = true)
	public List<AccountActionHistoryDto> getAccountActionHistoriesByCriteria(Long executeAccountId, Long accountId,
			String requestId) {
		List<Object[]> rows = accountActionHistoryRepo.findAccountActionHistoriesByCriteria(executeAccountId, accountId,
				requestId);

		List<AccountActionHistoryDto> dtos = new ArrayList<>();
		for (Object[] row : rows) {
			Long ownerAccountId = row[2] != null ? ((Number) row[2]).longValue() : null;
			Long executorAccountId = row[4] != null ? ((Number) row[4]).longValue() : null;

			dtos.add(new AccountActionHistoryDto((String) row[0], (String) row[1], ownerAccountId, (String) row[3],
					executorAccountId, (String) row[5], (String) row[6]));
		}
		return dtos;
	}

	@Transactional(readOnly = true)
	public List<AccountEntity> getExecuteAccountList(Long roleId) {
		return accountRepo.findByRoleIdOrderByUserIdAsc(roleId);
	}

	@Transactional(readOnly = true)
	public List<AccountEntity> getAccountList() {
		return accountRepo.findAllByOrderByUserIdAsc();
	}

	private void saveRolePermissions(PermissionVo vo) {
		ActionType action = ActionType.find(vo.getAction());
		if (action.isAdd()) {
			RoleEntity entity = new RoleEntity();
			entity.setRoleName(String.format("ROLE_%s", vo.getRoleName()));
			roleRepo.save(entity);
			vo.setRoleId(entity.getRoleId());
		} else if (action.isEdit()) {
			rolePermissionProgramRepo.deleteByRoleId(vo.getRoleId());
		} else {
			throw new IllegalArgumentException("Unsupported role permission action");
		}

		List<Long> programIds = parseProgramIds(vo.getOnPrograms());
		programIds.forEach(programId -> saveRolePermissionProgramEntity(vo.getRoleId(), programId));
	}

	private void saveAccountPermissions(PermissionVo vo) {
		Long accountId = vo.getAccountId();

		accountPermissionProgramRepo.deleteByAccountId(accountId);

		parseProgramIds(vo.getOnPrograms())
				.forEach(programId -> saveAccountPermissionProgramEntity(accountId, programId, true));

		parseProgramIds(vo.getOffPrograms())
				.forEach(programId -> saveAccountPermissionProgramEntity(accountId, programId, false));
	}

	private List<MenuVo> composeMenu() {
		return menuRepo.findAll().stream()
				.sorted(Comparator.comparing(MenuEntity::getOrderNo, Comparator.nullsLast(Integer::compareTo)))
				.map(menu -> {
					MenuVo vo = new MenuVo();
					vo.setMenuId(menu.getMenuId());
					vo.setName(menu.getMenuName());

					List<ProgramEntity> programs = menu.getPrograms().stream()
							.sorted(Comparator.comparing(ProgramEntity::getOrderNo,
									Comparator.nullsLast(Integer::compareTo)))
							.collect(Collectors.toList());

					vo.setSubVos(programs.stream()
							.map(program -> new SubMenuVo(program.getProgramId(), program.getProgramName()))
							.collect(Collectors.toList()));

					return vo;
				}).collect(Collectors.toList());
	}

	private void saveRolePermissionProgramEntity(Long roleId, Long programId) {
		RolePermissionProgramEntity entity = new RolePermissionProgramEntity();
		entity.setRoleId(roleId);
		entity.setProgramId(programId);
		entity.setCreateAccount(getAccountId());
		entity.setCreateDate(new Date());
		rolePermissionProgramRepo.save(entity);
	}

	private void saveAccountPermissionProgramEntity(Long accountId, Long programId, boolean enabled) {
		AccountPermissionProgramEntity entity = new AccountPermissionProgramEntity();
		entity.setAccountId(accountId);
		entity.setProgramId(programId);
		entity.setCreateAccount(getAccountId());
		entity.setCreateDate(new Date());
		entity.setEnabled(enabled ? "Y" : "N");
		accountPermissionProgramRepo.save(entity);
	}

	private List<Long> parseProgramIds(List<List<String>> programGroups) {
		if (CollectionUtils.isEmpty(programGroups)) {
			return new ArrayList<>();
		}

		return programGroups.stream().filter(CollectionUtils::isNotEmpty).flatMap(List::stream)
				.filter(StringUtilsEx::isNotBlank).map(Long::valueOf).collect(Collectors.toList());
	}
}
