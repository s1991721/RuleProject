package com.ljf.ruleproject.poet;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by mr.lin on 2020/6/5
 * 签约标准：店铺属性    签约级别    销售上限
 * 返利规则：店铺属性    签约标准    花纹      寸别      返利积分    退货积分
 * <p>
 * 1、匹配店铺属性
 * 2、签约量匹配签约级别
 * 3、匹配花纹
 * 4、匹配寸别
 * 5、销量>签约标准计算积分
 * 5、积分和*签约级别值/总销量
 * 6、积分和-退货积分
 */
@Data
public class Store implements Serializable {
    private String id;

    /**
     * 签约量
     */
    private int signValue;

    /**
     * 店铺属性
     */
    private String attribute;

    /**
     * 花纹
     */
    private String figure;

    /**
     * 寸别
     */
    private String size;

    /**
     * 销量
     */
    private int sales;

    /**
     * 退货量
     */
    private int returns;

    /**
     * 积分值
     */
    private int integral;

    @Override
    public String toString() {
        return "Store{" +
                "签约量=" + signValue +
                ", 店铺属性='" + attribute + '\'' +
                ", 花纹='" + figure + '\'' +
                ", 寸别='" + size + '\'' +
                ", 销量=" + sales +
                ", 退货量=" + returns +
                ", 积分值=" + integral +
                '}';
    }

}
