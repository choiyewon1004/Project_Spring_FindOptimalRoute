package com.example.project_spring_optimalroute;

import com.example.project_spring_optimalroute.Bus.Bus.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class backController {
    private final BusService testservice;

    @GetMapping("/testjpa")
    public String testjpa(Model model){
        model.addAttribute("testjpa", testservice.bus_testservice());
        return "testjpa";
    }

}

