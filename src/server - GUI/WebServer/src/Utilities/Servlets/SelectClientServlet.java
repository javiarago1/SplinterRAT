package Utilities.Servlets;

import Server.Client;
import Server.ConnectionStore;
import Updater.UpdaterInterface;
import Utilities.WebUpdater;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;

public class SelectClientServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        String jsonText = sb.toString();

        JSONObject jsonObject = new JSONObject(jsonText);


        resp.setContentType("application/json");
        resp.getWriter().println("{ \"status\": \"success\" }");
    }
}
