package com.example.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() {
        log.info(Thread.currentThread().getName() + "...testA ");
        //try { TimeUnit.MILLISECONDS.sleep(800); } catch (InterruptedException e) { e.printStackTrace(); }
        return "testA-----";
    }

    @GetMapping("/testB")
    public String testB() {
        log.info(Thread.currentThread().getName() + "...testB ");
        return "testB   -----";
    }

    @GetMapping("/testD")
    public String testD(){
        log.info(Thread.currentThread().getName() + "...testD ");
        // testD每次需要1秒钟
        //try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        int a = 10/0;
        return "testD -----";
    }

    @GetMapping("/testE")
    public String testE() {
        log.info(Thread.currentThread().getName() + "...testE ");
        int a = 10/0;
        return "testE   -----";
    }

    // 测试热点限流
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "dealTestHotKey")
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                             @RequestParam(value = "p2", required = false) String p2){
        // int age = 10 /0;
        return "testHotKey -----";
    }

    // 兜底方法
    public String dealTestHotKey(String p1, String p2, BlockException blockException){
        return "p1=" + p1 + "-" + "p2=" + p2 + "dealTestHotKey---------";
    }


}