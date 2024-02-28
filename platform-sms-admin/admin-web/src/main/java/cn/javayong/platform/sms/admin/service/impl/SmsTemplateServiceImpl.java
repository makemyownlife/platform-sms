package cn.javayong.platform.sms.admin.service.impl;

import cn.javayong.platform.sms.admin.common.utils.RedisKeyConstants;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<TSmsTemplate> queryTemplates(Map<String, Object> params) {
        // 1. 根据分页参数查询模板列表
        List<TSmsTemplate> tSmsTemplates = tSmsTemplateDAO.queryTemplates(params);
        for (TSmsTemplate tSmsTemplate : tSmsTemplates) {
            // 2. 便利每个模板条目，查询该条目绑定的
            List<TSmsTemplateBinding> bindingList = tSmsTemplateBindingDAO.selectBindingsByTemplateId(tSmsTemplate.getId());
            tSmsTemplate.setBindingList(bindingList);
        }
        return tSmsTemplates;
    }

    // 列表缓存
    @Override
    public List<TSmsTemplate> queryTemplates2(Map<String, Object> params) {
        List<TSmsTemplate> result = new ArrayList<>();
        // 1. 根据分页参数查询模板 ID 列表
        List<Long> templatesIdList = tSmsTemplateDAO.queryTemplatesIdList(params);
        if (CollectionUtils.isEmpty(templatesIdList)) {
            return Collections.EMPTY_LIST;
        }

        // 2. 根据模板编号列表 批量从 Redis 获取 模板列表
        List<String> templateIdsKeyList = new ArrayList<>(templatesIdList.size());
        for (Long id : templatesIdList) {
            templateIdsKeyList.add(RedisKeyConstants.TEMPLATE_ID_ITEM + id);
        }

        long s = System.currentTimeMillis();
        // 3. 加载缓存没有命中的条目
        List<Long> noHitIdList = new ArrayList<>();
        List<TSmsTemplate> templateList = redisTemplate.opsForValue().multiGet(templateIdsKeyList);

        // 4. 将没有命中缓存的条目 ，从数据库查询出来，然后加载到缓存里
        for (int index = 0; index < templateList.size(); index++) {
            TSmsTemplate tSmsTemplate = templateList.get(index);
            if (tSmsTemplate == null) {
                noHitIdList.add(templatesIdList.get(index));
            }
        }
        Map<Long, TSmsTemplate> noHitTemplateMap = new HashMap<>();
        if (noHitIdList.size() > 0) {
            List<TSmsTemplate> noHitTemplatesList = tSmsTemplateDAO.queryTemplatesByIds(noHitIdList);
            noHitTemplateMap =
                    noHitTemplatesList.stream()
                            .collect(
                                    Collectors.toMap(TSmsTemplate::getId, Function.identity())
                            );
            // 将没有命中的条目加入到缓存里
            for (TSmsTemplate template : noHitTemplatesList) {
                redisTemplate.opsForValue().set(RedisKeyConstants.TEMPLATE_ID_ITEM + template.getId(), template, 3600, TimeUnit.SECONDS);
            }
        }
        // 5 遍历条目ID列表，组装对象列表
        List<TSmsTemplateBinding>  queryBindingListResult = tSmsTemplateBindingDAO.queryTemplateBindingsByIds(templatesIdList);
        for (int index = 0; index < templatesIdList.size(); index++) {
            Long id = templatesIdList.get(index);
            TSmsTemplate tSmsTemplate = templateList.get(index);
            if (tSmsTemplate == null) {
                tSmsTemplate = noHitTemplateMap.get(id);
                templateList.set(index, tSmsTemplate);
            }
            List<TSmsTemplateBinding> bindingList = new ArrayList<>();
            for (TSmsTemplateBinding binding : queryBindingListResult) {
                if(binding.getTemplateId().equals(id)) {
                    bindingList.add(binding);
                }
            }
            tSmsTemplate.setBindingList(bindingList);
        }
        return templateList;
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
                } else {
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

