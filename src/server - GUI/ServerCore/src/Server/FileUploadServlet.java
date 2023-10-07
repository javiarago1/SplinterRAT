package Server;

import Utils.UniqueByteIDGenerator;
import org.json.JSONObject;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        System.out.println(fileName);

        Part metadataPart = req.getPart("metadata");
        String metadata;
        try (Scanner scanner = new Scanner(metadataPart.getInputStream(), StandardCharsets.UTF_8)) {
            metadata = scanner.useDelimiter("\\A").next();
        }

        System.out.println("Metadata: " + metadata);
        JSONObject jsonObject = new JSONObject(metadata);

        String tempZipPath = "temp.zip";
        ZipFile zipFile = new ZipFile(tempZipPath);

        // 1. Write the received file to disk
        File receivedFile = new File(fileName);
        try (InputStream fileContent = filePart.getInputStream();
             FileOutputStream fos = new FileOutputStream(receivedFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Compress the file using zip4j
        try {
            zipFile.addFile(receivedFile);
        } catch (ZipException e) {
            e.printStackTrace();
            // Handle error - maybe return a response to client
            resp.setContentType("application/json");
            resp.getWriter().println("{ \"status\": \"error\", \"message\": \"Error compressing file\" }");
            return;
        }

        // 3. Send the compressed file
        try (FileInputStream fis = new FileInputStream(tempZipPath)) {
            Client client = ConnectionStore.connectionsMapIdentifiedByUUID.get(jsonObject.getString("client_id"));
            UniqueByteIDGenerator uniqueByteIDGeneratorOut = client.getUniqueByteIDGeneratorOut();
            byte id = uniqueByteIDGeneratorOut.getID();
            JSONObject json2 = new JSONObject();
            json2.put("ACTION", "PREPARE_UPLOAD");
            json2.put("channel_id", id);
            json2.put("to_path", jsonObject.get("to_path"));
            client.sendString(json2.toString());

            byte[] buffer = new byte[204800];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(bytesRead + 2);
                byteBuffer.put(id);
                byteBuffer.put((byte) (bytesRead < buffer.length ? 1 : 0));
                byteBuffer.put(buffer, 0, bytesRead);
                byteBuffer.flip();
                client.sendBytes(byteBuffer);
            }

            uniqueByteIDGeneratorOut.finishTask(id);
            resp.setContentType("application/json");
            resp.getWriter().println("{ \"status\": \"success\" }");
        } catch (IOException e) {
            e.printStackTrace();
            resp.setContentType("application/json");
            resp.getWriter().println("{ \"status\": \"error\" }");
        } finally {
            // Cleanup: Delete the temporary files
            new File(tempZipPath).delete();
            receivedFile.delete();
        }
    }
}
