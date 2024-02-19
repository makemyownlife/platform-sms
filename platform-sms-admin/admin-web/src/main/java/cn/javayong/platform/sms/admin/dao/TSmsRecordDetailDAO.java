package cn.javayong.platform.sms.admin.dao;

import cn.javayong.platform.sms.admin.domain.TSmsRecordDetail;
import cn.javayong.platform.sms.admin.domain.vo.RecordVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * TSmsRecordDetailDAO继承基类
 */
@Repository
public interface TSmsRecordDetailDAO extends MyBatisBaseDao<TSmsRecordDetail, Long> {

    List<TSmsRecordDetail> queryRecordDetailList(Map<String, Object> param);

    List<RecordVO> queryRecordVOList(Map<String, Object> param);

    List<RecordVO> queryOneRecordVOByAppId(String appId);

    Long queryCountRecordDetail(Map<String, Object> param);

}