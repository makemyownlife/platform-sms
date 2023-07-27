import request from '@/utils/request'

export function getSmsRecords(params) {
  return request({
    url: '/sms/records',
    method: 'post',
    params: params
  })
}

export function addSmsRecord(data) {
  return request({
    url: '/sms/addSmsRecord',
    method: 'post',
    data
  })
}

