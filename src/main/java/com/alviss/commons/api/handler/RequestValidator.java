package com.alviss.commons.api.handler;

public interface RequestValidator<Ent> {
  void validate(Ent ent);
}
