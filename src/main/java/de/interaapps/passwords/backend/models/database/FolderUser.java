package de.interaapps.passwords.backend.models.database;

import org.javawebstack.orm.Model;
import org.javawebstack.orm.annotation.Column;
import org.javawebstack.orm.annotation.Dates;
import org.javawebstack.orm.annotation.Table;

import java.sql.Timestamp;

@Table("folder_users")
@Dates
public class FolderUser extends Model {
    @Column
    public int id;

    /**
     *  Folders id
     *  */
    @Column
    public int folderId;

    /**
     *  Users id
     *  */
    @Column
    public int userId;

    @Column
    Timestamp createdAt;

    @Column
    Timestamp updatedAt;

    public enum Type {
        USER,
        OWNER
    }
}
