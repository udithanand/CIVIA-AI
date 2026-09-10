package civia.model;

public abstract class Defect {
    private final String ticketId;
    private final String locationName;
    private final double latitude;
    private final double longitude;
    private TicketStatus status;
    private int priorityScore;

    public Defect(String ticketId, String locationName, double latitude, double longitude) {
        this.ticketId = ticketId;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = TicketStatus.SUBMITTED;
        this.priorityScore = 0;
    }

    public abstract String getCategory();
    public abstract String getAssignedDepartment();

    public void advanceStatus() {
        switch (this.status) {
            case SUBMITTED -> this.status = TicketStatus.AI_TRIAGED;
            case AI_TRIAGED -> this.status = TicketStatus.CREW_DISPATCHED;
            case CREW_DISPATCHED -> this.status = TicketStatus.RESOLVED;
            case RESOLVED -> System.out.println("Defect " + ticketId + " already resolved.");
        }
    }

    public String getTicketId() { return ticketId; }
    public String getLocationName() { return locationName; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public TicketStatus getStatus() { return status; }
    public int getPriorityScore() { return priorityScore; }
    public void setPriorityScore(int priorityScore) { this.priorityScore = priorityScore; }
}
