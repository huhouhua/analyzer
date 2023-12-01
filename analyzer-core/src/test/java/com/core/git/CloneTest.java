package com.core.git;

public class CloneTest {

//    @BeforeClass
//    @BeforeTest
//    public  void  setup(){
//        GitProjectFactory.createFeatureInstance(new GitProjectConfigProvider())
//    }

//    @Test
//    public void  TestSshSession(){
//        String currentDirectory = System.getProperty("user.dir");
//
//        String remoteURL = "git@172.17.189.70:devops/Demeter.git";
//        String privateKeyPath = String.format("%s\\src\\test\\java\\com\\core\\git\\testdata\\private_key",currentDirectory);
////        String passphrase = "passphrase";
//        // 设置 SSH 密钥
//        // 设置SSH会话工厂
//        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
//            @Override
//            protected void configure(OpenSshConfig.Host host, Session session) {
//                // 配置私钥文件路径
//                session.setConfig("StrictHostKeyChecking", "no");
//                session.setConfig("UserKnownHostsFile", "/dev/null");
//                session.setPassword("");
//            }
//
//            @Override
//            protected JSch createDefaultJSch(FS fs) throws JSchException {
//                JSch defaultJSch = super.createDefaultJSch(fs);
//                defaultJSch.addIdentity(privateKeyPath);
//                return defaultJSch;
//            }
//        };
//       SshSessionFactory.setInstance(sshSessionFactory);
//        try {
//            Git.cloneRepository()
//                    .setURI(remoteURL).setTransportConfigCallback(transport -> {
//                        SshTransport sshTransport = (SshTransport) transport;
//                        sshTransport.setSshSessionFactory(SshSessionFactory.getInstance());
//                    }).setProgressMonitor(new TextProgressMonitor(
//                            new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))))
//                    .setDirectory(new File("I:\\test\\project\\Demeter"))
//                    .call();
//            System.out.println("Clone completed.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
