package sg.edu.nus.iss.paf_day27_workshop2.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.UpdateResult;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import sg.edu.nus.iss.paf_day27_workshop2.model.Review;
import sg.edu.nus.iss.paf_day27_workshop2.repository.ReviewRepo;

@Service
public class ReviewSvc {
    @Autowired
    ReviewRepo repo;
    
    public ObjectId postReview(Review review, Integer id){
       return repo.postReview(review, id);
    }

    public Boolean validateGameId(Integer id){
        return repo.validateGameId(id);
    }

    public JsonObject idErrorJson(){
        JsonObjectBuilder object = Json.createObjectBuilder()
            .add("errormsg", "Game ID does not exist!");
        
            return object.build();
    }

    public JsonObject ratingErrorJson(){
        JsonObjectBuilder object = Json.createObjectBuilder()
            .add("errormsg", "Rating is not within range!");

            return object.build();
    }

    public Boolean checkReviewExists(String id){
        return repo.checkReviewExists(id);
    }

    public JsonObject reviewIdErrorJson(){
        JsonObjectBuilder object = Json.createObjectBuilder()
            .add("errormsg", "Review ID does not exist!");
            return object.build();
    }

    // convert Json -> Document
    public Document createDocumentFromJson(String comment, Integer rating){
        Document doc = new Document();
        doc.append("comment", comment);
        doc.append("rating", rating);
        doc.append("posted", new Date().toString());
        return doc;
    }

    public Document findReviewByID(String id){
        return repo.findReviewByID(id);
    }

    // combine original document and new edited document 
    public Document prepareNewDocument(String comment, Integer rating, String id){
        Document doc = findReviewByID(id);
        if (!checkIfEdited(id)) {
            List<Document> editedList = new ArrayList<>();
            editedList.add(createDocumentFromJson(comment, rating));
            doc.append("edited", editedList);
        }

        doc.getList("edited", Document.class)
        .add(createDocumentFromJson(comment, rating));
       
        return doc;
    }

    public Boolean updateReview(Document doc, String id){
        return repo.updateReview(doc, id);
    }

    public Boolean checkIfEdited(String id){
        return repo.checkIfEdited(id);
    }

    public JsonObject listReviewJson(Document doc, String id){
        doc = findReviewByID(id);
        List<Document> editedList = doc.getList("edited", Document.class);
        
        // get the last element in the list
        Document lastestReview = editedList.get(editedList.size() - 1);
        JsonObjectBuilder object = Json.createObjectBuilder()
        .add("user", doc.getString("user"))
        .add("rating", lastestReview.getInteger("rating"))
        .add("comment", lastestReview.getString("comment"))
        .add("id", doc.getInteger("id"))
        .add("posted", lastestReview.getString("posted"))
        .add("name",doc.getString("name"))
        .add("edited", checkIfEdited(id))
        .add("timestamp", new Date().toString());

        return object.build();
    }

    public JsonObject listReviewHistory(Document doc, String id){
        doc = findReviewByID(id);
        List<Document> editedList = doc.getList("edited", Document.class);

        JsonArrayBuilder array = Json.createArrayBuilder();
        for (Document document : editedList) {
            JsonObject obj = Json.createObjectBuilder()
            .add("comment", document.getString("comment"))
            .add("rating", document.getInteger("rating"))
            .add("posted", document.getString("posted"))
            .build();

            array.add(obj);
        }

        // get the last element in the list
        Document lastestReview = editedList.get(editedList.size() - 1);
        JsonObjectBuilder object = Json.createObjectBuilder()
        .add("user", doc.getString("user"))
        .add("rating", lastestReview.getInteger("rating"))
        .add("comment", lastestReview.getString("comment"))
        .add("id", doc.getInteger("id"))
        .add("posted", lastestReview.getString("posted"))
        .add("name",doc.getString("name"))
        .add("edited", array)
        .add("timestamp", new Date().toString());

        return object.build();
    }

   

}
