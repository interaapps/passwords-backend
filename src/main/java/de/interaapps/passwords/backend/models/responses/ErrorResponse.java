package de.interaapps.passwords.backend.models.responses;

public class ErrorResponse extends SuccessResponse {
    public boolean error = true;
    public String message;

}
