package cn.javayong.platform.sms.admin.service.impl;

import cn.javayong.platform.sms.admin.dispatcher.AdapterDispatcher;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestCode;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestEntity;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.body.ApplyTemplateRequestBody;
import cn.javayong.platform.sms.admin.service.SmsTemplateService;
import cn.javayong.platform.sms.admin.common.config.IdGenerator;
import cn.javayong.platform.sms.admin.common.utils.ResponseCode;
import cn.javayong.platform.sms.admin.common.utils.ResponseEntity;
import cn.javayong.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import cn.javayong.platform.sms.admin.dao.TSmsTemplateDAO;
import cn.javayong.platform.sms.admin.domain.TSmsTemplate;
import cn.javayong.platform.sms.admin.domain.TSmsTemplateBinding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SmsTemplateServiceImpl implements SmsTemplateService {

    private final static Logger logger = LoggerFactory.getLogger(SmsTemplateServiceImpl.class);

    @Autowired
    private TSmsTemplateDAO tSmsTemplateDAO;

    @Autowired
    private TSmsTemplateBindingDAO tSmsTemplateBindingDAO;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AdapterDispatcher smsAdapterController;

    @Override
    public List<TSmsTemplate> queryTemplates(Map<String, Object> params) {
        List<TSmsTemplate> tSmsTemplates = tSmsTemplateDAO.queryTemplates(params);
        for (TSmsTemplate tSmsTemplate : tSmsTemplates) {
            List<TSmsTemplateBinding> bindingList = tSmsTemplateBindingDAO.selectBindingsByTemplateId(tSmsTemplate.getId());
            tSmsTemplate.setBindingList(bindingList);
        }
        return tSmsTemplates;
    }

    @Override
    public Long queryCountTemplates(Map<String, Object> param) {
        return tSmsTemplateDAO.queryCountTemplates(param);
    }

    @Override
    public ResponseEntity<String> addSmsTemplate(TSmsTemplate tSmsTemplate) {
        Long templateId = idGenerator.createUniqueId(tSmsTemplate.getSignName());
        try {
            tSmsTemplate.setId(templateId);
            tSmsTemplate.setCreateTime(new Date());
            tSmsTemplate.setUpdateTime(new Date());
            tSmsTemplate.setStatus((byte) 0);
            tSmsTemplateDAO.insert(tSmsTemplate);
            return ResponseEntity.success("success");
        } catch (Exception e) {
            logger.error("addSmsTemplate error:", e);
            return ResponseEntity.fail("fail");
        }
    }

    @Override
    public ResponseEntity<String> updateSmsTemplate(TSmsTemplate tSmsTemplate) {
        try {
            tSmsTemplate.setUpdateTime(new Date());
            tSmsTemplateDAO.updateByPrimaryKeySelective(tSmsTemplate);
            return ResponseEntity.success("success");
        } catch (Exception e) {
            logger.error("updateSmsTemplate error:", e);
            return ResponseEntity.fail("fail");
        }
    }

    @Override
    public ResponseEntity<String> deleteSmsTemplate(Long id) {
        try {
            tSmsTemplateDAO.deleteByPrimaryKey(id);
            tSmsTemplateBindingDAO.deleteTemplateBindingByTemplateId(id);
            return ResponseEntity.success("success");
        } catch (Exception e) {
            logger.error("deleteSmsTemplate error:", e);
            return ResponseEntity.fail("fail");
        }
    }

    @Override
    public ResponseEntity<String> autoBindChannel(String channelIds, Long templateId) {
        logger.info("channelIds:" + channelIds + " templateId:" + templateId);
        try {
            String[] channelIdsArr = StringUtils.split(channelIds, ',');
            for (String channelId : channelIdsArr) {
                //先查询是否已经提交过
                TSmsTemplateBinding binding = tSmsTemplateBindingDAO.selectBindingByTemplateIdAndChannelId(templateId, Long.valueOf(channelId));
                if (binding == null) {
                    Long bindingId = idGenerator.createUniqueId(String.valueOf(templateId));
                    binding = new TSmsTemplateBinding();
                    binding.setId(bindingId);
                    binding.setTemplateId(templateId);
                    binding.setChannelId(Integer.valueOf(channelId));
                    // 0 : 待提交 1：待审核  2：审核成功 3：审核失败
                    binding.setStatus(0);
                    binding.setTemplateContent(StringUtils.EMPTY);
                    binding.setTemplateCode(StringUtils.EMPTY);
                    binding.setCreateTime(new Date());
                    binding.setUpdateTime(new Date());
                    tSmsTemplateBindingDAO.insertSelective(binding);
                }
                // 向渠道申请模版
                ApplyTemplateRequestBody applyTemplateRequestBody = new ApplyTemplateRequestBody(binding.getId());
                ResponseEntity<String> response = smsAdapterController.dispatchSyncRequest(RequestCode.APPLY_TEMPLATE, new RequestEntity(applyTemplateRequestBody));
                if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                    return ResponseEntity.success("success");
                }else {
                    return ResponseEntity.fail("绑定失败");
                }
            }
            return ResponseEntity.success("success");
        } catch (Exception e) {
            logger.error("autoBindChannel error:", e);
            return ResponseEntity.fail("fail");
        }
    }

    @Override
    public ResponseEntity<String> handBindChannel(String channelIds, Long templateId, String templateCode) {
        String[] channelIdsArr = StringUtils.split(channelIds, ',');
        for (String channelId : channelIdsArr) {
            TSmsTemplateBinding binding = tSmsTemplateBindingDAO.selectBindingByTemplateIdAndChannelId(templateId, Long.valueOf(channelId));
            if (binding == null) {
                Long bindingId = idGenerator.createUniqueId(String.valueOf(templateId));
                binding = new TSmsTemplateBinding();
                binding.setId(bindingId);
                binding.setTemplateId(templateId);
                binding.setChannelId(Integer.valueOf(channelId));
                // 0 : 待提交 1：待审核  2：审核成功 3：审核失败
                binding.setStatus(2);  // 默认审核成功
                binding.setTemplateContent(StringUtils.EMPTY);
                binding.setTemplateCode(templateCode);
                binding.setCreateTime(new Date());
                binding.setUpdateTime(new Date());
                tSmsTemplateBindingDAO.insertSelective(binding);
            } else {
                binding.setTemplateCode(templateCode);
                binding.setUpdateTime(new Date());
                binding.setStatus(2);
                tSmsTemplateBindingDAO.updateByPrimaryKeySelective(binding);
            }
        }
        return ResponseEntity.success("success");
    }

}

