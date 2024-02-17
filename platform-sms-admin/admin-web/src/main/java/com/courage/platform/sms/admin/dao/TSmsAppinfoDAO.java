package com.courage.platform.sms.admin.dao;

import com.courage.platform.sms.admin.domain.TSmsAppinfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TSmsAppinfoDAO extends MyBatisBaseDao<TSmsAppinfo, Long> {

    TSmsAppinfo getAppinfoByAppKey(@Param("appKey") String appKey);

    List<TSmsAppinfo> selectAll();

    List<TSmsAppinfo> selectAppInfoListPage(Map<String, Object> param);

    Integer selectAppInfoCount(Map<String, Object> param);

    List<TSmsAppinfo> selectAppInfoListByIds(@Param("ids") List<Long> ids);

}