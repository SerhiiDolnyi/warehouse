package ua.foxminded.warehouse.util.exception.auth;


public class UserNotRegisteredException extends RuntimeException{
    public UserNotRegisteredException(String msg) {
        super(msg);
    }
}
