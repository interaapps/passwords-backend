package de.interaapps.passwords.backend.models.responses;

import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.Key;

import java.util.List;
import java.util.Map;

public class FetchResponse {
    public User user;
    public PasswordListResponse passwords;
    public List<Key> keys;
}
