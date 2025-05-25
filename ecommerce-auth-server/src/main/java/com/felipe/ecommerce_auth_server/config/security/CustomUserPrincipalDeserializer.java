package com.felipe.ecommerce_auth_server.config.security;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.io.IOException;
import java.util.UUID;

public class CustomUserPrincipalDeserializer extends JsonDeserializer<UserPrincipal> {

  @Override
  public UserPrincipal deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
    ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
    JsonNode jsonNode = mapper.readTree(jsonParser);
    String id = readJsonNode(jsonNode, "id").asText();
    String email = readJsonNode(jsonNode, "email").asText();
    String password = readJsonNode(jsonNode, "password").asText();
    String role = readJsonNode(jsonNode, "role").asText();

    return new UserPrincipal(UUID.fromString(id), email, password, role);
  }

  private JsonNode readJsonNode(JsonNode jsonNode, String field) {
    return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
  }
}
