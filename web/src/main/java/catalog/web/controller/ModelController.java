package catalog.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelController {

    @RequestMapping("/test")
    public String getHello(){
        return "hello test";
    }
}
