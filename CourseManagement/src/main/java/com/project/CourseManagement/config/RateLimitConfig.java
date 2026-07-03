package com.project.CourseManagement.config;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket bucket(){
        Bandwidth limit = Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }
}


//        | Class       | Purpose                                 |
//        | ----------- | --------------------------------------- |
//        | `Bucket`    | Main object that controls rate limiting |
//        | `Bandwidth` | Defines rate limit rules                |
//        | `Refill`    | Defines how tokens refill               |
//        | `Bucket4j`  | Builder to create buckets               |




//There are three main refill strategies:
//
//greedy
//intervally
//intervallyAligned
//
//They differ in how and when tokens are refilled.

//        | Feature           | greedy         | intervally        | intervallyAligned   |
//        | ----------------- | -------------- | ----------------- | ------------------- |
//        | Refill style      | Gradual(1 by 1)| Batch             | Batch               |
//        | Refill timing     | Continuous     | After interval    | At fixed clock time |
//        | Traffic smoothing | Yes            | No                | No                  |
//        | Common usage      | API throttling | Simple rate limit | Quota systems       |


