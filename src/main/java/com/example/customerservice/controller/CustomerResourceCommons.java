package com.example.customerservice.controller;

import com.example.customerservice.model.dto.CustomerTagDto;
import com.example.customerservice.model.dto.CustomerTagWrapperDto;
import com.example.customerservice.model.dto.Time;
import com.example.customerservice.model.entity.Customer;
import com.example.customerservice.model.entity.CustomerType;
import com.example.customerservice.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

/**
 * Implementations common across versions
 */
public abstract class CustomerResourceCommons implements CustomerResource {

    protected final CustomerService customerService;

    public CustomerResourceCommons(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<Page<Customer>> findAllByType(CustomerType customerType,
                                                        String sortBy,
                                                        Sort.Direction direction,
                                                        int page,
                                                        int size)
    {
        final Page<Customer> byCustomerType = customerService.findByCustomerType(customerType, sortBy, direction, page, size);
        return ResponseEntity.ok(byCustomerType);
    }

    @Override
    public ResponseEntity<Time> findAverageTime(CustomerType customerType)
    {
        if(customerType.equals(CustomerType.LEAD))
            return ResponseEntity.badRequest().build();

        final Time averageLeadTime = customerService.calculateAverageLeadTime(customerType);
        return ResponseEntity.ok(averageLeadTime);
    }

    @Override
    public ResponseEntity<CustomerTagWrapperDto> groupCustomersByTag()
    {
        final CustomerTagWrapperDto customerTagWrapperDto = customerService.groupCustomersByTag();
        return ResponseEntity.ok(customerTagWrapperDto);
    }

    @Override
    public ResponseEntity<CustomerTagDto> groupCustomersByTag(String tag)
    {
        final CustomerTagDto customerTagDto = customerService.getCustomersByTag(tag);
        return ResponseEntity.ok(customerTagDto);
    }
}
