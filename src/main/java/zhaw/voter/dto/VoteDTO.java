package zhaw.voter.dto;

public class VoteDTO {
    private Long id;
    private String userEmail;
    private Long optionId;

    public VoteDTO(Long id, String userEmail, Long optionId) {
        this.id = id;
        this.userEmail = userEmail;
        this.optionId = optionId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }
}
