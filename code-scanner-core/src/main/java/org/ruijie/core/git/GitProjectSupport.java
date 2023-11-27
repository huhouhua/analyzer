package org.ruijie.core.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;

public interface GitProjectSupport {
    Git gitClone() throws Exception;
    PullResult pull() throws Exception;
}
