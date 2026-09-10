package civia.model;

public class Citizen extends User {
    public Citizen(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public String getRole() { return "CITIZEN"; }
}
