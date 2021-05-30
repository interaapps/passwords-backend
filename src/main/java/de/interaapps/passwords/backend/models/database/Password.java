package de.interaapps.passwords.backend.models.database;

import org.javawebstack.orm.Model;
import org.javawebstack.orm.annotation.Column;
import org.javawebstack.orm.annotation.Dates;
import org.javawebstack.orm.annotation.Table;

import java.sql.Timestamp;

@Table("passwords")
@Dates
public class Password extends Model {

    @Column
    public int id;

    @Column
    public int userId;

    @Column
    public String name;

    @Column
    public String username;

    @Column
    public String password;

    @Column
    public String website;

    @Column
    public String description;

    @Column
    public String totp;

    @Column
    public int folder;

    @Column
    Timestamp createdAt;

    @Column
    Timestamp updatedAt;

}
