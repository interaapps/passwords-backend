package de.interaapps.passwords.backend.models.responses;

import java.util.HashMap;
import java.util.Map;

public class SuccessResponse {
    public boolean success = false;
    public Map<String, Object> extra = new HashMap<>();

    public SuccessResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public SuccessResponse setExtra(String key, Object value) {
        this.extra.put(key, value);
        return this;
    }
}
