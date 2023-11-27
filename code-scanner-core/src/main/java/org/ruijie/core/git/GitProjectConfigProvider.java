package org.ruijie.core.git;

import lombok.NonNull;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class GitProjectConfigProvider {
    private final String gitUrl;
    private final String branch;
    private final UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider;
    private  final String cloneLocalPath;
    private final File projectFile;
    private  final ProgressMonitor progressMonitor;

    public GitProjectConfigProvider(@NonNull String gitUrl,
                                    @NonNull String cloneLocalPath,
                                    @NonNull  String branch,
                                    @NonNull String username,
                                    @NonNull String password) {
        this.gitUrl = gitUrl;
        this.branch = branch;
        this.cloneLocalPath = cloneLocalPath;
        this.projectFile = new File(cloneLocalPath);
        this.usernamePasswordCredentialsProvider =
                new UsernamePasswordCredentialsProvider(username,password);
        this.progressMonitor = new  TextProgressMonitor(
                new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));
    }
    public UsernamePasswordCredentialsProvider getUsernamePasswordCredentialsProvider() {
        return usernamePasswordCredentialsProvider;
    }
    public String getGitUrl() {
        return gitUrl;
    }

    public String getBranch() {
        return branch;
    }

    public String getCloneLocalPath() {
        return cloneLocalPath;
    }
    public File getProjectFile() {
        return this.projectFile;
    }
    public ProgressMonitor getProgressInstance(){
        return  this.progressMonitor;
    }

    @Override
    public String toString() {
        return "GitProjectConfigProvider{" +
                "gitUrl='" + gitUrl + '\'' +
                ", branch='" + branch + '\'' +
                ", usernamePasswordCredentialsProvider=" + usernamePasswordCredentialsProvider +
                ", cloneLocalPath='" + cloneLocalPath + '\'' +
                ", projectFile=" + projectFile +
                '}';
    }
}
