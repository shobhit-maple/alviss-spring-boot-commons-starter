package com.alviss.commons.dao.tracing;

import brave.Span;
import brave.Tracer;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

@Slf4j
public class DatabaseTracingListener
    implements PreInsertEventListener,
        PostInsertEventListener,
        PreUpdateEventListener,
        PostUpdateEventListener,
        PreDeleteEventListener,
        PostDeleteEventListener,
        PostLoadEventListener {

  private final Tracer tracer;

  private final ConcurrentHashMap<Object, Span> activeSpans = new ConcurrentHashMap<>();

  public DatabaseTracingListener(final Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public boolean onPreInsert(final PreInsertEvent event) {
    startSpan(event.getEntity(), "insert");
    return false;
  }

  @Override
  public void onPostInsert(final PostInsertEvent event) {
    finishSpan(event.getEntity(), "insert");
  }

  @Override
  public boolean onPreUpdate(final PreUpdateEvent event) {
    startSpan(event.getEntity(), "update");
    return false;
  }

  @Override
  public void onPostUpdate(final PostUpdateEvent event) {
    finishSpan(event.getEntity(), "update");
  }

  @Override
  public boolean onPreDelete(final PreDeleteEvent event) {
    startSpan(event.getEntity(), "delete");
    return false;
  }

  @Override
  public void onPostDelete(final PostDeleteEvent event) {
    finishSpan(event.getEntity(), "delete");
  }

  @Override
  public void onPostLoad(final PostLoadEvent event) {
    startAndFinishSpan(event.getEntity(), "select");
  }

  private void startSpan(final Object entity, final String operation) {
    Span span = tracer.nextSpan().name("db-operation").start();
    tracer.withSpanInScope(span);

    span.tag("db.operation", operation);
    span.tag("db.entity", entity.getClass().getSimpleName());
    span.annotate("start");

    activeSpans.put(entity, span);
  }

  private void finishSpan(final Object entity, final String operation) {
    Span span = activeSpans.remove(entity);
    if (span != null) {
      span.annotate("finish");
      log.debug(
          "Database operation '{}' on entity '{}' completed",
          operation,
          entity.getClass().getSimpleName());

      span.finish();
    } else {
      log.warn(
          "No active span found for entity '{}'. This might indicate a mismatch in pre/post event handling.",
          entity.getClass().getSimpleName());
    }
  }

  private void startAndFinishSpan(final Object entity, final String operation) {
    Span span = tracer.nextSpan().name("db-operation").start();
    try (Tracer.SpanInScope spanInScope = tracer.withSpanInScope(span)) {
      span.tag("db.operation", operation);
      span.tag("db.entity", entity.getClass().getSimpleName());
      span.annotate("start");

      log.debug(
          "Database operation '{}' on entity '{}'", operation, entity.getClass().getSimpleName());
    } finally {
      span.finish();
    }
  }

  @Override
  public boolean requiresPostCommitHandling(final EntityPersister entityPersister) {
    return false;
  }
}
