package de.interaapps.passwords.backend.models.database;

import org.javawebstack.orm.Model;
import org.javawebstack.orm.annotation.Column;
import org.javawebstack.orm.annotation.Dates;
import org.javawebstack.orm.annotation.Table;

import java.sql.Timestamp;

@Table("sessions")
@Dates
public class UserSession extends Model {
    @Column
    public int id;

    @Column(size = 150)
    public String key;

    @Column
    public int userId;

    @Column
    public String userKey;

    @Column
    Timestamp createdAt;

    @Column
    Timestamp updatedAt;
}
