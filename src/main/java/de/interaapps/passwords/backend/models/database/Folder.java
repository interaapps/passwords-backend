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

    @Column
    public String name;

    @Column(size = 7)
    public String color;

    @Column
    public int parentId;

    @Column
    Timestamp createdAt;

    @Column
    Timestamp updatedAt;
}
