package de.interaapps.passwords.backend.controller;

import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.Password;
import de.interaapps.passwords.backend.models.responses.SuccessResponse;
import org.javawebstack.framework.HttpController;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.Delete;
import org.javawebstack.httpserver.router.annotation.Path;
import org.javawebstack.httpserver.router.annotation.PathPrefix;
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

        Password oldPassword = Repo.get(Password.class).where("id", id).first();

        Password password = new Password();

        if (oldPassword != null && hasAccessToPassword(oldPassword.id, user.id)) {
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
        if (parameters.containsKey("totp"))
            password.totp = (String) parameters.get("totp");


        int folderId  = -1;

        if (parameters.containsKey("folder"))
            folderId = (int) (double) parameters.get("folder");

        if (FolderController.userInFolder(user.id, folderId)){
            password.folder = folderId;
        }

        Repo.get(Password.class).save(password);

        return new SuccessResponse().setSuccess(true).setExtra("id", password.id);
    }

    @Delete("/{i+:id}")
    public SuccessResponse delete(Exchange exchange, @Path("id") int id){
        User user = exchange.attrib("user");

        Password password = Repo.get(Password.class).where("id", id).first();

        if (password != null && hasAccessToPassword(password.id, user.id)) {
            password.delete();
            return new SuccessResponse().setSuccess(true);
        }
        return new SuccessResponse().setSuccess(false);
    }

    public static boolean hasAccessToPassword(Password password, int user){
        if (password != null) {
            if (password.userId == user)
                return true;
            if (password.folder != 0 && FolderController.userInFolder(user, password.folder))
                return true;
        }
        return false;
    }

    public static boolean hasAccessToPassword(int passwordId, int user){
        return hasAccessToPassword(Repo.get(Password.class).get(passwordId), user);
    }
}
