package sg.edu.nus.iss.paf_day27_workshop2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReview {
    private Integer rating;
    private String comment;
}
