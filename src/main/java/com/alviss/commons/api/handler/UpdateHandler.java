package com.alviss.commons.api.handler;

import com.alviss.commons.api.model.Response;

public interface UpdateHandler<Req, Res, Ent>
    extends ResponseBuilder<Res, Ent>, RequestValidator<Ent> {

  Response<Res> build(String id, Req request);

  Ent requestToEntity(Ent ent, Req req);
}
