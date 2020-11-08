package de.interaapps.passwords.backend.models.database;

import com.google.gson.annotations.SerializedName;
import org.javawebstack.orm.Model;
import org.javawebstack.orm.annotation.Column;
import org.javawebstack.orm.annotation.Dates;
import org.javawebstack.orm.annotation.Table;

import java.sql.Timestamp;

@Table("keys")
@Dates
public class Key extends Model {

    @Column
    public int id;

    @Column
    public String name;

    @Column
    public String key;

    @Column
    public int userId;

    @Column
    public String type; // Using String because ORM is broken

    @Column
    public Timestamp createdAt;

    @Column
    public Timestamp updatedAt;

    public enum KeyType {
        RECOVERY,
        MASTER_PASSWORD,
        FOLDER;
    }
}
