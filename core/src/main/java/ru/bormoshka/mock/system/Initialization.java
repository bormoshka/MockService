package ru.bormoshka.mock.system;

import org.springframework.stereotype.Component;

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
