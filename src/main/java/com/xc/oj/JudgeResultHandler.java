package com.xc.oj;

import com.xc.oj.entity.JudgeResult;
import com.xc.oj.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class JudgeResultHandler extends Thread implements CommandLineRunner {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SubmissionService submissionService;

    @Override
    public void run() {
        while (true){
            try {
                JudgeResult judgeResult = (JudgeResult) redisTemplate.opsForList().rightPop("JudgeResult", 0, TimeUnit.SECONDS);
                if (judgeResult == null)
                    continue;
                submissionService.updateResult(judgeResult);
            }catch(Exception e){
//                System.out.println("tick-tok");
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
