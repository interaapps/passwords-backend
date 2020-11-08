package de.interaapps.passwords.backend.models.responses;

public class ErrorResponse extends SuccessResponse {
    public boolean success = false;
    public boolean error = true;
    public String message;

    public ErrorResponse setSuccess(boolean success) {
        this.success = success;
        this.error = !success;
        return this;
    }
}
