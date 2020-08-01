package com.itheima.travel.domain;

import lombok.Data;

import java.util.List;

/**
 * 分页实体类
 */
@Data
public class PageBean<T> {
    private Integer pageSize;//每页记录数
    private Integer totalCount;//总记录数
    private Integer totalPage;//总页数
    private Integer curPage;//当前页
    private List<T> list;//当前页数据
    private Integer begin;//展示页开始值
    private Integer end;//展示页结束值

    //重写get方法
    public Integer getBegin() {
        calculate();
        return begin;
    }

    //计算begin和end
    public void calculate() {
        if (totalPage <= 10) {
            begin = 1;
            end = totalPage;
        } else {
            //超过10页,前五后四
            begin = curPage - 5;
            end = curPage + 4;
            //begin小于,修正为1,后面补齐10个
            if (begin < 1) {
                begin = 1;
                end = begin + 9;
            }
            //end大于总页数,修正为总页数,前面补齐10个
            if (end > totalPage) {
                end = totalPage;
                begin = end - 9;
            }
        }
    }
}
