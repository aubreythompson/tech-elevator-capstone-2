package com.techelevator.tenmo.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST, reason = "User did not match the account to send from, or tried to send funds to themself.")

public class InvalidUserException extends Exception{
}
