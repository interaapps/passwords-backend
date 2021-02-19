package de.interaapps.passwords.backend.controller;

import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.Note;
import de.interaapps.passwords.backend.models.responses.SuccessResponse;
import org.javawebstack.framework.HttpController;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.Delete;
import org.javawebstack.httpserver.router.annotation.Path;
import org.javawebstack.httpserver.router.annotation.PathPrefix;
import org.javawebstack.httpserver.router.annotation.Put;
import org.javawebstack.orm.Repo;

import java.util.Map;

@PathPrefix("/note")
public class NotesController extends HttpController {

    @Put("")
    public SuccessResponse post(Exchange exchange){
        SuccessResponse successResponse = new SuccessResponse();
        User user = exchange.attrib("user");
        Map<String, Object> parameters = exchange.attrib("parameters");

        int id  = -1;

        if (parameters.containsKey("id"))
            id = (int) (double) parameters.get("id");

        Note oldNote = Repo.get(Note.class).where("id", id).first();

        Note note = new Note();

        successResponse.setExtra("updated", false);
        if (oldNote != null && oldNote.userId == user.id) {
            note = oldNote;
            successResponse.setExtra("updated", true);
        }

        note.userId = user.id;

        if (parameters.containsKey("title"))
            note.title = (String) parameters.get("title");
        if (parameters.containsKey("content"))
            note.content = (String) parameters.get("content");

        note.save();

        successResponse.setExtra("id", note.id);

        return successResponse.setSuccess(true);
    }

    @Delete("/{i+:id}")
    public SuccessResponse delete(Exchange exchange, @Path("id") int id){
        User user = exchange.attrib("user");

        Note note = Repo.get(Note.class).where("id", id).first();

        if (note != null && note.userId == user.id) {
            note.delete();
            return new SuccessResponse().setSuccess(true);
        }
        return new SuccessResponse().setSuccess(false);
    }
}
