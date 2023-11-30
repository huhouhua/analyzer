package com.ruijie.job.middleware;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.ruijie.core.Middleware;
import org.ruijie.core.MiddlewareNext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jgit.api.Git;

public class ConfigParseMiddleware implements Middleware<SonarContext> {
    private final Logger LOG = LoggerFactory.getLogger(ConfigParseMiddleware.class.getName());

    @Override
    public Object invoke(Object prev, SonarContext ctx, MiddlewareNext next) throws Exception {
        if (!(prev instanceof Git)) {
            return new Exception("git data conversion failure!");
        }
        Git git = (Git) prev;
        String workDir = git.getRepository().getWorkTree().getPath();
        String sonarFilePath = StrUtil.format("{}{}{}", workDir,"/",ctx.getProject().getSonarFileUrl());
        LOG.info(StrUtil.format("start parse config {}", sonarFilePath));
        boolean exits = FileUtil.exist(sonarFilePath);
        if (!exits) {
            throw new Exception(StrUtil.format("{} file not exist!", sonarFilePath));
        }
        next.execute(sonarFilePath);
        return null;
    }
}
