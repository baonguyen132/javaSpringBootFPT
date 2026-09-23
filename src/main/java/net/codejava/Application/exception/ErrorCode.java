package net.codejava.Application.exception;

public enum ErrorCode {

    USER_EXISTS(1001, "User already exists"),
    UNCATEGRORIZED_EXCEPTION(9999, "Uncategorized exception"),
    USER_VALIDATION(1002, "User validation failed"),
    PASSWORD_VALIDATION(1003, "Password validation failed"),
    INVALID_KEY(1004, "Invalid key"),

    USER_NOT_EXISTED(1006, "User not exist"),
    UN_AUTHENTICATED(1007 , "UnAuthenticated");

    private int code ;
    private String message ;

    ErrorCode(int code, String message) {
        this.code = code ;
        this.message = message ;
    }


    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
