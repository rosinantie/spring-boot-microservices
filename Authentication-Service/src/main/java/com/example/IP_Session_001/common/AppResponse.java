package com.example.IP_Session_001.common;

public class AppResponse {

    private final Status status;
    private final Object data;

    public AppResponse(int code, String message, Object data) {
        this.status = new Status(code, message);
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    // ------------------------------------
    // STATIC FACTORY METHODS
    // ------------------------------------

    public static AppResponse success(String message, Object data) {
        return new AppResponse(2000, message, data);
    }

    public static AppResponse success(String message) {
        return new AppResponse(2000, message, null);
    }

    public static AppResponse error(String message, Object data) {
        return new AppResponse(4000, message, data);
    }

    public static AppResponse error(String message) {
        return new AppResponse(4000, message, null);
    }

    public static AppResponse unauthorized(String message) {
        return new AppResponse(4001, message, null);
    }

    public static AppResponse forbidden(String message) {
        return new AppResponse(4003, message, null);
    }

    public static AppResponse notFound(String message) {
        return new AppResponse(4004, message, null);
    }

    public static AppResponse conflict(String message) {
        return new AppResponse(4009, message, null);
    }

    public static AppResponse serverError(String message) {
        return new AppResponse(5000, message, null);
    }

    // ------------------------------------
    // NESTED STATUS CLASS
    // ------------------------------------

    public static class Status {
        private final int code;
        private final String message;

        public Status(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
