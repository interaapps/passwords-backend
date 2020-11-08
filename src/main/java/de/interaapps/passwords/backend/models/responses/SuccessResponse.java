package de.interaapps.passwords.backend.models.responses;

public class SuccessResponse {
    public boolean success = false;

    public SuccessResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
