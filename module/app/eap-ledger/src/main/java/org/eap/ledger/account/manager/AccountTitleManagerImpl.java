package org.eap.ledger.account.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.eap.ledger.account.domain.AccountTitle;
import org.eap.ledger.account.mapper.AccountTitleMapper;

@Component
@Transactional(readOnly=true) 
public class AccountTitleManagerImpl implements AccountTitleManager {

	@Autowired
	private AccountTitleMapper accountTitleMapper;
	
	@Override
	public AccountTitle readOneAccountTitle(String id) {
		return accountTitleMapper.selectByPrimaryKey(id);
	}

}
