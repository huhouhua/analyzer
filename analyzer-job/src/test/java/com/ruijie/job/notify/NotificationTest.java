package com.ruijie.job.notify;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NotificationTest {

    @Autowired
    private  Notification notification;

//    @Test
//    public  void  notificationTest(){
//        NotificationBuilder builder = NotificationBuilder.newBuilder().
//                withMsgType("text").
//                withContent("测试");
//        notification.send(builder);
//        Assert.assertTrue(true);
//    }
}
