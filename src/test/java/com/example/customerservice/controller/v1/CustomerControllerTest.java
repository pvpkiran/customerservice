package com.example.customerservice.controller.v1;

import com.example.customerservice.model.dto.Time;
import com.example.customerservice.model.entity.CustomerType;
import com.example.customerservice.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void findAllByType() throws Exception {
        mockMvc.perform(get("/v1/customer/LEAD/average"))
                .andExpect(status().isBadRequest());

        ArgumentCaptor<CustomerType> customerTypeArgumentCaptor = ArgumentCaptor.forClass(CustomerType.class);
        doAnswer(invocation ->  {
            assertEquals("PROSPECT", customerTypeArgumentCaptor.getValue().name());
            return Time.builder().unit(ChronoUnit.DAYS).days(5).build();
        }).when(customerService).calculateAverageLeadTime(customerTypeArgumentCaptor.capture());

        mockMvc.perform(get("/v1/customer/PROSPECT/average"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days").value(5));
    }

    // TODO:: Similar test cases for other methods
}