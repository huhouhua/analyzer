package com.ruijie.core.git;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.FileNotFoundException;

public class GitConfigSessionFactory  extends JschConfigSessionFactory {
    private  final String privateKeyFilePath;

    public  GitConfigSessionFactory(String privateKeyFilePath) throws FileNotFoundException {
        this.privateKeyFilePath = privateKeyFilePath;
        this.checkFile();
    }
    
    @Override
    protected void configure(OpenSshConfig.Host host, Session session) {
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("UserKnownHostsFile", "/dev/null");
        session.setPassword("");
    }

    @Override
    protected JSch createDefaultJSch(FS fs) throws JSchException {
        JSch defaultJSch = super.createDefaultJSch(fs);
        defaultJSch.addIdentity(privateKeyFilePath);
        return defaultJSch;
    }

    private void  checkFile() throws FileNotFoundException {
        File file = new File(privateKeyFilePath);
        if (!file.exists()){
            throw  new FileNotFoundException("git private key file does not exist! ");
        }
    }
}
