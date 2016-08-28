package ru.bormoshka.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.bormoshka.dao.ConfigDAO;
import ru.bormoshka.dao.SmsDAO;

@Component
public class Initialization {
  /*  private final SmsDAO smsDAO;
    private final ConfigDAO configDAO;

    @Autowired
    public Initialization(ConfigDAO configDAO, SmsDAO smsDAO) {
        this.configDAO = configDAO;
        this.smsDAO = smsDAO;
    }

    @EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
        smsDAO.init();
        configDAO.init();
    }*/
}
