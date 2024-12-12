package com.alviss.commons.dao.tracing;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(prefix = "tracing.db", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(JpaTracingConfiguration.class)
@EnableConfigurationProperties(DatabaseTracingProperties.class)
public class DatabaseTracingAutoConfiguration {
}