package com.felipe.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * Class to provide a default API response.
 * <p>This class uses {@link HttpStatus} to return the http response status code
 * and {@link ResponseType} to indicate whether the response is an error or success.</p>
 *
 * <p>The {@code T} parameter is the type of the payload in the response body.
 * If a payload is not needed, we can pass a {@code null} value to it.
 * We should pass the {@code Void} type as the {@code T} generic parameter
 * in the {@link Builder}:
 * </p>
 * <pre class="code">
 *  ResponsePayload&lt;Void&gt; response = new ResponsePayload.Builder&lt;Void&gt;()
 *    .status(ResponseConditionStatus.SUCCESS)
 *    .code(HttpStatus.OK)
 *    .message("Success response")
 *    .payload(null)
 *    .build();
 * </pre>
 *
 * @param <T> the payload type
 */
public class ResponsePayload<T> {
  private final String type;
  private final int code;
  private final String message;
  private final T payload;

  private ResponsePayload(Builder<T> builder) {
    this.type = builder.type;
    this.code = builder.code;
    this.message = builder.message;
    this.payload = builder.payload;
  }

  /**
   * Return the text value of the response type.
   *
   * @return the response type as a String value
   */
  @Schema(name = "type", type = "string")
  public String getType() {
    return this.type;
  }

  /**
   * Return the HTTP status code of the response.
   *
   * @return the HTTP status as an int value
   */
  @Schema(name = "code", type = "integer", format = "int32")
  public int getCode() {
    return this.code;
  }

  /**
   * Return the response message.
   *
   * @return the message as a String value
   */
  @Schema(name = "message", type = "string")
  public String getMessage() {
    return this.message;
  }

  /**
   * Return the payload of the response.
   *
   * @return the payload of {@code T} type
   */
  @Schema(name = "payload", type = "object")
  public T getPayload() {
    return this.payload;
  }

  /**
   * Builder class to build a new instance of {@code ResponsePayload}
   *
   * @param <T> the payload type
   */
  // This T type declaration is from static context (static class Builder<T>)
  // It's not the same from outer class (class ResponsePayload<T>)
  // They just have the same name T (T stands for Type)
  public static class Builder<T> {
    private String type;
    private int code;
    private String message;
    private T payload;

    public Builder() {}

    /**
     * Create a builder with the given response type.
     *
     * @param type the response type
     * @return the created builder
     */
    public Builder<T> type(ResponseType type) {
      this.type = type.getText();
      return this;
    }

    /**
     * Create a builder with the given http status code.
     *
     * @param code the HTTP status code
     * @return the created builder
     */
    public Builder<T> code(HttpStatus code) {
      this.code = code.value();
      return this;
    }

    /**
     * Create a builder with the given message.
     *
     * @param message the response message
     * @return the created builder
     */
    public Builder<T> message(String message) {
      this.message = message;
      return this;
    }

    /**
     * Create a builder with the given payload (possibly null).
     *
     * @param payload the response payload
     * @return the created builder
     */
    public Builder<T> payload(@Nullable T payload) {
      this.payload = payload;
      return this;
    }

    /**
     * Create a new instance of {@code ResponsePayload} with the
     * given builder fields.
     *
     * @return a new instance of {@code ResponsePayload}
     */
    public ResponsePayload<T> build() {
      return new ResponsePayload<>(this);
    }
  }
}
