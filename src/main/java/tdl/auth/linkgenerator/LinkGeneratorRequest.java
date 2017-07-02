package tdl.auth.linkgenerator;

public class LinkGeneratorRequest {

    private String username;
    private Integer validityDays;

    public LinkGeneratorRequest(String username, Integer validityDays) {
        this.username = username;
        this.validityDays = validityDays;
    }

    public String getUsername() {
        return username;
    }

    public Integer getValidityDays() {
        return validityDays;
    }
}
