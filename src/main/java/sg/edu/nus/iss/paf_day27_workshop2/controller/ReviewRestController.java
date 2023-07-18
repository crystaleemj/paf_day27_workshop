package sg.edu.nus.iss.paf_day27_workshop2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import sg.edu.nus.iss.paf_day27_workshop2.model.Review;
import sg.edu.nus.iss.paf_day27_workshop2.model.UpdateReview;
import sg.edu.nus.iss.paf_day27_workshop2.service.ReviewSvc;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping
public class ReviewRestController {

    @Autowired
    ReviewSvc svc;
    
    @PostMapping(path = "/review", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> postReview(Review review) {

        if (!svc.validateGameId(review.getGameId())) {
            return new ResponseEntity<String>(svc.idErrorJson().toString(), HttpStatus.NOT_FOUND);
        }

        if (review.getRating() < 1 || review.getRating() > 10){
            return new ResponseEntity<String>(svc.ratingErrorJson().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<String>(svc.postReview(review, review.getGameId()).toString(), HttpStatus.CREATED);
    }

    @PutMapping(path = "/review/{review_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateReview(@PathVariable String review_id, @RequestBody UpdateReview update) {
        
        if (!svc.checkReviewExists(review_id)) {
            return new ResponseEntity<String>(svc.reviewIdErrorJson().toString(), HttpStatus.NOT_FOUND);
        }

        Document doc = svc.prepareNewDocument(update.getComment(), update.getRating(), review_id);
        
        return new ResponseEntity<String>(svc.updateReview(doc, review_id).toString(), HttpStatus.CREATED);
    }

    @GetMapping(path = "/review/{review_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listReviewByID(@PathVariable String review_id) {

        Document doc = svc.findReviewByID(review_id);
        return new ResponseEntity<>(svc.listReviewJson(doc, review_id).toString(), HttpStatus.OK);
    }

    @GetMapping(path = "/review/{review_id}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listReviewHistory(@PathVariable String review_id) {

        Document doc = svc.findReviewByID(review_id);
        return new ResponseEntity<>(svc.listReviewHistory(doc, review_id).toString(), HttpStatus.OK);
    }
    
    
    
}
