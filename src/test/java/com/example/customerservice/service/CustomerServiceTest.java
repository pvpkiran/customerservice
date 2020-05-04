package com.example.customerservice.service;

import com.example.customerservice.model.dto.CustomerTagDto;
import com.example.customerservice.model.dto.CustomerTagWrapperDto;
import com.example.customerservice.model.dto.Time;
import com.example.customerservice.model.entity.Customer;
import com.example.customerservice.model.entity.CustomerType;
import com.example.customerservice.repository.CustomerRepository;
import com.example.customerservice.repository.TagRepository;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TagRepository tagRepository;

    private CustomerService customerService;

    @BeforeAll
    void setUp() {
        customerService = new CustomerService(customerRepository, tagRepository);
    }

    @BeforeEach
    public void fillData() {
        customerService.initialize();
    }

    @Test
    @Order(1)
    void testIfInitializeFillsData() {
        List<Customer> customers = Lists.newArrayList();
        customerRepository.findAll().forEach(customers::add);
        assertEquals(20, customers.size());
    }

    @Test
    @Order(2)
    void testIfGroupByCustomersByTagReturnsAllGroups(){
        final CustomerTagWrapperDto customerTagWrapperDto = customerService.groupCustomersByTag();
        assertNotNull(customerTagWrapperDto);
        assertEquals(5, customerTagWrapperDto.getCustomerTagDtoList().size());
    }

    @Test
    @Order(3)
    void testIfGetCustomersByTagReturnsAllCustomersForTag(){
        final CustomerTagDto customerTagDto = customerService.getCustomersByTag("red");
        assertNotNull(customerTagDto);
        assertEquals("red", customerTagDto.getTag());
        // cannot check for customers in customerTagDto object.
        // since we are populating database with random data, there is a chance that
        // there are no customers with this tag.
    }

    @Test
    @Order(3)
    void testIfAverageLeadTimeIsGreaterThanZero(){
        final Time averageLeadTime = customerService.calculateAverageLeadTime(CustomerType.PROSPECT);
        assertNotNull(averageLeadTime);
        assertTrue(averageLeadTime.getDays() > 0);
        assertEquals(DAYS, averageLeadTime.getUnit());
    }
    //TODO : Add negative test cases
}