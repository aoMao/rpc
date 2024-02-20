package com.tt.gate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class GateServer {
    public static void main(String[] args) {
        SpringApplication.run(GateServer.class);
    }
}
