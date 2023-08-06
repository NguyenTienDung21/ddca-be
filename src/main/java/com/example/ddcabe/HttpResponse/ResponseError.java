package com.example.ddcabe.HttpResponse;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseError {
    private final String msg; // The error message
    private final int code; // The error code
    private final Object data; // Optional data associated with the error

    /**
     * Constructs a new ResponseError object with the given error message, error code, and data.
     *
     * @param msg  The error message
     * @param code The error code
     * @param data Optional data associated with the error
     */
    public ResponseError(String msg, int code, Object data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    /**
     * Constructs a new ResponseError object with the given error message and error code.
     * The optional data is set to null.
     *
     * @param msg  The error message
     * @param code The error code
     */
    public ResponseError(String msg, int code) {
        this(msg, code, null);
    }
}