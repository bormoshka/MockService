package ru.bormoshka.mock.dao;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.bormoshka.mock.system.SystemConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@EnableConfigurationProperties
public class MongoManager implements AutoCloseable {
    private final SystemConfig config;
    private MongoClient mongo;
    private MongoDatabase db = null;
    private List<CodecProvider> codecProviders;

    @Autowired
    public MongoManager(SystemConfig config, List<CodecProvider> codecProviders) {
        this.config = config;
        this.codecProviders = codecProviders;
    }

    void initConnection() {
        if (db != null)
            return;
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(codecProviders),
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

    public List<String> getExistingCollections() {
        final List<String> collectionsNames = new ArrayList<>();
        db.listCollectionNames().forEach((Block<String>) collectionsNames::add);
        return collectionsNames;
    }

    @Override
    public void close() throws Exception {
        if (mongo != null) {
            mongo.close();
        }
    }
}
