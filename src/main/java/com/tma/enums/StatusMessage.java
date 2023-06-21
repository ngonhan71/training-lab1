package com.tma.enums;

public enum StatusMessage {

    NOT_FOUND(404),
    BAD_REQUEST(400),
    FORBIDDEN(403),
    UNAUTHORIZED(401),
    CONTENT_TOO_LARGE(413);
    public final int label;

    StatusMessage(int label) {
        this.label = label;
    }

}
