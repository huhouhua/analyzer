package com.ruijie.job.middleware;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ruijie.core.Middleware;
import com.ruijie.core.MiddlewareNext;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigParseMiddleware implements Middleware<AnalyzerContext> {
    private final Logger LOG = LoggerFactory.getLogger(ConfigParseMiddleware.class.getName());

    @Override
    public Object invoke(Object prev, AnalyzerContext ctx, MiddlewareNext next) throws Exception {
        if (!(prev instanceof Git)) {
            return new Exception("git data conversion failure!");
        }
        Git git = (Git) prev;
        String workDir = git.getRepository().getWorkTree().getPath();
        String sonarFilePath = StrUtil.format("{}{}{}", workDir,"/",ctx.getProject().getSonarFilePath());
        LOG.info(StrUtil.format("start parse config {}", sonarFilePath));
        boolean exits = FileUtil.exist(sonarFilePath);
        if (!exits) {
            throw new Exception(StrUtil.format("{} file not exist!", sonarFilePath));
        }
        next.execute(sonarFilePath);
        return null;
    }
}
