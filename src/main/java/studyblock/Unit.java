package studyblock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    private String block;
    private int number;
    private String question;
    private String answer;
    private String videoLink;
}
