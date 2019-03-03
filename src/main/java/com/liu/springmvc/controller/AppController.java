package com.liu.springmvc.controller;

import com.liu.springmvc.Model.Pizza;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AppController {
    @RequestMapping(value = "/pizzavalley/{pizzaName}",method = RequestMethod.GET)
    public String getPizza(@PathVariable String pizzaName, ModelMap modelMap){
        Pizza pizza = new Pizza(pizzaName);
        modelMap.addAttribute("pizza",pizza);
        return "pizza";
    }
}
