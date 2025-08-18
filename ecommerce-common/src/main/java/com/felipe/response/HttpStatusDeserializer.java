package com.felipe.response;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class HttpStatusDeserializer extends JsonDeserializer<HttpStatus> {

  @Override
  public HttpStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
    if(p.getCurrentToken().isNumeric()) {
      return HttpStatus.valueOf(p.getIntValue());
    }
    return null;
  }
}
