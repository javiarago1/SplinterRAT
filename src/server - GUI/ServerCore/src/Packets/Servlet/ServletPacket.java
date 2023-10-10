package Packets.Servlet;

import org.eclipse.jetty.servlet.ServletHolder;

public record ServletPacket(ServletHolder servletHolder, String pathSpec){

}
