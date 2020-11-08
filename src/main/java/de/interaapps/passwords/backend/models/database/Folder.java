package de.interaapps.passwords.backend.models.database;

import org.javawebstack.orm.Model;
import org.javawebstack.orm.annotation.Column;
import org.javawebstack.orm.annotation.Dates;
import org.javawebstack.orm.annotation.Table;

import java.sql.Timestamp;

@Table("folders")
@Dates
public class Folder extends Model {
    @Column
    public int id;

    // Global Folders encryption key. (Client-Encrypted by a FOLDER-Key)
    @Column
    public String key;

    @Column
    public int parentId;

    @Column
    Timestamp createdAt;

    @Column
    Timestamp updatedAt;
}
