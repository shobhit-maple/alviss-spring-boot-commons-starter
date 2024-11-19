package com.alviss.commons.api.handler;

import com.alviss.commons.api.model.Response;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface GetAllHandler<Res, Ent> extends ResponseBuilder<Res, Ent> {

  Response<List<Res>> build(String parentId, Pageable pageable);
}
