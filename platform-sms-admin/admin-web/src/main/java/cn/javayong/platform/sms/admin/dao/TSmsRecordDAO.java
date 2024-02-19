package cn.javayong.platform.sms.admin.dao;

import cn.javayong.platform.sms.admin.domain.TSmsRecord;
import org.springframework.stereotype.Repository;

/**
 * TSmsRecordDAO继承基类
 */
@Repository
public interface TSmsRecordDAO extends MyBatisBaseDao<TSmsRecord, Long> {

    
}