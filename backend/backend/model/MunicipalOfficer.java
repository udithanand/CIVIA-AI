package civia.model;

public class MunicipalOfficer extends User {
    public MunicipalOfficer(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public String getRole() { return "MUNICIPAL_OFFICER"; }
}
