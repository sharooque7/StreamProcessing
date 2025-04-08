package org.ainzson.oops.enums;

public enum Status {
    SUCCESS(200),
    ERROR(500),
    NOT_FOUND(404);

    private final int code; // Enum field

    // Constructor
    Status(int code) {
        this.code = code;
    }

    // Getter method
    public int getCode() {
        return code;
    }
}
