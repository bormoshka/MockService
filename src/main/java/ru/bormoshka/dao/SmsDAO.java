package ru.bormoshka.dao;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bormoshka.dao.model.SmsModel;
import ru.bormoshka.dao.model.SmsStatus;
import ru.bormoshka.dao.model.codec.SmsCodec;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;

@Component
public class SmsDAO extends AbstractDAO<SmsModel> {
    private List<String> finishedStatuses = Arrays.asList(SmsStatus.NOTDELIVERED.getCode(),
            SmsStatus.NOTSENT.getCode(), SmsStatus.DELIVERED.getCode());

    public void replace(SmsModel sms) {
        collection.replaceOne(eq(SmsCodec.MSID, sms.getMsid()), sms);
    }

    public void updateStatus(SmsModel sms) {
        updateByID(sms.getMsid(), new Document(SmsCodec.STATUS, sms.getStatus()));
    }

    public void updateByID(String id, Bson toUpdate) {
        collection.updateOne(eq(SmsCodec.MSID, id), new Document("$set", toUpdate));
    }

    public void deleteAll() {
        collection.deleteMany(ne(SmsCodec.DATE, new Date()));
    }

    public void delete(String id) {
        collection.findOneAndDelete(eq(SmsCodec.MSID, id));
    }

    public List<SmsModel> getUnfinished() {
        FindIterable<SmsModel> iterable = collection
                .find(nin(SmsCodec.STATUS, finishedStatuses))
                .limit(50)
                .sort(descending("msid"));
        final List<SmsModel> result = new ArrayList<>();
        iterable.forEach((Block<SmsModel>) result::add);
        return result;
    }

    public List<SmsModel> getFinished() {
        FindIterable<SmsModel> iterable = collection.find(in(SmsCodec.STATUS, finishedStatuses))
                .limit(50)
                .sort(descending("msid"));
        final List<SmsModel> result = new ArrayList<>();
        iterable.forEach((Block<SmsModel>) result::add);
        return result;
    }

    public List<SmsModel> find(String msid) {
        FindIterable<SmsModel> iterable = collection.find(eq(SmsCodec.MSID, msid));
        iterable.limit(50);
        final List<SmsModel> result = new ArrayList<>();
        iterable.forEach((Block<SmsModel>) result::add);
        return result;
    }

    @Autowired
    public SmsDAO(MongoManager manager) {
        super(manager);
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    protected void initCollection() {
        collection = db.getCollection(SmsCodec.COLLECTION_NAME).withDocumentClass(SmsModel.class);
    }
}
