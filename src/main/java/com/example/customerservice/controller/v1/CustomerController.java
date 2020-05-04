package com.example.customerservice.controller.v1;

import com.example.customerservice.controller.CustomerResourceCommons;
import com.example.customerservice.service.CustomerService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/customer")
@Api(tags = {"Customer"})
public class CustomerController extends CustomerResourceCommons {

    public CustomerController(final CustomerService customerService) {
        super(customerService);
    }

    @PostMapping("/initialize")
    public void initialize() {
        customerService.initialize();
    }

}
