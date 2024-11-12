package zhaw.voter.dto;

public class VoteCountDTO {
    private Long optionId;
    private int votes;

    public VoteCountDTO(Long optionId, int votes) {
        this.optionId = optionId;
        this.votes = votes;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
