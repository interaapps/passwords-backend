package de.interaapps.passwords.backend.controller.user;

import com.google.gson.Gson;
import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.Key;
import de.interaapps.passwords.backend.models.responses.SuccessResponse;
import org.javawebstack.framework.HttpController;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.PathPrefix;
import org.javawebstack.httpserver.router.annotation.Post;
import org.javawebstack.orm.Repo;

import java.util.Map;

@PathPrefix("/user")
public class MasterPasswordController extends HttpController {
    @Post("/masterpassword")
    public SuccessResponse setMasterPassword(Exchange exchange){
        User user = exchange.attrib("user");
        Map<String, Object> parameters = exchange.attrib("parameters");

        Key oldKey = Repo.get(Key.class).where("userId", user.id).get();

        Key key = new Key();

        if (oldKey != null)
            key = oldKey;

        key.name = "APP.MASTER";
        key.userId = user.id;
        key.key  = (String) parameters.get("key");
        key.type = Key.KeyType.MASTER_PASSWORD.name();
        key.save();

        return new SuccessResponse().setSuccess(true);
    }
}
