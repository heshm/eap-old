package org.eap.stock.param.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.eap.framework.manager.SequenceManager;
import org.eap.framework.util.Const;
import org.eap.stock.param.domain.Price;
import org.eap.stock.param.manager.PriceManager;
import org.eap.stock.param.mapper.PriceMapper;

@Service
@Transactional(readOnly=true)
public class PriceServiceImpl implements PriceService {
	@Autowired
	private PriceManager priceManager;
	@Autowired
	private PriceMapper priceMapper;
	@Autowired
	private SequenceManager sequenceManager;

	@Override
	public List<Price> getAllPriceList() {
		return priceManager.getAllPriceList();
	}

	@Override
	@Transactional
	public void save(Price price) {
		if(StringUtils.isEmpty(price.getId())){
			price.setId(sequenceManager.nextStringSequence(Const.PRICE_SEQ));
			price.setEnabled(price.getEnabled() != Const.VALID ? Const.INVALID : Const.VALID);
			price.setBuying(price.getBuying() != Const.VALID ? Const.INVALID : Const.VALID);
			price.setSelling(price.getSelling() != Const.VALID ? Const.INVALID : Const.VALID);
			priceMapper.insert(price);
		}else{
			price.setEnabled(price.getEnabled() != Const.VALID ? Const.INVALID : Const.VALID);
			price.setBuying(price.getBuying() != Const.VALID ? Const.INVALID : Const.VALID);
			price.setSelling(price.getSelling() != Const.VALID ? Const.INVALID : Const.VALID);
			priceMapper.updateByPrimaryKey(price);
		}
		
	}

	@Override
	public Price getOnePrice(String id) {
		return priceMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Price> getEnablePriceList() {
		List<Price> record = getAllPriceList();
		List<Price> resultList = new LinkedList<>();
		for(Price price : record){
			if(price.getEnabled() == Const.VALID){
				resultList.add(price);
			}
		}
		return resultList;
	}

}
