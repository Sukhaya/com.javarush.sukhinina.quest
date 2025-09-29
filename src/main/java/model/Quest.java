package model;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Quest {
    private Long id;
    private String title;
    private List<Question> questions;
}
