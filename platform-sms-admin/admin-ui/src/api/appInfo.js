import request from '@/utils/request'

export function getAppList(params) {
  return request({
    url: '/sms/appList',
    method: 'post',
    params: params
  })
}

export function addAppInfo(data) {
  return request({
    url: '/sms/addAppInfo',
    method: 'post',
    data
  })
}

export function deleteAppInfo(data) {
  return request({
    url: '/sms/deleteAppInfo?id=' + data,
    method: 'post'
  })
}

export function updateAppInfo(data) {
  return request({
    url: '/sms/updateAppInfo',
    method: 'post',
    data
  })
}
