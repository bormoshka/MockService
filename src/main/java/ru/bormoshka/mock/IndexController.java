package ru.bormoshka.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
@RequestMapping(value = "/")
public class IndexController {
    private final RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public IndexController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("handlerMethods", this.handlerMapping.getHandlerMethods());
        return "index";
    }

}
