package com.ruijie.job.notify;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NotificationBuilder {

    public String msg_type;

    public NotificationContent content;

    public static NotificationBuilder newBuilder() {
        return new NotificationBuilder();
    }

    public NotificationBuilder withMsgType(String msgType) {
        this.msg_type = msgType;
        return this;
    }

    public NotificationBuilder withContent(NotificationContent content) {
        this.content = content;
        return this;
    }

    public NotificationBuilder withContent(String text) {
        this.content = new NotificationContent(text);
        return this;
    }
    
    public String build() {
        return JSONUtil.toJsonStr(this);
    }
    @Override
    public String toString() {
        return "NotificationBuilder{" +
                "msg_type='" + msg_type + '\'' +
                ", content=" + content +
                '}';
    }

}

