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

public class ByteChannelServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Scanner scanner = new Scanner(req.getInputStream()).useDelimiter("\\A");
            String body = scanner.hasNext() ? scanner.next() : "";
            JSONObject jsonRequest = new JSONObject(body);

            System.out.println("Received JSON: " + jsonRequest.toString());

            String clientId = jsonRequest.getString("client_id");
            String category = jsonRequest.getString("category");

            Client client = ConnectionStore.connectionsMap.get(clientId);
            if (client == null) {
                throw new Exception("Client is null for clientId: " + clientId);
            }

            BytesChannel bytesChannel = client.createFileChannel(Category.valueOf(category));

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("channel_id", bytesChannel.getId());

            System.out.println(jsonResponse);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Error creating byte channel: " + e.getMessage());
        }
    }
}
