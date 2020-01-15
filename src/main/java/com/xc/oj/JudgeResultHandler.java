package com.xc.oj;

import com.xc.oj.entity.JudgeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JudgeResultHandler extends Thread implements CommandLineRunner {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void run(){
        while (true){
            try {
                JudgeResult judgeResult = (JudgeResult) redisTemplate.opsForList().rightPop("JudgeResult", 0, TimeUnit.SECONDS);
                if (judgeResult == null)
                    continue;
                System.out.println(judgeResult.getSubmissionId());
                System.out.println(judgeResult.getExecuteTime());
                System.out.println(judgeResult.getExecuteMemory());
                for (Map<String, String> mp : judgeResult.getDetail()) {
                    for (String key : mp.keySet())
                        System.out.println(key + " " + mp.get(key));
                }
                System.out.println();
            }catch(Exception e){
                System.out.println("tick-tok");
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
