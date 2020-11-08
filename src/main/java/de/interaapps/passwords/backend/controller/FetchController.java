package de.interaapps.passwords.backend.controller;

import de.interaapps.passwords.backend.models.database.Key;
import de.interaapps.passwords.backend.models.database.Password;
import de.interaapps.passwords.backend.models.responses.FetchResponse;
import de.interaapps.passwords.backend.models.responses.PasswordListResponse;
import org.javawebstack.framework.HttpController;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.Get;
import org.javawebstack.orm.Repo;

public class FetchController extends HttpController {
    @Get("/fetch")
    public FetchResponse fetch(Exchange exchange){
        FetchResponse fetchResponse = new FetchResponse();
        fetchResponse.user = exchange.attrib("user");
        fetchResponse.passwords = new PasswordListResponse();
        fetchResponse.keys = Repo.get(Key.class).where("userId", fetchResponse.user.id).all();

        fetchResponse.passwords.passwords = Repo.get(Password.class).where("userId", fetchResponse.user.id).all();

        return fetchResponse;
    }

    public PasswordListResponse getPasswordsRecursive(){
        return null;
    }
}
