package com.tsien.mall.controller.protal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/30 0030 22:23
 */

@Controller
@RequestMapping
public class IndexController {

    @GetMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping
    public String homePage() {
        return "HomePage";
    }
}
