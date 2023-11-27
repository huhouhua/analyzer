package com.ruijie.job.middleware;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ruijie.job.config.GitRepositoryConfig;
import com.ruijie.job.config.JobScanConfig;
import org.ruijie.core.Middleware;
import org.ruijie.core.MiddlewareNext;
import org.ruijie.core.git.GitProjectConfigProvider;
import org.ruijie.core.git.GitProjectFactory;
import org.ruijie.core.git.GitProjectSupport;
import org.ruijie.core.git.ProjectFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitRepositoryMiddleware implements Middleware<ScanContext> {
    private final Logger LOG = LoggerFactory.getLogger(InitRepositoryMiddleware.class.getName());
    private final JobScanConfig jobConfig;
    private  final GitRepositoryConfig gitConfig;

    public InitRepositoryMiddleware(
            JobScanConfig jobConfig,
            GitRepositoryConfig gitConfig){
        this.gitConfig = gitConfig;
       this.jobConfig= jobConfig;
    }

    @Override
    public Object invoke(Object prev, ScanContext ctx, MiddlewareNext next) throws Exception {
        LOG.info(StrUtil.format("clone {}, branch is {} ",ctx.getRepoName(),ctx.getBranch()));
        String   projectPath = StrUtil.format("{}{}",
                jobConfig.getDirectory(),
                FileUtil.mainName(ctx.getRepoUrl()));
        GitProjectSupport instance = this.createProjectFeatureInstance(projectPath, ctx);
        LOG.info("start clone ....................");
        LOG.info(StrUtil.format("clone to ....................{}",projectPath));
        next.execute(instance.gitClone());
        return null;
    }
    private GitProjectSupport createProjectFeatureInstance(String projectPath, ScanContext ctx ){
        return GitProjectFactory.createFeatureInstance(
                new GitProjectConfigProvider(ctx.getRepoUrl(),
                        projectPath,
                        ctx.getBranch(),
                        gitConfig.getUsername(),
                        gitConfig.getPassword()));
    }
}
