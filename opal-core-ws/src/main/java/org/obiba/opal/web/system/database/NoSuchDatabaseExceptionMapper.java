package org.obiba.opal.web.system.database;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.obiba.opal.core.service.database.NoSuchDatabaseException;
import org.obiba.opal.web.magma.ClientErrorDtos;
import org.obiba.opal.web.provider.ErrorDtoExceptionMapper;
import org.springframework.stereotype.Component;

import com.google.protobuf.GeneratedMessage;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Component
@Provider
public class NoSuchDatabaseExceptionMapper extends ErrorDtoExceptionMapper<NoSuchDatabaseException> {

  @Override
  protected Response.Status getStatus() {
    return NOT_FOUND;
  }

  @Override
  protected GeneratedMessage.ExtendableMessage<?> getErrorDto(NoSuchDatabaseException exception) {
    return ClientErrorDtos.getErrorMessage(getStatus(), "NoSuchDatabase").addArguments(exception.getDatabaseName())
        .build();
  }

}