package civia.service;

import civia.model.Defect;

public class HaversineDeduplicationService {
    private static final double METERS_LIMIT = 50.0;

    public static boolean isSpatialDuplicate(Defect d1, Defect d2) {
        final int R = 6371000;
        double dLat = Math.toRadians(d2.getLatitude() - d1.getLatitude());
        double dLon = Math.toRadians(d2.getLongitude() - d1.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(d1.getLatitude())) * Math.cos(Math.toRadians(d2.getLatitude()))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (R * c) <= METERS_LIMIT;
    }
}
