import request from '@/utils/request'

export function getSmsRecords(params) {
  return request({
    url: '/sms/records',
    method: 'post',
    params: params
  })
}

export function addSmsRecord(params) {
  return request({
    url: '/sms/addSmsRecord',
    method: 'post',
    params: params
  })
}

