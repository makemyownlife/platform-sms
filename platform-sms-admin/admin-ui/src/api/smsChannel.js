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
