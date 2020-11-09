package de.interaapps.passwords.backend.controller;

import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.Folder;
import de.interaapps.passwords.backend.models.database.FolderUser;
import de.interaapps.passwords.backend.models.database.Key;
import de.interaapps.passwords.backend.models.database.Password;
import de.interaapps.passwords.backend.models.responses.FetchResponse;
import de.interaapps.passwords.backend.models.responses.PasswordListResponse;
import org.javawebstack.framework.HttpController;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.Get;
import org.javawebstack.orm.Repo;

import java.util.ArrayList;
import java.util.List;

public class FetchController extends HttpController {
    @Get("/fetch")
    public FetchResponse fetch(Exchange exchange) {
        FetchResponse fetchResponse = new FetchResponse();
        fetchResponse.user = exchange.attrib("user");
        fetchResponse.passwords = new PasswordListResponse();
        fetchResponse.keys = Repo.get(Key.class).where("userId", fetchResponse.user.id).all();

        fetchResponse.passwords.passwords = Repo.get(Password.class).where("userId", fetchResponse.user.id).all();

        fetchResponse.passwords = getPasswordsRecursive(fetchResponse.user, null);

        return fetchResponse;
    }

    public PasswordListResponse getPasswordsRecursive(User user, Folder parent){
        PasswordListResponse passwordListResponse = new PasswordListResponse();

        passwordListResponse.passwords = Repo.get(Password.class).where("userId", user.id).where("folder", (parent == null ? 0 : parent.id)).all();

        passwordListResponse.folders = new ArrayList<>();


        if (parent != null && parent.parentId != 0)
            passwordListResponse.parent = parent.parentId;

        System.out.println("USER: "+user.id);
        System.out.println("PARENT: "+(parent == null ? 0 : parent.id));
        System.out.println(Repo.get(FolderUser.class).where("userId", user.id).all().size());
        if (parent != null) {
            passwordListResponse.folder = parent;
            Repo.get(Folder.class).where("parentId", parent.id).all().forEach(folder -> {
                passwordListResponse.folders.add(getPasswordsRecursive(user, folder));
            });
        } else {
            for (FolderUser folderUser : Repo.get(FolderUser.class).where("userId", user.id).all()) {
                Folder folder = Repo.get(Folder.class)
                        .where("parentId", 0)
                        .where("id", folderUser.folderId).get();
                if (folder != null)
                    passwordListResponse.folders.add(getPasswordsRecursive(user, folder));
            }
        }

        return passwordListResponse;
    }
}
