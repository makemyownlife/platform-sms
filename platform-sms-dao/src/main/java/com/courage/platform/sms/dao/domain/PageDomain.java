package com.courage.platform.sms.dao.domain;


import org.apache.commons.lang3.StringUtils;

public class PageDomain {

    /**
     * 当前记录起始索引
     */
    private int pageNum;
    /**
     * 每页显示记录数
     */
    private int pageSize;
    /**
     * 排序列
     */
    private String orderByColumn;
    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    private String isAsc;
    //分页起始数
    private int startNum;
    //分页结束数
    private int endNum;
    //总记录数
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStartNum() {
        startNum = (getPageNum() - 1) * getPageSize();
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getEndNum() {
        endNum = getPageNum() * getPageSize();
        endNum = endNum < total ? endNum : total;
        return endNum;
    }

    public void setEndNum(int endNum) {
        this.endNum = endNum;
    }

    @Override
    public String toString() {
        return "PageDomain{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", orderByColumn='" + orderByColumn + '\'' +
                ", isAsc='" + isAsc + '\'' +
                '}';
    }

    public String getOrderBy() {
        if (StringUtils.isEmpty(orderByColumn)) {
            return "";
        }
        return orderByColumn + " " + isAsc;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc() {
        return isAsc;
    }

    public void setIsAsc(String isAsc) {
        this.isAsc = isAsc;
    }

}
