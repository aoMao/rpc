package com.tt.gate.runner;

import com.tt.core.net.server.TcpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(1)
@Component
public class GateAppRunner implements ApplicationRunner {

    @Autowired
    private TcpServer tcpServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        tcpServer.start();
        log.info("server start on port : {} success", tcpServer.getPort());
    }
}
