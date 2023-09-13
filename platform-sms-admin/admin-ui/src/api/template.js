import request from '@/utils/request'

export function getSmsTemplates(params) {
  return request({
    url: '/sms/templates',
    method: 'post',
    params: params
  })
}

export function addSmsTemplate(data) {
  return request({
    url: '/sms/addSmsTemplate',
    method: 'post',
    data
  })
}

export function deleteTemplate(data) {
  return request({
    url: '/sms/deleteSmsTemplate?id=' + data,
    method: 'post'
  })
}

export function updateSmsTemplate(data) {
  return request({
    url: '/sms/updateSmsTemplate',
    method: 'post',
    data
  })
}

export function getSmsTemplatesBinding(data) {
  return request({
    url: '/sms/getSmsTemplatesBinding?id=' + data,
    method: 'post'
  })
}


export function autoBindChannel(data) {
  return request({
    url: '/sms/autoBindChannel?templateId=' + data.templateId + "&channelIds=" +  data.channelIds + "&templateCode=" + data.templateCode,
    method: 'post'
  })
}
