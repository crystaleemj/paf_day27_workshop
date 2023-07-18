package sg.edu.nus.iss.paf_day27_workshop2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    
//      name – name of the person that posted the rating
//  rating – between 0 and 10
//  comment – this can be optional
//  game id – must be a valid game id from the games collection

    private String user;
    private Integer rating;
    private String comment;
    private Integer gameId;
}
