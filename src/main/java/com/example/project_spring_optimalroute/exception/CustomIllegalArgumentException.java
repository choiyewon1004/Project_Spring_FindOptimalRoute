package com.example.project_spring_optimalroute.exception;

public class CustomIllegalArgumentException extends IllegalArgumentException{
    public boolean logging;

    private CustomIllegalArgumentException(String message, Exception ex, boolean logging) {
        super(message, ex);
        this.logging = logging;
    }

    public static CustomIllegalArgumentException message(String message) {
        return message(message, false);
    }

    public static CustomIllegalArgumentException message(String message, boolean logging) {
        return exception(message, null, logging);
    }

    public static CustomIllegalArgumentException exception(String message) {
        return exception(message, null, false);
    }

    public static CustomIllegalArgumentException exception(Exception e) {
        return exception(e, false);
    }

    public static CustomIllegalArgumentException exception(Exception e, boolean logging) {
        return exception(null, e, logging);
    }

    public static CustomIllegalArgumentException exception(String message, Exception e, boolean logging) {
        return new CustomIllegalArgumentException(message, e, logging);
    }
}
