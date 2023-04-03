import request from '@/utils/request'

export function getSmsChannels(params) {
  return request({
    url: '/sms/channels',
    method: 'post',
    params: params
  })
}

export function addSmsChannel(data) {
  return request({
    url: '/sms/addSmsChannel',
    method: 'post',
    data
  })
}

export function deleteSmsChannel(data) {
  return request({
    url: '/sms/deleteSmsChannel?id=' + data,
    method: 'post'
  })
}

export function updateSmsChannel(data) {
  return request({
    url: '/sms/updateSmsChannel',
    method: 'post',
    data
  })
}
