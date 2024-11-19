package com.alviss.commons.api.handler;

import com.alviss.commons.api.model.Response;

public interface PostHandler<Req, Res, Ent> extends ResponseBuilder<Res, Ent>, RequestValidator<Ent> {

  Response<Res> build(Req req);
  Ent requestToEntity(Req req);
}
