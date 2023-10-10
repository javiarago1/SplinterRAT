package Packets.Servlet;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.ArrayList;
import java.util.List;


public class ServletConfiguration {



    private final List<ServletPacket> servletPackets = new ArrayList<>();

    private FilterHolder filterHolder;
    public void addServlet(ServletHolder servletHolder, String pathSpe){
        servletPackets.add(new ServletPacket(servletHolder, pathSpe));
    }

    public void addFilterHolder(FilterHolder filterHolder){
        this.filterHolder = filterHolder;
    }

    public FilterHolder getFilterHolder() {
        return filterHolder;
    }

    public List<ServletPacket> getServletPackets() {
        return servletPackets;
    }
}
