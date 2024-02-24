package cn.javayong.platform.sms.admin.dao;

import cn.javayong.platform.sms.admin.domain.TSmsRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * TSmsRecordDAO继承基类
 */
@Repository
public interface TSmsRecordDAO extends MyBatisBaseDao<TSmsRecord, Long> {


    List<Map> queryWaitingSendSmsList(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startId") Long startId);

}