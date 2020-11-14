package de.interaapps.passwords.backend.controller.user;

import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.Key;
import de.interaapps.passwords.backend.models.responses.SuccessResponse;
import org.javawebstack.framework.HttpController;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.PathPrefix;
import org.javawebstack.httpserver.router.annotation.Put;
import org.javawebstack.orm.Repo;

import java.util.Map;

@PathPrefix("/key")
public class KeyController extends HttpController {
    @Put("")
    public SuccessResponse setMasterPassword(Exchange exchange){
        SuccessResponse successResponse = new SuccessResponse();
        User user = exchange.attrib("user");
        Map<String, Object> parameters = exchange.attrib("parameters");

        int id  = -1;

        if (parameters.containsKey("id"))
            id = (int) (double) parameters.get("id");

        Key oldKey = Repo.get(Key.class).where("userId", user.id).where("id", id).get();

        Key key = new Key();

        if (oldKey != null)
            key = oldKey;

        key.key = (String) parameters.get("key");

        successResponse.setExtra("updated", true);
        if (oldKey == null) {
            key.userId = user.id;
            key.type = Key.KeyType.valueOf((String) parameters.get("type")).name();
            key.name = (String) parameters.get("name");
            successResponse.setExtra("updated", false);
        }
        key.save();

        return successResponse.setSuccess(true);
    }
}
