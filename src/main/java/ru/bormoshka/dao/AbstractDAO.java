package ru.bormoshka.dao;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractDAO<T> {
    protected MongoCollection<T> collection;
    protected MongoDatabase db;

    protected final MongoManager manager;

    protected boolean isInitialized = false;


    public AbstractDAO(MongoManager manager) {
        this.manager = manager;
    }

    public void init() {
        if (isInitialized)
            return;
        manager.initConnection();
        manager.initCollections();
        db = manager.getDb();
        initCollection();
        isInitialized = true;
    }
    protected abstract void initCollection();

    public void insert(T doc) {
        collection.insertOne(doc);
    }

    public void insert(List<T> docList) {
        collection.insertMany(docList);
    }

    protected List<T> find(Bson search) {
        FindIterable<T> iterable = collection.find(search).limit(50);
        final List<T> result = new ArrayList<>();
        iterable.forEach((Block<T>) result::add);
        return result;
    }
    protected List<T> find() {
        FindIterable<T> iterable = collection.find();
        final List<T> result = new ArrayList<>();
        iterable.forEach((Block<T>) result::add);
        return result;
    }
}
