package com.ruijie.job.middleware;


import cn.hutool.core.util.StrUtil;
import com.ruijie.job.notify.Notification;
import com.ruijie.job.notify.NotificationBuilder;
import lombok.NonNull;
import org.ruijie.core.MiddlewareFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.function.Consumer;

public class CodeScnaProcess {
    private final ScanContext context;
    private static Consumer<ScanContext> middleware;
    private static AutowireCapableBeanFactory beanFactory;

    CodeScnaProcess(AutowireCapableBeanFactory autowireCapableBeanFactory, ScanContext scanContext) {
        context = scanContext;
        beanFactory = autowireCapableBeanFactory;
        middleware = MiddlewareFactory.<ScanContext>CreateDefaultWithGeneric().
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
        String repoInfo = StrUtil.format("仓库名称:{} 仓库地址:{} 仓库分支:{}", context.getRepoName(), context.getRepoUrl(), context.getBranch());
        NotificationBuilder builder = NotificationBuilder.newBuilder().withMsgType("text");
        if (exception == null) {
            builder.withContent(StrUtil.format("{} 代码扫描成功！", repoInfo));
        } else {
            builder.withContent(StrUtil.format("{} 错误消息:{} 代码扫描失败！", repoInfo, exception.getMessage()));
        }
        notification.send(builder);
    }

    private Notification createNotification() {
        final Notification notification = beanFactory.createBean(Notification.class);
        beanFactory.autowireBean(notification);
        return notification;
    }

    public static CodeScnaProcess prepare(@NonNull ScanContext scanContext,
                                          @NonNull AutowireCapableBeanFactory beanFactory) {
        return new CodeScnaProcess(beanFactory, scanContext);
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

    private CodeScanMiddleware createCodeScanMiddleware() {
        final CodeScanMiddleware middleware = beanFactory.createBean(CodeScanMiddleware.class);
        beanFactory.autowireBean(middleware);
        return middleware;
    }
}
