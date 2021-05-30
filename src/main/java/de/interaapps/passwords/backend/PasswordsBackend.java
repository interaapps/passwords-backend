package de.interaapps.passwords.backend;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import de.interaapps.passwords.backend.controller.PasswordController;
import de.interaapps.passwords.backend.exceptions.AuthenticationException;
import de.interaapps.passwords.backend.exceptions.PageNotFoundException;
import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.*;
import de.interaapps.passwords.backend.models.responses.ErrorResponse;
import org.javawebstack.abstractdata.AbstractObject;
import org.javawebstack.command.CommandSystem;
import org.javawebstack.framework.HttpController;
import org.javawebstack.framework.WebApplication;
import org.javawebstack.framework.config.Config;
import org.javawebstack.httpclient.HTTPClient;
import org.javawebstack.httpserver.HTTPServer;
import org.javawebstack.httpserver.helper.HttpMethod;
import org.javawebstack.injector.SimpleInjector;
import org.javawebstack.orm.ORM;
import org.javawebstack.orm.ORMConfig;
import org.javawebstack.orm.Repo;
import org.javawebstack.orm.exception.ORMConfigurationException;
import org.javawebstack.orm.wrapper.SQL;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordsBackend extends WebApplication {

    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setExclusionStrategies(new ExclusionStrategy() {
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    return fieldAttributes.getAnnotation(Expose.class) != null && !fieldAttributes.getAnnotation(Expose.class).serialize();
                }
                public boolean shouldSkipClass(Class<?> aClass) { return false; }
            })
            .setPrettyPrinting().create();

    private HTTPClient interaAppsAccountsAPI;
    private static PasswordsBackend instance;

    public static void main(String[] args) {
        instance = new PasswordsBackend();

        instance.start();
    }

    @Override
    public void setupModules() {

    }

    public void setupConfig(Config config) {
        Map<String, String> map = new HashMap<>();
        map.put("INTERAAPPS_AUTH_KEY", "interaapps.auth.key");
        map.put("INTERAAPPS_AUTH_ID", "interaapps.auth.id");
        map.put("APP_FRONTEND", "app.frontend");
        config.addEnvKeyMapping(map);
        config.addEnvFile(new File(".env"));
    }

    public void setupModels(SQL sql) {
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        Logger.getLogger("ORM").addHandler(handler);
        Logger.getLogger("ORM").setLevel(Level.ALL);

        ORMConfig config = new ORMConfig().setTablePrefix("passwords_");
        try {
            ORM.register(Password.class, sql, config).autoMigrate();
            ORM.register(Key.class, sql, config).autoMigrate();
            ORM.register(Folder.class, sql, config).autoMigrate();
            ORM.register(FolderUser.class, sql, config).autoMigrate();
            ORM.register(UserSession.class, sql, config).autoMigrate();
            ORM.register(Note.class, sql, config).autoMigrate();

        } catch (ORMConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void setupServer(HTTPServer httpServer) {
        httpServer.controller(HttpController.class, PasswordController.class.getPackage());

        interaAppsAccountsAPI = new HTTPClient()
                .setBaseUrl("https://accounts.interaapps.de/iaauth/api");

        httpServer.beforeInterceptor(exchange ->{
            String hostname = "passwords-api";
            if (System.getenv("HOSTNAME") != null)
                hostname = System.getenv("HOSTNAME");
            exchange.header("server-name", hostname+", InteraApps-k8s");
            if (exchange.getMethod() != HttpMethod.GET)
                exchange.attrib("parameters", new Gson().fromJson(exchange.body(String.class), new TypeToken<Map<String, Object>>(){}.getType()));
            if (exchange.header("X-Key") != null) {
                User user = getUser(Repo.get(UserSession.class).where("key", exchange.header("X-Key")).first());
                exchange.attrib("user", user);
                return false;
            }
            if (exchange.rawRequest().getRequestURI().startsWith("/auth"))
                return false;
            throw new AuthenticationException();
        });

        httpServer.exceptionHandler((ex, e)->{
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.error = true;
            errorResponse.message = e.getClass().getSimpleName();
            return errorResponse;
        });

        httpServer.notFound(exchange -> {
            throw new PageNotFoundException();
        });
    }

    @Override
    protected void setupCommands(CommandSystem commandSystem) {

    }

    public HTTPClient getInteraAppsAccountsAPI() {
        return interaAppsAccountsAPI;
    }

    public static PasswordsBackend getInstance() {
        return instance;
    }

    public User getUserByKey(String userKey){

        System.out.println(interaAppsAccountsAPI
                .post("/getuserinformation")
                .body(new AbstractObject()
                        .set("userkey", userKey)
                        .set("key", getConfig().get("interaapps.auth.key"))
                        .toFormData().toString()).string());

        return interaAppsAccountsAPI
                .post("/getuserinformation")
                .body(new AbstractObject()
                        .set("userkey", userKey)
                        .set("key", getConfig().get("interaapps.auth.key"))
                        .toFormData().toString())
                .object(User.class);
    }

    public User getUser(UserSession userSession) {
        System.out.println("AAAAAAAAAAAHHHH");
        return getUserByKey(userSession.userKey);
    }

}
