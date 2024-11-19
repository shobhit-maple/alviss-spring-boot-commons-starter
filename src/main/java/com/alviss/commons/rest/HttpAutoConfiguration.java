package com.alviss.commons.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alviss.commons.api.GlobalExceptionHandler;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(HttpProperties.class)
public class HttpAutoConfiguration {

  private final ObjectMapper objectMapper;
  private final HttpProperties httpProperties;

  @LoadBalanced
  @Bean
  public RestTemplate restOrganization() {
    val mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
    val restOrganization = new RestTemplate();
    val factory = new HttpComponentsClientHttpRequestFactory();

    var connectionConfig =
        ConnectionConfig.custom()
            .setConnectTimeout(httpProperties.getConnectTimeout(), TimeUnit.MILLISECONDS)
            .setSocketTimeout(httpProperties.getSocketTimeout(), TimeUnit.MILLISECONDS)
            .build();
    var connectionManager =
        PoolingHttpClientConnectionManagerBuilder.create()
            .setMaxConnPerRoute(httpProperties.getMaxConnectRoute())
            .setMaxConnTotal(httpProperties.getMaxConnect())
            .setDefaultConnectionConfig(connectionConfig)
            .build();
    factory.setHttpClient(
        HttpClients.custom()
            .disableAutomaticRetries()
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setResponseTimeout(httpProperties.getSocketTimeout(), TimeUnit.MILLISECONDS)
                    .build())
            .setConnectionManager(connectionManager)
            .build());

    factory.setConnectionRequestTimeout(httpProperties.getConnectRequestTimeout());
    restOrganization.setRequestFactory(factory);

    val messageConverters = restOrganization.getMessageConverters();
    messageConverters.removeIf(m -> m.getClass().equals(MappingJackson2HttpMessageConverter.class));
    messageConverters.add(mappingJackson2HttpMessageConverter);

    return restOrganization;
  }

  @Bean
  public GlobalExceptionHandler exceptionHandler(final ObjectMapper objectMapper) {
    return new GlobalExceptionHandler(objectMapper);
  }
}
