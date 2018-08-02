package org.eap.framework.mapper;

import org.eap.framework.domain.Sequence;
import java.util.List;

public interface SequenceMapper {
    int deleteByPrimaryKey(String id);

    int insert(Sequence record);

    Sequence selectByPrimaryKey(String id);

    List<Sequence> selectAll();

    int updateByPrimaryKey(Sequence record);
}