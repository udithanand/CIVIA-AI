package civia.controller;

import civia.exception.DuplicateGrievanceException;
import civia.model.*;
import civia.repository.GrievanceRepository;
import civia.service.GrievanceDispatcher;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class CiviaWebController {
    private final GrievanceRepository repository;
    private final GrievanceDispatcher dispatcher;

    public CiviaWebController(GrievanceRepository repository, GrievanceDispatcher dispatcher) {
        this.repository = repository;
        this.dispatcher = dispatcher;
    }

    public HttpHandler getAuthHandler() {
        return exchange -> {
            CorsHandler.setCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                        .lines().collect(Collectors.joining("\n"));
                String role = body.contains("officer") ? "MUNICIPAL_OFFICER" : "CITIZEN";
                CorsHandler.sendResponse(exchange, 200, String.format("{\"access_token\":\"demo-token-xyz\",\"role\":\"%s\"}", role));
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        };
    }

    public HttpHandler getReportsHandler() {
        return exchange -> {
            CorsHandler.setCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            String method = exchange.getRequestMethod();

            if ("GET".equalsIgnoreCase(method)) {
                List<Defect> list = dispatcher.getPrioritizedQueue();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < list.size(); i++) {
                    Defect d = list.get(i);
                    json.append(String.format(
                        "{\"id\":\"%s\",\"type\":\"%s\",\"location\":\"%s\",\"priority\":%d,\"status\":\"%s\",\"department\":\"%s\",\"lat\":%f,\"lng\":%f}",
                        d.getTicketId(), d.getCategory(), d.getLocationName(), d.getPriorityScore(),
                        d.getStatus(), d.getAssignedDepartment(), d.getLatitude(), d.getLongitude()
                    ));
                    if (i < list.size() - 1) json.append(",");
                }
                json.append("]");
                CorsHandler.sendResponse(exchange, 200, json.toString());
            } else if ("POST".equalsIgnoreCase(method)) {
                String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                        .lines().collect(Collectors.joining("\n"));

                String nextId = "CIV-" + (repository.count() + 101);
                Defect defect = body.contains("STREETLIGHT")
                        ? new StreetlightDefect(nextId, "Ward Sector 4", 13.0840, 80.2710, true)
                        : new PotholeDefect(nextId, "Main Arterial Road", 13.0827, 80.2707, 4.5, true);

                try {
                    dispatcher.logGrievance(defect);
                    CorsHandler.sendResponse(exchange, 201, String.format("{\"ticket_id\":\"%s\",\"priority_score\":%d,\"status\":\"%s\"}",
                            defect.getTicketId(), defect.getPriorityScore(), defect.getStatus()));
                } catch (DuplicateGrievanceException e) {
                    CorsHandler.sendResponse(exchange, 409, "{\"error\":\"" + e.getMessage() + "\"}");
                }
            } else if ("PATCH".equalsIgnoreCase(method)) {
                String path = exchange.getRequestURI().getPath();
                String[] parts = path.split("/");
                if (parts.length >= 4) {
                    repository.findById(parts[3]).ifPresentOrElse(d -> {
                        d.advanceStatus();
                        try {
                            CorsHandler.sendResponse(exchange, 200, "{\"status\":\"" + d.getStatus() + "\"}");
                        } catch (IOException ignored) {}
                    }, () -> {
                        try {
                            CorsHandler.sendResponse(exchange, 404, "{\"error\":\"Ticket not found\"}");
                        } catch (IOException ignored) {}
                    });
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        };
    }
}
