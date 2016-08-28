package ru.bormoshka.dao;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.bormoshka.dao.model.codec.SmsCodec;
import ru.bormoshka.dao.model.codec.SmsCodecProvider;
import ru.bormoshka.system.SystemConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@EnableConfigurationProperties
class MongoManager implements AutoCloseable {
    private final SystemConfig config;
    private MongoClient mongo;
    private MongoDatabase db = null;

    @Autowired
    public MongoManager(SystemConfig config) {
        this.config = config;
    }

    void initConnection() {
        if (db != null)
            return;
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(new SmsCodecProvider()),
                MongoClient.getDefaultCodecRegistry());
        MongoClientOptions options = MongoClientOptions.builder().codecRegistry(codecRegistry).build();

        if ((config.getDbPassword() != null && !config.getDbPassword().isEmpty()) && (config.getDbUser() != null && !config.getDbUser().isEmpty())) {
            MongoCredential credential = MongoCredential.createCredential(config.getDbUser(), config.getDbName(), config.getDbPassword().toCharArray());
            mongo = new MongoClient(new ServerAddress(config.getDbHost(), config.getDbPort()), Collections.singletonList(credential), options);
        } else {
            mongo = new MongoClient(new ServerAddress(config.getDbHost(), config.getDbPort()), options);
        }
        db = mongo.getDatabase(config.getDbName());
    }

    MongoDatabase getDb() {
        return db;
    }

    void initCollections() {
        final List<String> collectionsNames = new ArrayList<>();
        db.listCollectionNames().forEach((Block<String>) collectionsNames::add);
        CreateCollectionOptions opts = new CreateCollectionOptions();
        opts.autoIndex(false);

        if (!collectionsNames.contains(SmsCodec.COLLECTION_NAME)) {
            db.createCollection(SmsCodec.COLLECTION_NAME, opts);
            MongoCollection collection = db.getCollection(SmsCodec.COLLECTION_NAME);
            collection.createIndex(new Document(SmsCodec.MSID, -1));
            collection.createIndex(new Document(SmsCodec.STATUS, 1));
        }
    }

    @Override
    public void close() throws Exception {
        if (mongo != null) {
            mongo.close();
        }
    }
}
