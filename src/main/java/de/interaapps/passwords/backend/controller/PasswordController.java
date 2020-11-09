package de.interaapps.passwords.backend.controller;

import com.google.gson.Gson;
import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.Password;
import de.interaapps.passwords.backend.models.responses.SuccessResponse;
import org.javawebstack.framework.HttpController;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.PathPrefix;
import org.javawebstack.httpserver.router.annotation.Post;
import org.javawebstack.httpserver.router.annotation.Put;
import org.javawebstack.orm.Repo;

import java.util.Map;

@PathPrefix("/password")
public class PasswordController extends HttpController {

    @Put("")
    public SuccessResponse post(Exchange exchange){
        User user = exchange.attrib("user");
        Map<String, Object> parameters = exchange.attrib("parameters");

        int id  = -1;

        if (parameters.containsKey("id"))
            id = (int) (double) parameters.get("id");

        Password oldPassword = Repo.get(Password.class).where("id", id).get();

        Password password = new Password();

        if (oldPassword != null && oldPassword.userId == user.id) {
            System.out.println("YEA");
            password = oldPassword;
        }

        //password.folder
        password.userId = user.id;

        if (parameters.containsKey("name"))
            password.name = (String) parameters.get("name");
        if (parameters.containsKey("username"))
            password.username = (String) parameters.get("username");
        if (parameters.containsKey("password"))
            password.password = (String) parameters.get("password");
        if (parameters.containsKey("website"))
            password.website = (String) parameters.get("website");
        if (parameters.containsKey("description"))
            password.description = (String) parameters.get("description");

        System.out.println(new Gson().toJson(password));
        System.out.println("SAVING");
        Repo.get(Password.class).save(password);
        System.out.println("SAVED TO "+password.id);
        System.out.println(new Gson().toJson(password));

        return new SuccessResponse().setSuccess(true);
    }
}
