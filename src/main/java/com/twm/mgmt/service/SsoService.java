package com.twm.mgmt.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twm.mgmt.model.common.UserInfoVo;
import com.twm.mgmt.persistence.entity.AccountEntity;
import com.twm.mgmt.persistence.entity.LoginHistoryEntity;
import com.twm.mgmt.persistence.repository.AccountRepository;
import com.twm.mgmt.persistence.repository.LoginHistoryRepository;
import com.twm.mgmt.ws.nt.GetTokenValueRq;
import com.twm.mgmt.ws.nt.GetTokenValueRs;
import com.twm.mgmt.ws.proxy.NtSsoProxy;

@Service
public class SsoService {

	@Autowired
	public NtSsoProxy proxy;

	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private LoginHistoryRepository loginHistoryRepo;

	/**
	 * 取得SSO使用者資訊
	 * 
	 * @param tokenId
	 * @return
	 * @throws Exception
	 */
	public GetTokenValueRs getTokenValue(String tokenId) throws Exception {
		GetTokenValueRq rq = new GetTokenValueRq();

		rq.setFunc("GetTokenValue");

		rq.setTokenId(tokenId);

		return proxy.callWs(rq, GetTokenValueRs.class);
	}

	/**
	 * 取得使用者資訊
	 * 紀錄sso登入的userId和當下登入時間,新增LoginHistory一筆資料
	 * 
	 * @param rs
	 * @return
	 */
	public UserInfoVo getUserInfo(GetTokenValueRs rs) {
		UserInfoVo vo = null;

		if (rs != null) {
			String userId = rs.getUserId();
			List<AccountEntity> entities = accountRepo.findByUserId(userId);

			if (CollectionUtils.isNotEmpty(entities)) {
				AccountEntity entity = entities.get(0);

				vo = UserInfoVo.builder().accountId(entity.getAccountId()).userName(entity.getUserName()).roleId(entity.getRoleId()).roleName(entity.getRole().getRoleName()).departmentId(entity.getDepartmentId())
						.departmentName(entity.getDepartment().getDepartmentName())
						.buTag(entity.getDepartment().getBuTag()).employeeId(rs.getEmployeeId())
						.build();
				System.out.print("GetTokenValueRs:" + rs.getEmployeeId());
				
				Date date = new Date();
				LoginHistoryEntity loginHistoryEntity = LoginHistoryEntity.builder().loginDate(date).accountId(entity.getAccountId()).build();
				loginHistoryRepo.save(loginHistoryEntity);
			}
						
		}

		return vo;
	}

}
