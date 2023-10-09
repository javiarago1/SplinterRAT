package Server;

import org.eclipse.jetty.servlet.ServletHolder;

public record ServletPacket(ServletHolder servletHolder, String pathSpec){

}
