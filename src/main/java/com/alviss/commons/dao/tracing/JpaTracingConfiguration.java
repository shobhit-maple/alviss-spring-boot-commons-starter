package com.alviss.commons.dao.tracing;

import brave.Tracer;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@ConditionalOnProperty(
    prefix = "tracing.db",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class JpaTracingConfiguration {

  private final EntityManagerFactory entityManagerFactory;
  private final Tracer tracer;

  public JpaTracingConfiguration(
      final EntityManagerFactory entityManagerFactory, final Tracer tracer) {
    this.entityManagerFactory = entityManagerFactory;
    this.tracer = tracer;
  }

  @PostConstruct
  public void configureGlobalEntityListeners() {
    SessionFactoryImpl sessionFactory = null;
    try {
      sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
    } catch (Exception e) {
      throw new IllegalStateException(
          "Failed to unwrap Hibernate SessionFactory from EntityManagerFactory", e);
    }

    if (sessionFactory == null) {
      throw new IllegalStateException(
          "SessionFactory is null. Unable to configure Hibernate event listeners.");
    }

    val eventListenerRegistry =
        sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
    if (eventListenerRegistry == null) {
      throw new IllegalStateException(
          "EventListenerRegistry is null. Unable to configure Hibernate event listeners.");
    }

    val listener = new DatabaseTracingListener(tracer);

    eventListenerRegistry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(listener);
    eventListenerRegistry.getEventListenerGroup(EventType.POST_INSERT).appendListener(listener);
    eventListenerRegistry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(listener);
    eventListenerRegistry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(listener);
    eventListenerRegistry.getEventListenerGroup(EventType.PRE_DELETE).appendListener(listener);
    eventListenerRegistry.getEventListenerGroup(EventType.POST_DELETE).appendListener(listener);
    eventListenerRegistry.getEventListenerGroup(EventType.POST_LOAD).appendListener(listener);
  }
}
