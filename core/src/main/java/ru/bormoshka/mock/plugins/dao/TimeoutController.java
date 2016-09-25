package ru.bormoshka.mock.plugins.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

@Controller
@RequestMapping(value = "/timeout/", produces={"text/xml; charset=UTF-8"})
public class TimeoutController {
	final static Logger logger = Logger.getLogger(TimeoutController.class);

	@RequestMapping(method = RequestMethod.POST, value = "post")
	@ResponseBody
	public String testPost(@RequestBody String requestBody) throws Exception {
		String message = URLDecoder.decode(requestBody, "UTF-8");
		Thread.sleep(600);
		logger.debug("Received message (sendMessage): " + message);
		return "ok";
	}

	@RequestMapping(method = RequestMethod.GET, value = "get", produces={"text/html; charset=UTF-8"})
	@ResponseBody
	public String testGet() throws Exception {
		logger.debug("Ping message received! ");
		Thread.sleep(600);
		String body = "<h3>Received data: </h3><ul>";

		return "<html><head><title>MockSMSService</title></head><body>" + body + "</body>";
	}

}