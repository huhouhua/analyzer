package com.ruijie.job.notify;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruijie.job.config.NotificationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Notification {
    private final Logger LOG = LoggerFactory.getLogger(Notification.class.getName());
    private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    private final ObjectMapper mapper;
    private final NotificationConfig notificationConfig;

    @Autowired
    public Notification(NotificationConfig notificationConfig,
                        ObjectMapper objectMapper) {
        this.notificationConfig = notificationConfig;
        this.mapper = objectMapper;
    }

    public void send(NotificationBuilder builder) {
        LOG.info(StrUtil.format("notify data: {}", builder.toString()));
        HttpRequest request = HttpRequest.post(notificationConfig.webhook);
        request.body(builder.build(),CONTENT_TYPE_JSON);
        HttpResponse response = request.execute();
        response.close();
        this.handlerResult(response);
    }

    private void handlerResult(HttpResponse response) {
        try {
            NotificationResult result = readResponse(response);
            if (response.isOk()) {
                LOG.info(StrUtil.format("notify {}", result.getMsg()));
            } else {
                LOG.error(StrUtil.format("notify {} fail! detail:{}", result.getMsg(), result.toString()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private NotificationResult readResponse(HttpResponse response) throws JsonProcessingException {
        return mapper.readValue(response.body(), NotificationResult.class);
    }
}
