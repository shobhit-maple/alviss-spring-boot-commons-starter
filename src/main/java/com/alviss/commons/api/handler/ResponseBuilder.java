package com.alviss.commons.api.handler;

public interface ResponseBuilder<Res, Ent> {

  Res entityToResponse(Ent ent);
}
