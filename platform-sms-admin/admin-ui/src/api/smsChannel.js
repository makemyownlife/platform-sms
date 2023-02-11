import request from '@/utils/request'

export function getSmsChannels(params) {
  return request({
    url: '/sms/channels',
    method: 'post',
    params: params
  })
}
