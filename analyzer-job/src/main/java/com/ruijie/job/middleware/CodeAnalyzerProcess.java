package com.ruijie.job.middleware;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ruijie.core.MiddlewareFactory;
import com.ruijie.job.notify.Notification;
import com.ruijie.job.notify.NotificationBuilder;
import lombok.NonNull;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.function.Consumer;

public class CodeAnalyzerProcess {
    private final AnalyzerContext context;
    private static Consumer<AnalyzerContext> middleware;
    private static AutowireCapableBeanFactory beanFactory;
    CodeAnalyzerProcess(AutowireCapableBeanFactory autowireCapableBeanFactory, AnalyzerContext scanContext) {
        context = scanContext;
        beanFactory = autowireCapableBeanFactory;
        middleware = MiddlewareFactory.<AnalyzerContext>CreateDefaultWithGeneric().
                use(createInitRepositoryMiddleware()).
                use(createConfigParseMiddleware()).
                use(createCodeScanMiddleware()).
                build();
    }

    public void run() {
        try {
            middleware.accept(context);
            notification(null);
        } catch (Exception e) {
            notification(e);
        }
    }

    private void notification(Exception exception) {
        Notification notification = this.createNotification();
        String repoInfo = StrUtil.format("*************通知*************\n项目名:{}\ngit地址:{}\n分支:{}\n完成时间:{}\nsonar地址:{}",
                context.getProject().getName(),
                context.getProject().getUrl(),
                context.getProject().getBranch(),
                DateUtil.now(),
                context.getSonar().getUrl());
        NotificationBuilder builder = NotificationBuilder.newBuilder().
                withMsgType("text");
        if (exception == null) {
            builder.withContent(StrUtil.format("{}\n结果:扫描成功！", repoInfo));
        } else {
            builder.withContent(StrUtil.format("{}\n结果:扫描失败!\n错误消息:{}", repoInfo, exception.getMessage()));
        }
        notification.send(builder);
    }
    
    private Notification createNotification() {
        final Notification notification = beanFactory.createBean(Notification.class);
        beanFactory.autowireBean(notification);
        return notification;
    }

    public static CodeAnalyzerProcess prepare(@NonNull AnalyzerContext scanContext,
                                              @NonNull AutowireCapableBeanFactory beanFactory) {
        return new CodeAnalyzerProcess(beanFactory, scanContext);
    }

    private InitRepositoryMiddleware createInitRepositoryMiddleware() {
        final InitRepositoryMiddleware middleware = beanFactory.createBean(InitRepositoryMiddleware.class);
        beanFactory.autowireBean(middleware);
        return middleware;
    }

    private ConfigParseMiddleware createConfigParseMiddleware() {
        final ConfigParseMiddleware middleware = beanFactory.createBean(ConfigParseMiddleware.class);
        beanFactory.autowireBean(middleware);
        return middleware;
    }

    private CodeSonarMiddleware createCodeScanMiddleware() {
        final CodeSonarMiddleware middleware = beanFactory.createBean(CodeSonarMiddleware.class);
        beanFactory.autowireBean(middleware);
        return middleware;
    }
}
