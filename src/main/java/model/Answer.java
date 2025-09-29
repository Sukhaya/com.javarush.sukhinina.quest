package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Answer {
    private Long id;
    private String text;
    private Long nextQuestionId;
    @JsonProperty("isWinning")
    private boolean isWinning;
    @JsonProperty("isLosing")
    private boolean isLosing;
    private String endText;
}
