package com.courage.platform.sms.dao;

import com.courage.platform.sms.domain.TSmsAppinfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TSmsAppinfoDAO extends MyBatisBaseDao<TSmsAppinfo, Long> {

    TSmsAppinfo getAppinfoByAppKey(@Param("appKey") String appKey);

    List<TSmsAppinfo> selectAll();

}