package com.xc.oj;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OjApplication.class})
class OjApplicationTests {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

    }
    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "JWTUserService")
    void contextLoads() throws Exception {
        log.info("info");
        log.warn("warn");
        log.error("error");
        ResultActions resultActions=mockMvc.perform(MockMvcRequestBuilders.get("/api/contest/list"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(MockMvcResultHandlers.print());

//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/list")
//                .contentType(MediaType.APPLICATION_JSON_UTF8).content("123"))
//                .andExpect(status().is(200))// 模拟向testRest发送get请求
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
//                .andReturn();// 返回执行请求的结果
//        log.info(result.toString());

    }

}
