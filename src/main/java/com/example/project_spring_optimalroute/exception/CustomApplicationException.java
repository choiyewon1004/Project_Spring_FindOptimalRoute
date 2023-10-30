package com.example.project_spring_optimalroute.exception;


public class CustomApplicationException extends RuntimeException {
    public boolean logging = true;

    private CustomApplicationException(String message, Exception ex, boolean logging) {
        super(message, ex);
        this.logging = logging;
    }

    public static CustomApplicationException message(String message) {
        return message(message, true);
    }

    public static CustomApplicationException message(String message, boolean logging) {
        return exception(message, null, logging);
    }

    public static CustomApplicationException exception(Exception e) {
        return exception(e, true);
    }
    public static CustomApplicationException exception(String message, Exception e) {
        return exception(message, e, true);
    }
    public static CustomApplicationException exception(Exception e, boolean logging) {
        return exception(null, e, logging);
    }

    public static CustomApplicationException exception(String message, Exception e, boolean logging) {
        return new CustomApplicationException(message, e, logging);
    }
}