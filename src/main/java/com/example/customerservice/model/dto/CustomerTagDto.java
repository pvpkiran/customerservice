package com.example.customerservice.model.dto;

import com.example.customerservice.model.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerTagDto {
    private String tag;
    private int totalCustomersCount;
    private Set<Customer> customers;
}
