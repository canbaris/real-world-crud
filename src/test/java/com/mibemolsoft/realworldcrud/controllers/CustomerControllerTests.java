package com.mibemolsoft.realworldcrud.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mibemolsoft.realworldcrud.domain.Customer;
import com.mibemolsoft.realworldcrud.repository.CustomerRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@Import(CustomerController.class)
@ContextConfiguration(classes = {CustomerRepository.class})
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void givenCustomerObject_whenCreateCustomer_thenReturnSavedCustomer() throws Exception{

        // given - precondition or setup
        Customer customer = new Customer("canan",1);
        given(customerRepository.save(any(Customer.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer))
                .with(csrf()));

        // then - verify the result or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",
                        is(customer.getName())))
                .andExpect(jsonPath("$.id",
                        is((customer.getId())), Long.class));

    }

    @Test
    @WithMockUser
    public void givenListOfCustomers_whenGetAllCustomers_thenReturnCustomerList() throws Exception{
        // given - precondition or setup
        List<Customer> listOfCustomers = new ArrayList<>();
        listOfCustomers.add(new Customer("canile", 2));
        listOfCustomers.add(new Customer("canan", 3));
        given(customerRepository.findAll()).willReturn(listOfCustomers);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/customers"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfCustomers.size())));

    }

    // positive scenario - valid customer id
    // JUnit test for GET customer by id REST API
    @Test
    @WithMockUser
    public void givenCustomerId_whenGetCustomerById_thenReturnCustomerObject() throws Exception{
        // given - precondition or setup
        long customerId = 1L;
        Customer customer = new Customer("cemile", 4L);
        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/customers/{id}", customerId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(customer.getName())))
                .andExpect(jsonPath("$.id", is(customer.getId()), Long.class));

    }

    // negative scenario - valid customer id
    // JUnit test for GET customer by id REST API
    @Test
    @WithMockUser
    public void givenInvalidCustomerId_whenGetCustomerById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        long customerId = 1L;
        given(customerRepository.findById(customerId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/customers/{id}", customerId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // JUnit test for update employee REST API - positive scenario
    @Test
    @WithMockUser
    public void givenUpdatedCustomer_whenUpdateCustomer_thenReturnUpdatedCustomerObject() throws Exception{
        // given - precondition or setup
        long customerId = 1L;
        Customer savedCustomer = new Customer("aliye", 5L);
        Customer updatedCustomer = new Customer("veli", 5L);
        given(customerRepository.findById(customerId)).willReturn(Optional.of(savedCustomer));
        given(customerRepository.save(updatedCustomer))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomer))
                .with(csrf()));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

    // JUnit test for update customer REST API - negative scenario
    @Test
    @WithMockUser
    public void givenUpdatedCustomer_whenUpdateCustomer_thenReturn404() throws Exception{
        // given - precondition or setup
        long customerId = 1L;
        Customer updatedEmployee = new Customer("veli", 7L);
        given(customerRepository.findById(customerId)).willReturn(Optional.empty());
        given(customerRepository.save(any(Customer.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)).with(csrf()));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for delete customer REST API
    @Test
    @WithMockUser
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception{
        // given - precondition or setup
        long customerId = 1;
        willDoNothing().given(customerRepository).deleteById(customerId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/customers/{id}", customerId).with(csrf()));

        // then - verify the output
        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}