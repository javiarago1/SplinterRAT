package Main;

import Server.Server;
import Server.ConnectionStore;
import Utilities.Servlets.*;
import Utilities.WebUpdaterFactory;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import Packets.Servlet.ServletConfiguration;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.MultipartConfigElement;

public class Main {

    public static Server server;
    public static void main(String[] args) {
        ConnectionStore.updaterFactory = new WebUpdaterFactory();
        ServletConfiguration servletConfiguration = new ServletConfiguration();
        ServletHolder fileUploadServlet = new ServletHolder(new FileUploadServlet());
        long maxFileSize = 10 * 1024 * 1024; // 10 MB
        long maxRequestSize = 10 * 1024 * 1024; // 10 MB
        int fileSizeThreshold = 64 * 1024; // 64 KB
        MultipartConfigElement multipartConfig = new MultipartConfigElement("",maxFileSize, maxRequestSize, fileSizeThreshold);
        fileUploadServlet.getRegistration().setMultipartConfig(multipartConfig);
        servletConfiguration.addServlet(fileUploadServlet,"/upload-files");
        servletConfiguration.addServlet(new ServletHolder(new WebSocketServlet()),"/web");
        servletConfiguration.addServlet(new ServletHolder(new ByteChannelServlet()),"/create-byte-channel");
        servletConfiguration.addServlet(new ServletHolder(new SelectClientServlet()),"/select-client");
        servletConfiguration.addServlet(new ServletHolder(new CompilationServlet()), "/compile");
        servletConfiguration.addServlet(new ServletHolder(new FileDownloadServlet()), "/downloads/*");


        // TODO PLEASE FIX THIS (T＿T)
        FilterHolder cors = new FilterHolder(CrossOriginFilter.class);
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,POST,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        servletConfiguration.addFilterHolder(cors);
        server = new Server(3055, servletConfiguration);
        server.startServer();
    }
}