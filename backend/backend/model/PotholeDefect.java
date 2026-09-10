package civia.model;

public class PotholeDefect extends Defect {
    private final double depthInches;
    private final boolean highSpeedCorridor;

    public PotholeDefect(String id, String loc, double lat, double lon, double depthInches, boolean highSpeedCorridor) {
        super(id, loc, lat, lon);
        this.depthInches = depthInches;
        this.highSpeedCorridor = highSpeedCorridor;
    }

    @Override
    public String getCategory() { return "POTHOLE"; }

    @Override
    public String getAssignedDepartment() { return "Road Surface & Asphalt Division"; }

    public double getDepthInches() { return depthInches; }
    public boolean isHighSpeedCorridor() { return highSpeedCorridor; }
}
