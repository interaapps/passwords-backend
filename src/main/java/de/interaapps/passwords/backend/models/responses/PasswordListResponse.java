package de.interaapps.passwords.backend.models.responses;

import de.interaapps.passwords.backend.models.database.Folder;
import de.interaapps.passwords.backend.models.database.Password;

import java.util.ArrayList;
import java.util.List;

public class PasswordListResponse {
    public List<Password> passwords = new ArrayList<>();
    public List<PasswordListResponse> folders = new ArrayList<>();
    public Folder folder;
    public String key = null; // If in Folder
    public String parent = null;
}
