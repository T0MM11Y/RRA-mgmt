package com.twm.mgmt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twm.mgmt.persistence.entity.AccountEntity;
import com.twm.mgmt.persistence.repository.AccountRepository;
import com.twm.mgmt.persistence.repository.LoginHistoryRepository;

@Service
public class AlertFor90dayLoginService extends BaseService {
	
	@Autowired
	private AlertFor90dayLoginService alertFor90dayLoginService;
	
	@Autowired
	private LoginHistoryRepository loginHistoryRepo;
	
	private Set<String> almostDisableSet;
	private Set<String> disabledSet;
    
    
	
	public void alertFor90dayLogin() {
		almostDisableSet =new HashSet<String>();
		disabledSet = new HashSet<String>();
		
	    List<Object[]> results = loginHistoryRepo.findLatestLoginForEachAccount();
	    Map<Long, Date> loginMap = new HashMap<>();

	    for (Object[] result : results) {
	    	if (result[0] == null) {
	    		continue;
	    	}
	    	Long accountId = ((Number) result[0]).longValue();
	        Date loginDate = (Date) result[1];
	        loginMap.put(accountId, loginDate);
	    }
	    
	    List<AccountEntity> enabledAccounts = accountRepo.findEnabledAccount();
	    
	    for (AccountEntity enabledAccount : enabledAccounts) {
	    	Long accountId = enabledAccount.getAccountId();
	        Date createDate = enabledAccount.getCreateDate();
	        
	        if (!loginMap.containsKey(accountId)) {
	           
	            loginMap.put(accountId, createDate);
	        }
	    }


	    log.info("啟用狀態使用者未登入天數:");
	    for (Map.Entry<Long, Date> entry : loginMap.entrySet()) {
	        try {
				long daysDifference = calculateDaysDifference(entry.getValue());	       
				if (Arrays.asList(70L, 75L, 80L, 85L).contains(daysDifference)) {
					
				    String email = getEmailFromAccountId(entry.getKey());
				    if (email != null) {
				        almostDisableSet.add(email);
				    }
				} else if (daysDifference == 90) {
					
				    String email = getEmailFromAccountId(entry.getKey());
				    if (email != null) {
				        disabledSet.add(email);
				        alertFor90dayLoginService.disableAccount(entry.getKey());
				    }
				}
				
				log.info(entry.getKey()+":"+daysDifference+"天");
			} catch (Exception e) {
				log.info("處理已啟用的帳號出錯的accountId:{}",entry.getKey(),e.toString());
				log.error("處理已啟用的帳號出錯的accountId:{}",entry.getKey(),e.toString());
			}
	    }

	setAlmostDisableSet(almostDisableSet);
	setDisabledSet(disabledSet);
	}
	
	private long calculateDaysDifference(Date date) {
	   
	    Calendar currentDate = Calendar.getInstance();
	    currentDate.setTime(new Date());
	    
	    currentDate.set(Calendar.HOUR_OF_DAY, 0);
	    currentDate.set(Calendar.MINUTE, 0);
	    currentDate.set(Calendar.SECOND, 0);
	    currentDate.set(Calendar.MILLISECOND, 0);

	    
	    Calendar inputDate = Calendar.getInstance();
	    inputDate.setTime(date);
	   
	    inputDate.set(Calendar.HOUR_OF_DAY, 0);
	    inputDate.set(Calendar.MINUTE, 0);
	    inputDate.set(Calendar.SECOND, 0);
	    inputDate.set(Calendar.MILLISECOND, 0);

	   
	    long diffInMillis = currentDate.getTimeInMillis() - inputDate.getTimeInMillis();
	    
	    return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
	}

	private String getEmailFromAccountId(Long accountId) {
	    AccountEntity account = accountRepo.findById(accountId).orElse(null);
	    return account != null ? account.getEmail() : null;
	}
	
	public void disableAccount(Long accountId) throws Exception {
	    AccountEntity account = accountRepo.findById(accountId).orElse(null);
	    if (account != null) {	    	
	        account.setEnabled("N");
	        accountRepo.save(account);
	        log.info("90天未登入已啟用的accountId:{},設定成已停用Enabled=N",accountId);
	    } else {
	    	throw new Exception("查無此90天未登入已啟用的accountId:"+accountId);
	    }
	}
	
	public Set<String> getAlmostDisableSet() {
		return almostDisableSet;
	}

	public void setAlmostDisableSet(Set<String> almostDisableSet) {
		this.almostDisableSet = almostDisableSet;
	}

	public Set<String> getDisabledSet() {
		return disabledSet;
	}

	public void setDisabledSet(Set<String> disabledSet) {
		this.disabledSet = disabledSet;
	}
	
}
