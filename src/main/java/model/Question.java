package model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {
    private Long id;
    private String text;
    private List<Answer> answers;
}
