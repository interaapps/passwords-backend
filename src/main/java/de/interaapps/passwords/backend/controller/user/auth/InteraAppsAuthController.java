package de.interaapps.passwords.backend.controller.user.auth;

import de.interaapps.passwords.backend.PasswordsBackend;
import de.interaapps.passwords.backend.models.User;
import de.interaapps.passwords.backend.models.database.UserSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.javawebstack.framework.HttpController;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.Get;
import org.javawebstack.httpserver.router.annotation.PathPrefix;

import java.net.URLEncoder;

@PathPrefix("/auth/ia")
public class InteraAppsAuthController extends HttpController {

    @Get("/login")
    public String login(Exchange exchange)  {
        if (exchange.rawRequest().getParameter("userkey") != null) {
            User user = PasswordsBackend.getInstance().getUserByKey(exchange.rawRequest().getParameter("userkey"));
            if (user.valid) {
                UserSession session = new UserSession();
                session.key = "S-NB01-"+RandomStringUtils.random(143, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyt0123456789");
                session.userId  = user.id;
                session.userKey = exchange.rawRequest().getParameter("userkey");
                session.save();
                String url = PasswordsBackend.getInstance().getConfig().get("app.frontend")+"/auth?authentication="+ URLEncoder.encode(session.key);
                exchange.redirect(url);
                return "REDIRECTING TO FRONTEND... <a href=\""+url+"\">CLICK HERE</a>";
            }
        }
        exchange.redirect("https://accounts.interaapps.de/iaauth/"+ PasswordsBackend.getInstance().getConfig().get("interaapps.auth.id"));
        return "a";
    }

}
