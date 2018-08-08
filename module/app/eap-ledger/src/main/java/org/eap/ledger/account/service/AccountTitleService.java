package org.eap.ledger.account.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.eap.ledger.account.domain.AccountTitle;

public interface AccountTitleService {
	
	AccountTitle readOneAccountTitle(String id);
	
	Page<AccountTitle> readPageAccountTitle(Pageable pageable,Map<String, Object> param);

}
