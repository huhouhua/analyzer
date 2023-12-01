package com.ruijie.scheduler.config;

import cn.hutool.core.util.ReUtil;

import java.util.Arrays;
import java.util.Objects;

public class MatchExpressionsConfig {

    private String operator;

    private  String[] values;

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }


    public void setOperator(String operator) {
        this.operator = operator;
    }


    public String getOperator() {
        return this.operator;
    }

    public  boolean match(String value){
        if (Objects.equals(this.getOperator(), "NotIn")) {
            return Arrays.stream(this.getValues()).anyMatch(match -> ReUtil.isMatch(match, value));
        }
        return  false;
    }

    @Override
    public String toString() {
        return "MatchExpressionsConfig{" +
                "operator='" + operator + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }

}
