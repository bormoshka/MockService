package ru.bormoshka.system;

import org.bson.Document;
import ru.bormoshka.dao.ConfigDAO;

import java.io.Serializable;

public class AppConfig implements Serializable {
    private static volatile Document document;
    private static AppConfig self = null;
    public static final int TYPE_BOOL = 0;
    public static final int TYPE_STR = 1;
    public static ConfigDAO dao;

    public static void load(Document doc, ConfigDAO configDAO) {
        if (self == null) {
            self = new AppConfig(doc);
            dao = configDAO;
        } else {
            document = doc;
        }
    }

    public static AppConfig get() {
        return self;
    }

    private AppConfig(Document document) {
        AppConfig.document = document;
    }

    public String getString(String key) {
        return document.getString(key);
    }

    public Boolean getBoolean(String key) {
        return document.getBoolean(key);
    }

    public void set(String key, String value) {
        document.put(key, value);
        save(key, value);
    }

    public void set(String key, Boolean value) {
        document.put(key, value);
        save(key, value);
    }

    public String getJSON() {
        return document.toJson();
    }

    public static Document getDocument() {
        return document;
    }

    private void save(String key, Object value) {
        dao.save(document, key, value);
    }

    public class Names {
        public static final String AUTO_CHANGE_PROPERTY = "autoChangeStatus";
        public static final String AUTO_CHANGE_VAL_OFF = "off";
        public static final String AUTO_CHANGE_VAL_SUCCESS = "success";
        public static final String AUTO_CHANGE_VAL_FAIL = "fail";
        public static final String AUTO_CHANGE_VAL_RANDOM = "random";
    }
}
