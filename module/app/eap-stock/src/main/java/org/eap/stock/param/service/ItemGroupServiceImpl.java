package org.eap.stock.param.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.eap.framework.manager.SequenceManager;
import org.eap.framework.util.Const;
import org.eap.framework.web.util.WebConst;
import org.eap.stock.param.domain.Item;
import org.eap.stock.param.domain.ItemGroup;
import org.eap.stock.param.mapper.ItemGroupMapper;
import org.eap.stock.param.mapper.ItemMapper;

@Service
@Transactional(readOnly = true)
public class ItemGroupServiceImpl implements ItemGroupService {

	private final String ID_PREFIX = "ITEM-GROUP-";

	@Autowired
	private ItemGroupMapper itemGroupMapper;
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SequenceManager sequenceManager;

	@Override
	public ItemGroup readAllItemGroup(String id) {
		ItemGroup itemGroup = itemGroupMapper.selectByPrimaryKey(id);
		if (null != itemGroup) {
			if (!WebConst.ROOT.equals(id)) {
				itemGroup.setParentName(readOneItemGroup(itemGroup.getParentId()).getName());
			}
			List<ItemGroup> childList = new ArrayList<ItemGroup>();
			List<ItemGroup> itemList = itemGroupMapper.selectChildren(id);
			if (null != itemList) {
				for (ItemGroup cItemGroup : itemList) {
					childList.add(readAllItemGroup(cItemGroup.getId()));
				}
				itemGroup.setChildren(childList);
			}

		}
		return itemGroup;
	}

	@Override
	public List<ItemGroup> readItemGroupList(String id) {
		List<ItemGroup> resultList = new ArrayList<ItemGroup>(500);
		ItemGroup itemGroup = itemGroupMapper.selectByPrimaryKey(id);
		if (null != itemGroup) {
			resultList.add(itemGroup);
			List<ItemGroup> itemList = itemGroupMapper.selectChildren(id);
			if (null != itemList) {
				for (ItemGroup cItemGroup : itemList) {
					resultList.addAll(readItemGroupList(cItemGroup.getId()));
				}
			}
		}
		return resultList;
	}

	@Override
	public List<ItemGroup> readChildItemGroup(String id) {
		List<ItemGroup> itemGroupList = itemGroupMapper.selectChildren(id);
		if (null != itemGroupList) {
			//Lambda表达式测试
			itemGroupList = itemGroupList.stream().filter(itemGroup -> itemGroup.getStatus() == (byte) 1)
					.collect(Collectors.toList());
			itemGroupList.forEach(itemGroup -> {
				itemGroup.setChildren(readChildItemGroup(itemGroup.getId()));
			});
		}
		return itemGroupList;
	}

	@Override
	public ItemGroup readOneItemGroup(String id) {
		return itemGroupMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void updateItemGroup(ItemGroup record) {
		itemGroupMapper.updateBySelective(record);

	}

	@Override
	@Transactional
	public void addItemGroup(ItemGroup record) {
		if (StringUtils.isEmpty(record.getId())) {
			record.setId(ID_PREFIX + sequenceManager.nextStringSequence(Const.ITEM_GROUP_SEQ));
			record.setStatus((byte) 1);
			itemGroupMapper.insert(record);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteItemGroup(String id) throws IllegalArgumentException {
		List<ItemGroup> child = itemGroupMapper.selectChildren(id);
		if (child != null && child.size() > 0) {
			throw new IllegalArgumentException("产品群组存在子记录!");
		}
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("itemGroupId", id);
		List<Item> itemList = itemMapper.selectList(parmMap);
		if (itemList != null && itemList.size() > 0) {
			throw new IllegalArgumentException("该组别下存在货品记录!");
		}
		itemGroupMapper.deleteByPrimaryKey(id);

	}

}
