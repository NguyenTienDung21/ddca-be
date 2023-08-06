package com.example.ddcabe.HttpResponse;

import lombok.Getter;
import lombok.ToString;

/**
 * ResponseBody class represents the structure of a response body in a Java Spring Boot application.
 * <p>
 * It includes data and error information to be included in the response.
 * <p>
 * The class is annotated with @Getter and @ToString to automatically generate getter methods and
 * <p>
 * a toString() method for convenient access and debugging.
 */
@Getter
@ToString
public class ResponseBody {
    private final Object data; // The data to be included in the response.
    private final ResponseError error; // An optional error object to be included in the response.

    /**
     * Constructs a ResponseBody object with both data and error.
     *
     * @param data  The data object to be included in the response.
     * @param error The error object to be included in the response (optional, can be null).
     */
    public ResponseBody(Object data, ResponseError error) {
        this.data = data;
        this.error = error;
    }

    /**
     * Constructs a ResponseBody object with only data and no error.
     * Calls the two-argument constructor with null error.
     *
     * @param data The data object to be included in the response.
     */
    public ResponseBody(Object data) {
        this(data, null);
    }

    /**
     * Constructs an empty ResponseBody object with no data and no error.
     * Sets the data and error objects to null, indicating no data and no error.
     */
    public ResponseBody() {
        this.data = null;
        this.error = null;
    }
}
