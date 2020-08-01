package com.itheima.travel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址实体类
 */
@Data //为属性设置了get、set、toString等方法
@NoArgsConstructor  //提供了无参构造器
@AllArgsConstructor //提供了全参构造器
public class Address implements Serializable {

    private Integer aid;

    private String contact;

    private String address;

    private String telephone;

    private String isdefault;

    private User user;



}
