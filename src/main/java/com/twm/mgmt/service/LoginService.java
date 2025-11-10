package com.twm.mgmt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twm.mgmt.model.common.MenuVo;
import com.twm.mgmt.model.common.SubMenuVo;
import com.twm.mgmt.persistence.entity.MenuEntity;
import com.twm.mgmt.persistence.entity.ProgramEntity;
import com.twm.mgmt.persistence.repository.AccountPermissionProgramRepository;
import com.twm.mgmt.persistence.repository.MenuRepository;
import com.twm.mgmt.persistence.repository.ProgramRepository;
import com.twm.mgmt.persistence.repository.RolePermissionProgramRepository;
import com.twm.mgmt.utils.JsonUtil;
import com.twm.mgmt.utils.SpringUtils;
import com.twm.mgmt.utils.StringUtilsEx;

@Service
public class LoginService extends BaseService {

	@Autowired
	private ProgramRepository programRepo;

	public List<MenuVo> findMenuVo() {
		RolePermissionProgramRepository repo = SpringUtils.getBean(RolePermissionProgramRepository.class);

		// <v20210728.M1.M_Comment> DB : ROLE_PERMISSION_PROGRAM ACCOUNT 要做設定

		// role_permission
		List<Long> programIds = repo.findByRoleId(getRoleId()).stream().map(entity -> entity.getProgramId())
				.collect(Collectors.toList());

		// account_permission
		filter(programIds);

		// menu
		return composeMenu(programIds);
	}

	private void filter(List<Long> programIds) {
		Long accountId = getAccountId();

		AccountPermissionProgramRepository repo = SpringUtils.getBean(AccountPermissionProgramRepository.class);

		repo.findByAccountId(accountId).stream().forEach(entity -> {
			Long programId = entity.getProgramId();

			if (StringUtilsEx.equals("Y", entity.getEnabled())) {
				if (!programIds.contains(programId)) {
					programIds.add(programId);
				}
			} else {
				if (programIds.contains(programId)) {
					programIds.remove(programId);
				}
			}
		});
	}

	private List<MenuVo> composeMenu(List<Long> programIds) {
		// <v20210728.M1.M_Comment> 這一段 DB PROGRAM 有bug
		for (Long x : programIds) {
			// System.out.println("00Menu: "+x);
		}

		MenuRepository menuRepo = SpringUtils.getBean(MenuRepository.class);

		ProgramRepository programRepo = SpringUtils.getBean(ProgramRepository.class);

		List<ProgramEntity> programs = CollectionUtils.isNotEmpty(programIds) ? programRepo.findPrograms(programIds)
				: new ArrayList<>();

		List<Long> menuIds = programs.stream().map(program -> program.getMenuId()).distinct()
				.collect(Collectors.toList());

		List<MenuEntity> menus = CollectionUtils.isNotEmpty(menuIds) ? menuRepo.findMenus(menuIds) : new ArrayList<>();

		return menus.stream().map(menu -> {

			List<SubMenuVo> subVos = programs.stream().filter(program -> menu.getMenuId().equals(program.getMenuId()))
					.map(program -> new SubMenuVo(program.getProgramName(), program.getProgramUri()))
					.collect(Collectors.toList());

			return new MenuVo(menu.getMenuName(), subVos, menu);
		}).collect(Collectors.toList());
	}

	public ProgramEntity getShowProgramName(String showalert) {
		ProgramEntity findProgramUriLike = programRepo.findByProgramUri(showalert);
		// checkmarx弱掃
		findProgramUriLike = JsonUtil.jsonToPojo(
				ESAPI.encoder().decodeForHTML(ESAPI.encoder().encodeForHTML(JsonUtil.objectToJson(findProgramUriLike))),
				ProgramEntity.class);
		return findProgramUriLike;
	}

}
