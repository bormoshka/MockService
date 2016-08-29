package ru.bormoshka.mock;

import com.google.gson.Gson;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.bormoshka.dao.SmsDAO;
import ru.bormoshka.dao.model.SmsModel;
import ru.bormoshka.dao.model.SmsStatus;
import ru.bormoshka.system.AppConfig;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "/sms/")
public class SMSController {
    final static Logger logger = Logger.getLogger(SMSController.class);
    private static volatile long nextID = -1;
    private final SmsDAO dao;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
    private Random rand = new Random();
    private static Gson gson = new Gson();

    @Autowired
    public SMSController(SmsDAO dao) {
        this.dao = dao;
    }

    @RequestMapping(method = RequestMethod.POST, value = "SendMessage", produces = "text/xml")
    @ResponseBody
    public String sendMessage(@RequestParam(value = "msid", required = true) String phone,
                              @RequestParam(value = "message", required = true) String message,
                              HttpServletRequest request) throws UnsupportedEncodingException {
        if (nextID < 0) {
            nextID = System.nanoTime();
        }
        SmsModel sms = new SmsModel(nextID++ + "", phone, message, SmsStatus.PENDING.getCode(), sdf.format(new Date()), request.getRemoteAddr(), 0);
        autoChangeStatus(sms);
        dao.insert(sms);
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<long xmlns=\"http://mcommunicator.ru/M2M\">" + sms.getMsid() + "</long>";
    }

    @RequestMapping(method = RequestMethod.GET, value = "SendMessage")
    @ResponseBody
    public String sendMessageGet(@RequestParam(value = "msid", required = true) String phone,
                                 @RequestParam(value = "message", required = true) String message,
                                 HttpServletRequest request) throws UnsupportedEncodingException {
        return sendMessage(phone, message, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "ping")
    public ModelAndView ping() throws UnsupportedEncodingException {
        logger.debug("Ping message received! ");
        ModelAndView model = new ModelAndView("index");
        List<SmsModel> lst = dao.getUnfinished();
        model.addObject("smsList", lst);
        List<SmsModel> lst2 = dao.getFinished();
        model.addObject("smsListOther", lst2);
        model.addObject("config", AppConfig.get().getJSON());
        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String index() throws UnsupportedEncodingException {
        logger.debug("index! ");
        return "redirect:/sms/ping";
    }

    @RequestMapping(method = RequestMethod.POST, value = "delete/{msid}", produces = "text/json")
    @ResponseBody
    public String deletePost(@PathVariable(value = "msid") String msid) throws UnsupportedEncodingException {
        logger.debug("delete! ");
        dao.delete(msid);
        return "{\"ok\": \"true\"}";
    }

    @RequestMapping(method = RequestMethod.GET, value = "delete/{msid}")
    public String delete(@PathVariable(value = "msid") String msid) throws UnsupportedEncodingException {
        logger.debug("delete! ");
        dao.delete(msid);
        return "redirect:/sms/ping";
    }

    @RequestMapping(method = RequestMethod.POST, value = "change/{msid}/{success}", produces = "text/json")
    @ResponseBody
    public String changePost(@PathVariable(value = "msid") String msid,
                             @PathVariable(value = "success") Boolean success) throws UnsupportedEncodingException {
        logger.debug("change! ");
        List<SmsModel> models = dao.find(msid);
        if (success == null) {
            success = true;
        }
        for (SmsModel model : models) {
            model.setStatus(success ? SmsStatus.DELIVERED.getCode() : SmsStatus.NOTDELIVERED.getCode());
            dao.updateStatus(model);
        }
        return "{\"ok\": \"true\", \"status\": \"" + success +"\", \"model\": " + gson.toJson(models.get(0)) + "}";
    }

    @RequestMapping(method = RequestMethod.GET, value = "get/{msid}", produces = "text/json")
    @ResponseBody
    public String getModel(@PathVariable(value = "msid") String msid) throws UnsupportedEncodingException {
        logger.debug("change! ");
        List<SmsModel> models = dao.find(msid);
        return gson.toJson(models.get(0));
    }

    @RequestMapping(method = RequestMethod.GET, value = "change/{msid}/{success}")
    public String change(@PathVariable(value = "msid") String msid,
                         @PathVariable(value = "success") Boolean success) throws UnsupportedEncodingException {
        logger.debug("change! ");
        List<SmsModel> models = dao.find(msid);
        if (success == null) {
            success = true;
        }
        for (SmsModel model : models) {
            model.setStatus(success ? SmsStatus.DELIVERED.getCode() : SmsStatus.NOTDELIVERED.getCode());
            dao.updateStatus(model);
        }
        return "redirect:/sms/ping";
    }

    @RequestMapping(method = RequestMethod.GET, value = "delete-all")
    public String deleteAll() throws UnsupportedEncodingException {
        logger.debug("deleteAll! ");
        dao.deleteAll();
        return "redirect:/sms/ping";
    }

    @RequestMapping(method = RequestMethod.GET, value = "change-all/{good}")
    public String changeAll(@PathVariable(value = "good") Boolean good) throws UnsupportedEncodingException {
        logger.debug("changeAll! ");
        List<SmsModel> models = dao.getUnfinished();
        for (SmsModel model : models) {
            model.setStatus(good ? SmsStatus.DELIVERED.getCode() : SmsStatus.NOTDELIVERED.getCode());
            dao.updateStatus(model);
        }
        return "redirect:/sms/ping";
    }

    @RequestMapping(method = RequestMethod.GET, value = "GetMessageStatus", produces = "text/xml")
    @ResponseBody
    public String getMessageStatusGET(@RequestBody String requestBody,
                                   @RequestParam String messageid,
                                   @RequestParam String login,
                                   @RequestParam String password) throws UnsupportedEncodingException {
        return getMessageStatus(requestBody, messageid, login, password);
    }

    @RequestMapping(method = RequestMethod.POST, value = "GetMessageStatus", produces = "text/xml")
    @ResponseBody
    public String getMessageStatus(@RequestBody String requestBody,
                                   @RequestParam String messageid,
                                   @RequestParam String login,
                                   @RequestParam String password) throws UnsupportedEncodingException {
        String message = URLDecoder.decode(requestBody, "UTF-8");
        logger.debug("Recived message (getMessageStatus): " + message);
        SmsModel model = dao.find(messageid).get(0);
        model.setTouchCount(model.getTouchCount() + 1);
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<ArrayOfDeliveryInfo xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://mcommunicator.ru/M2M\">\n" +
                "  <DeliveryInfo>\n" +
                "    <Msid>" + messageid + "</Msid>\n" +
                "    <DeliveryStatus>" + model.getStatus() + "</DeliveryStatus>\n" +
                "    <DeliveryDate>2015-02-21T15:02:00</DeliveryDate>\n" +
                "    <UserDeliveryDate>2015-02-21T15:02:32</UserDeliveryDate>\n" +
                "    <PartCount>1</PartCount>\n" +
                "  </DeliveryInfo>\n" +
                "</ArrayOfDeliveryInfo>";
    }

    private void autoChangeStatus(SmsModel sms) {
        String autoChange = AppConfig.get().getString(AppConfig.Names.AUTO_CHANGE_PROPERTY);

        switch (autoChange) {
            case AppConfig.Names.AUTO_CHANGE_VAL_SUCCESS: {
                sms.setStatus(SmsStatus.DELIVERED.getCode());
                break;
            }
            case AppConfig.Names.AUTO_CHANGE_VAL_FAIL: {
                sms.setStatus(SmsStatus.NOTDELIVERED.getCode());
                break;
            }
            case AppConfig.Names.AUTO_CHANGE_VAL_RANDOM: {
                if (rand.nextBoolean()) { //Скорее всего доставлено
                    sms.setStatus(rand.nextBoolean() ? SmsStatus.DELIVERED.getCode() : SmsStatus.SENT.getCode());
                } else if (rand.nextBoolean()) { // мб ещё доставляется
                    sms.setStatus(rand.nextBoolean() ? SmsStatus.PENDING.getCode() : SmsStatus.SENDING.getCode());
                } else { // это провал!
                    sms.setStatus(rand.nextBoolean() ? SmsStatus.NOTDELIVERED.getCode() : SmsStatus.NOTSENT.getCode());
                }
                break;
            }
            case AppConfig.Names.AUTO_CHANGE_VAL_OFF:
            default:
        }
    }
}