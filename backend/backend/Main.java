package civia;

import civia.controller.CiviaWebController;
import civia.repository.GrievanceRepository;
import civia.repository.InMemoryGrievanceRepository;
import civia.service.CiviaTriageEngine;
import civia.service.GrievanceDispatcher;
import civia.service.TriageCalculable;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        GrievanceRepository repository = new InMemoryGrievanceRepository();
        TriageCalculable triageEngine = new CiviaTriageEngine();
        GrievanceDispatcher dispatcher = new GrievanceDispatcher(repository, triageEngine);

        CiviaWebController controller = new CiviaWebController(repository, dispatcher);
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/api/auth/login", controller.getAuthHandler());
        server.createContext("/api/reports", controller.getReportsHandler());
        server.createContext("/api/report", controller.getReportsHandler());

        server.setExecutor(null);
        System.out.println("=================================================");
        System.out.println("  CIVIA AI Java OOP Backend running on port " + PORT);
        System.out.println("  Listening for React UI requests at localhost:8000");
        System.out.println("=================================================");
        server.start();
    }
}
