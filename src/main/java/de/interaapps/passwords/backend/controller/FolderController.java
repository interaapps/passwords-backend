package de.interaapps.passwords.backend.controller;

import com.google.gson.Gson;
import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.Folder;
import de.interaapps.passwords.backend.models.database.FolderUser;
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

@PathPrefix("/folder")
public class FolderController extends HttpController {
    @Put("")
    public SuccessResponse putFolder(Exchange exchange){
        SuccessResponse successResponse = new SuccessResponse();
        User user = exchange.attrib("user");
        Map<String, Object> parameters = exchange.attrib("parameters");

        int id  = -1;

        if (parameters.containsKey("id"))
            id = (int) (double) parameters.get("id");

        Folder oldFolder = Repo.get(Folder.class).where("id", id).get();

        Folder folder = new Folder();

        if (oldFolder != null && userInFolder(user, oldFolder)) {
            folder = oldFolder;
        }


        if (parameters.containsKey("name"))
            folder.name = (String) parameters.get("name");

        if (parameters.containsKey("color") && ((String) parameters.get("color")).length() == 7)
            folder.color = (String) parameters.get("color");


        if (oldFolder == null) {
            if (parameters.containsKey("parent") && userInFolder(user.id, (int) parameters.get("parent")))
                folder.parentId = (int) parameters.get("parent");
        }

        folder.save();


        successResponse.setExtra("updated", true);
        if (oldFolder == null) {
            FolderUser folderUser = new FolderUser();
            folderUser.userId   = user.id;
            folderUser.folderId = folder.id;
            folderUser.type     = FolderUser.Type.OWNER;
            folderUser.save();
            successResponse.setExtra("updated", false);
            successResponse.setExtra("id", folder.id);
        }

        return successResponse.setSuccess(true);
    }

    @Delete("/{i+:id}")
    public SuccessResponse delete(Exchange exchange, @Path("id") int id){
        User user = exchange.attrib("user");

        Folder folder = Repo.get(Folder.class).where("id", id).get();

        if (folder != null && userInFolder(user, folder)) {
            folder.delete();
            return new SuccessResponse().setSuccess(true);
        }
        return new SuccessResponse().setSuccess(false);
    }

    public static Folder getTopFolder(Folder folder){
        if (folder != null && folder.parentId != 0) {
            return getTopFolder(Repo.get(Folder.class).get(folder.parentId));
        }
        return folder;
    }

    public static boolean userInFolder(int user, Folder folder){
        FolderUser folderUser = Repo.get(FolderUser.class)
                .where("userId", user)
                .where("folderId", getTopFolder(folder).id)
                .get();
        if (folderUser != null) {
            return true;
        }
        return false;
    }

    public static boolean userInFolder(int user, int folder){
        return userInFolder(user, Repo.get(Folder.class).get(folder));
    }

    public static boolean userInFolder(User user, Folder folder){
        return userInFolder(user.id, folder);
    }
}
