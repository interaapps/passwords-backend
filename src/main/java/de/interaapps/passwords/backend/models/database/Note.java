package de.interaapps.passwords.backend.models.database;

import org.javawebstack.orm.Model;
import org.javawebstack.orm.annotation.Column;
import org.javawebstack.orm.annotation.Dates;
import org.javawebstack.orm.annotation.Table;

import java.sql.Timestamp;


@Table("notes")
@Dates
public class Note extends Model {

    @Column
    public int id;

    @Column
    public String title;

    @Column
    public String content;

    @Column
    public int userId;

    @Column
    Timestamp createdAt;

    @Column
    Timestamp updatedAt;

}