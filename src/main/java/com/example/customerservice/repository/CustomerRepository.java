package com.example.customerservice.repository;

import com.example.customerservice.model.entity.Customer;
import com.example.customerservice.model.entity.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.stream.Stream;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
  Page<Customer> findByType(CustomerType type, Pageable pageable);
  Stream<Customer> findByType(CustomerType type);
}
