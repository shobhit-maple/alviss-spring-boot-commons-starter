package com.alviss.commons.api.handler;

import com.alviss.commons.api.model.Response;
import java.util.List;

public interface PostManyHandler<Req, Res, Ent>
    extends ResponseBuilder<Res, Ent>, RequestValidator<Ent> {

  Response<List<Res>> build(Req req);

  List<Ent> requestToEntity(Req req);
}
