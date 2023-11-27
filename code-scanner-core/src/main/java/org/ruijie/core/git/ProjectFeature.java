package org.ruijie.core.git;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class ProjectFeature implements GitProjectSupport {
    private final Logger LOG = LoggerFactory.getLogger(ProjectFeature.class.getName());
    private final GitProjectConfigProvider configProvider;
    private static final int MAX_ATTEMPTS = 10;
    private static final long DELAY_IN_MILLIS = 5000;

    public ProjectFeature(@NonNull GitProjectConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    public Git gitClone() throws Exception {
        this.mkStoreDir();
        AtomicReference<Git> git = new AtomicReference<>();
        boolean success = this.tryRetry(() -> {
            try {
                git.set(Git.cloneRepository().
                        setURI(configProvider.getGitUrl()).
                        setBranch(configProvider.getBranch()).
                        setDirectory(configProvider.getProjectFile()).
                        setCredentialsProvider(configProvider.
                                getUsernamePasswordCredentialsProvider()).
                        setProgressMonitor(configProvider.getProgressInstance()).
                        call());
                LOG.info("clone succeed!");
                return true;
            } catch (GitAPIException e) {
                LOG.error(ExceptionUtil.stacktraceToString(e));
                git.set(null);
                return false;
            }
        });
        if (!success) {
            throw new Exception("git clone fail");
        }
        return git.get();
    }

    public PullResult pull() throws Exception {
        this.mkStoreDir();
        AtomicReference<PullResult> git = new AtomicReference<>();
        boolean success =  this.tryRetry(() -> {
            try {
                git.set(Git.open(configProvider.getProjectFile()).
                        pull().
                        setCredentialsProvider(configProvider.getUsernamePasswordCredentialsProvider()).
                        setRemoteBranchName(configProvider.getBranch()).
                        setProgressMonitor(configProvider.getProgressInstance()).
                        call());
                LOG.info("pull succeed!");
                return true;
            } catch (GitAPIException | IOException e) {
                LOG.error(ExceptionUtil.stacktraceToString(e));
                git.set(null);
                return false;
            }
        });
        if (!success) {
            throw new Exception("git clone fail");
        }
        return git.get();
    }

    private void mkStoreDir() {
        String dir = configProvider.
                getProjectFile().
                getParent();
        boolean exits = FileUtil.exist(dir);
        if (!exits) {
            FileUtil.mkdir(dir);
        }
    }

    private boolean tryRetry(ExecuteFunction execute) {
        AtomicInteger currentAttempt = new AtomicInteger(0);
        Predicate<Boolean> retryLogic = isSuccess -> {
            if (!isSuccess && currentAttempt.get() < MAX_ATTEMPTS) {
                try {
                    Thread.sleep(DELAY_IN_MILLIS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (currentAttempt.get() > 0) {
                    LOG.info(StrUtil.format("{} continue retry....",
                            currentAttempt.get()));
                }
                currentAttempt.getAndIncrement();
                return true; // 继续重试
            } else {
                return false; // 不再重试
            }
        };
        boolean success = false;
        while (retryLogic.test(success)) {
            // 执行需要重试的逻辑
            success = execute.run();
        }
        return success;
    }
}
