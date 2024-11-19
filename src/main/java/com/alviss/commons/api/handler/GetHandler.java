package com.alviss.commons.api.handler;

import com.alviss.commons.api.model.Response;

public interface GetHandler<Res, Ent> extends ResponseBuilder<Res, Ent> {

  Response<Res> build(String id);
}
