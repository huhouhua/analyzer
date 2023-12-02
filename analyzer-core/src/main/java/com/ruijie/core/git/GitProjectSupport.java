package com.ruijie.core.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.FetchResult;

public interface GitProjectSupport {
    Git cloneRepo() throws Exception;

    Ref checkout();

    FetchResult fetch() throws Exception;

    PullResult pull() throws Exception;
}
