package org.ruijie.core.git;

import lombok.NonNull;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class  GitProjectConfigProvider  {
    private final String gitUrl;
    private final String branch;
    private final CredentialsConfigProvider credentialsConfigProvider;
    private  final String cloneLocalPath;
    private final File projectFile;
    private  final ProgressMonitor progressMonitor;

    public GitProjectConfigProvider(@NonNull String gitUrl,
                                    @NonNull String cloneLocalPath,
                                    @NonNull  String branch,
                                    CredentialsConfigProvider credentialsConfigProvider) {
        this.gitUrl = gitUrl;
        this.branch = branch;
        this.cloneLocalPath = cloneLocalPath;
        this.projectFile = new File(cloneLocalPath);
        this.credentialsConfigProvider = credentialsConfigProvider;

        this.progressMonitor = new  TextProgressMonitor(
                new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));
    }

    public CredentialsConfigProvider getCredentialsConfigProvider() {
        return credentialsConfigProvider;
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


}
