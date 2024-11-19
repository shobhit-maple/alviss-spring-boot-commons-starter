package com.alviss.commons.rest;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("http.client")
public class HttpProperties {

    private int maxConnectRoute = 2;
    private int maxConnect = 8;
    private int connectRequestTimeout = 5000;
    private int connectTimeout = 5000;
    private int socketTimeout = 15000;
}
