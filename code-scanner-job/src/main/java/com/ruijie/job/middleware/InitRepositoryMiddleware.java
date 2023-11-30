package com.ruijie.job.middleware;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ruijie.job.config.JobScanConfig;
import com.ruijie.job.config.ProjectConfigProvider;
import com.ruijie.job.config.RepositoryConfigProvider;
import org.ruijie.core.Middleware;
import org.ruijie.core.MiddlewareNext;
import org.ruijie.core.git.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitRepositoryMiddleware implements Middleware<SonarContext> {
    private final Logger LOG = LoggerFactory.getLogger(InitRepositoryMiddleware.class.getName());
    private final JobScanConfig jobConfig;
    public InitRepositoryMiddleware(
            JobScanConfig jobConfig){
       this.jobConfig= jobConfig;
    }

    @Override
    public Object invoke(Object prev, SonarContext ctx, MiddlewareNext next) throws Exception {
        LOG.info(StrUtil.format("clone {}, branch is {} ",ctx.getProject().getName(),ctx.getProject().getBranch()));
        String   projectPath = StrUtil.format("{}/{}",
                jobConfig.getDirectory(),
                FileUtil.mainName(ctx.getProject().getUrl()));
        GitProjectSupport instance = this.createProjectFeatureInstance(projectPath, ctx.getProject());
        LOG.info("******************* start clone *******************");
        LOG.info(StrUtil.format("clone to {}..............",projectPath));
        next.execute(instance.gitClone());
        return null;
    }
    private GitProjectSupport createProjectFeatureInstance(String projectPath, ProjectConfigProvider provider ){
        return GitProjectFactory.createFeatureInstance(
                new GitProjectConfigProvider(provider.getUrl(),
                        projectPath,
                        provider.getBranch(),
                        new SshCredentialsConfig(RepositoryConfigProvider.getSshSessionFactory())));
    }
}
