package com.verify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class Web3Config {

    @Value("${blockchain.rpc}")
    private String rpc;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(rpc));
    }
}
