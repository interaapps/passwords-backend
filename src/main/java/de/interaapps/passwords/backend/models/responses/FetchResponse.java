package de.interaapps.passwords.backend.models.responses;

import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.Key;
import de.interaapps.passwords.backend.models.database.Note;

import java.util.List;

public class FetchResponse {
    public User user;
    public PasswordListResponse passwords;
    public List<Key> keys;
    public List<Note> notes;
}
