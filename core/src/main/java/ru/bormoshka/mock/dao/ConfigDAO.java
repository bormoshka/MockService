package ru.bormoshka.mock.dao;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bormoshka.mock.system.AppConfig;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Component
public class ConfigDAO extends AbstractDAO<Document> {

    @Autowired
    public ConfigDAO(MongoManager manager) {
        super(manager);
    }

    public void save(Document doc, String key, Object value) {
        collection.updateOne(eq("_id", doc.getObjectId("_id")), set(key, value));
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    protected void initCollection() {
        collection = db.getCollection("application_config").withDocumentClass(Document.class);
        List<Document> docs = find();
        if (docs.size() < 1) {
            Document doc = new Document();
            doc.put(AppConfig.Names.AUTO_CHANGE_PROPERTY, AppConfig.Names.AUTO_CHANGE_VAL_OFF);
            AppConfig.load(doc, this);
            collection.insertOne(doc);
        } else {
            AppConfig.load(docs.get(0), this);
        }
    }

}
