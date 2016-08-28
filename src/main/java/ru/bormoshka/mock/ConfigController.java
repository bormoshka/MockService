package ru.bormoshka.mock;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.bormoshka.dao.ConfigDAO;
import ru.bormoshka.system.AppConfig;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping(value = "/config/")
public class ConfigController {
    final static Logger logger = Logger.getLogger(ConfigController.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    private final ConfigDAO dao;

    @Autowired
    public ConfigController(ConfigDAO dao) {
        this.dao = dao;
    }

    @RequestMapping(method = RequestMethod.GET, value = "change/{name}/{value}/{type}", produces = "text/json")
    @ResponseBody
    public String changeParam(@PathVariable(value = "name") String paramName,
                              @PathVariable(value = "value") String value,
                              @PathVariable(value = "type") int type) throws UnsupportedEncodingException {
        if (type == AppConfig.TYPE_BOOL) {
            AppConfig.get().set(paramName, Boolean.parseBoolean(value));
        } else if (type == AppConfig.TYPE_STR) {
            AppConfig.get().set(paramName, value);
        }
        return "{\"ok\": \"true\"}";
    }

}