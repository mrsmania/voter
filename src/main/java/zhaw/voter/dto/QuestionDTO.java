package zhaw.voter.dto;

import java.util.List;

public class QuestionDTO {
    private String text;
    private List<String> options;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
