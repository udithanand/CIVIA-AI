package civia.model;

public class StreetlightDefect extends Defect {
    private final boolean blackoutBlock;

    public StreetlightDefect(String id, String loc, double lat, double lon, boolean blackoutBlock) {
        super(id, loc, lat, lon);
        this.blackoutBlock = blackoutBlock;
    }

    @Override
    public String getCategory() { return "STREETLIGHT"; }

    @Override
    public String getAssignedDepartment() { return "Electrical & Grid Operations"; }

    public boolean isBlackoutBlock() { return blackoutBlock; }
}
