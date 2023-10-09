package Utilities.Servlets;

import Packets.Identificators.Category;
import Server.BytesChannel;
import Server.Client;
import Server.ConnectionStore;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;

public  class ByteChannelServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Scanner scanner = new Scanner(req.getInputStream()).useDelimiter("\\A");
        String body = scanner.hasNext() ? scanner.next() : "";
        JSONObject jsonRequest = new JSONObject(body);

        // Log to console
        System.out.println("Received JSON: " + jsonRequest.toString());

        String clientId = jsonRequest.getString("client_id");

        Client client = ConnectionStore.connectionsMap.get(clientId);
        BytesChannel bytesChannel = client.createFileChannel(Category.ZIP_FILE);

        // Create JSON response
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("channel_id", bytesChannel.getId());

        // Send response
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(jsonResponse);
    }
}