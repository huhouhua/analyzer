package com.ruijie.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruijie.scheduler.config.SchedulerDockerConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CodeScanJobTest {
    @Autowired
    private SchedulerDockerConfig dockerConfig;

    @Autowired
    private   ObjectMapper objectMapper;

//    @Test
//    public  void  TestCodeScanJob()  {
//        Job job = new CodeScanJob(dockerConfig,objectMapper);
//         try {
//             job.execute(new JobExecutionTest(objectMapper));
//             Assert.assertTrue(true);
//         }catch (JobExecutionException e){
//             Assert.fail(e.getMessage(),e);
//         }
//    }
}



