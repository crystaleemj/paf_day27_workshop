package sg.edu.nus.iss.paf_day27_workshop2.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import sg.edu.nus.iss.paf_day27_workshop2.model.Review;

@Repository
public class ReviewRepo {
    
    @Autowired
    MongoTemplate template;

    // Document newDoc = mongoTemplate.insert(toInsert, “tv_shows”);
    // ObjectId id = newDoc.getObjectId()
    public ObjectId postReview(Review review, Integer id){
        Document doc = new Document();
        doc.append("user", review.getUser());
        doc.append("rating", review.getRating());
        doc.append("comment", review.getComment());
        doc.append("id", review.getGameId());
        doc.append("posted", new Date().toString());
        doc.append("name",listGameById(id));

        doc = template.insert(doc,"reviews");
        return  doc.getObjectId("_id");
    }

    // check if game id exists from game collection
    public Boolean validateGameId(Integer id){
        Criteria c = Criteria.where("gid").is(id);
        Query q = Query.query(c);

        return template.exists(q, "games");
    }

    // get game name per id
    public String listGameById(Integer id){
        Criteria c = Criteria.where("gid").is(id);
        Query q = Query.query(c);

        Document doc = template.findOne(q, Document.class, "games");
        return doc.getString("name");
    }

    // check if review exists for updating
    public Boolean checkReviewExists(String id){
        Criteria c = Criteria.where("_id").is(id);
        Query q = Query.query(c);

        return template.exists(q, "reviews");
    }

    // find review by objectID
    public Document findReviewByID(String id){

        return template.findById(id, Document.class, "reviews");
    }

    // update/upsert updated document into original review document
    public Boolean updateReview(Document doc, String id){
        Criteria c = Criteria.where("_id").is(id);
        Query q = Query.query(c);

        UpdateResult result = template.updateFirst(q, Update.fromDocument(doc), "reviews");
        return result.wasAcknowledged();
    }

    // create a method to check if document has been edited
    public Boolean checkIfEdited(String id){
        Criteria c = Criteria.where("_id").is(id);
        Query q = Query.query(c);

         Document doc = template.findOne(q, Document.class, "reviews");

         if (doc.getList("edited", Document.class) == null) {
            return false;
         }

         return true;
    }


}
