package com.MyProject.exception;

public class InvalidWorkHoursException extends RuntimeException{

    public InvalidWorkHoursException(String message){
        super(message);
    }
}
