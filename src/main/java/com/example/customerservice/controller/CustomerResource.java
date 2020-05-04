package com.example.customerservice.controller;

import com.example.customerservice.model.dto.CustomerTagDto;
import com.example.customerservice.model.dto.CustomerTagWrapperDto;
import com.example.customerservice.model.dto.Time;
import com.example.customerservice.model.entity.Customer;
import com.example.customerservice.model.entity.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface CustomerResource {

    @GetMapping
    ResponseEntity<Page<Customer>> findAllByType(@RequestParam(required = false) CustomerType customerType,
                                                 @RequestParam(required = false, defaultValue = "lastName") String sortBy,
                                                 @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "10") int size);

    @GetMapping("/{customerType}/average")
    ResponseEntity<Time> findAverageTime(@PathVariable CustomerType customerType);

    @GetMapping("/group/tag")
    ResponseEntity<CustomerTagWrapperDto> groupCustomersByTag();

    @GetMapping("/group/tag/{tag}")
    ResponseEntity<CustomerTagDto> getCustomersByTag(@PathVariable String tag);
}
