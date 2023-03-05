package com.mibemolsoft.realworldcrud.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mibemolsoft.realworldcrud.domain.Customer;
import com.mibemolsoft.realworldcrud.domain.File;
import com.mibemolsoft.realworldcrud.repository.FileRepository;
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
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@Import(FileController.class)
@ContextConfiguration(classes = {FileRepository.class})
class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileRepository fileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // TODO : fix the tests
    @Test
    @WithMockUser
    public void givenCustomerObject_whenCreateCustomer_thenReturnSavedCustomer() throws Exception {

        // given - precondition or setup
        Customer customer = new Customer("canan",1L);
        byte[] fileData = new byte[20];
        new Random().nextBytes(fileData);
        File file = new File("canan.cv", fileData, customer);
        given(fileRepository.save(any(File.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/files")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer))
                .with(csrf()));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",
                        is(file.getName())))
                .andExpect(jsonPath("$.id",
                        is(file.getId())))
                .andExpect(jsonPath("$.fileContents",
                        is(file.getFileContents())));
    }

    @Test
    @WithMockUser
    public void givenListOfCustomers_whenGetAllCustomers_thenReturnCustomerList() throws Exception {
        // given - precondition or setup
        List<File> listOfFiles = new ArrayList<>();
        Customer customer1 = new Customer("canan",2L);
        Customer customer2 = new Customer("fatma",3L);
        byte[] fileData = new byte[20];
        new Random().nextBytes(fileData);
        listOfFiles.add(new File("canan.cv", fileData, customer1));
        listOfFiles.add(new File("fatma.cv", fileData, customer2));
        given(fileRepository.findAll()).willReturn(listOfFiles);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/files"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfFiles.size())));

    }

    // positive scenario - valid customer id
    // JUnit test for GET customer by id REST API
    @Test
    @WithMockUser
    public void givenCustomerId_whenGetCustomerById_thenReturnCustomerObject() throws Exception {
        // given - precondition or setup
        Long fileId = 1L;
        Customer customer = new Customer("canan", 4L);
        byte[] fileData = new byte[20];
        new Random().nextBytes(fileData);
        File file = new File("canan.cv", fileData, customer);
        given(fileRepository.findById(fileId)).willReturn(Optional.of(file));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/files/{id}", fileId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(customer.getName())))
                .andExpect(jsonPath("$.id", is(customer.getId())))
                .andExpect(jsonPath("$.fileContents",
                        is(file.getFileContents())));

    }

    // negative scenario - valid customer id
    // JUnit test for GET customer by id REST API
    @Test
    @WithMockUser
    public void givenInvalidCustomerId_whenGetCustomerById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long fileId = 1;
        given(fileRepository.findById(fileId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/files/{id}", fileId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // JUnit test for update employee REST API - positive scenario
    @Test
    @WithMockUser
    public void givenUpdatedCustomer_whenUpdateCustomer_thenReturnUpdatedCustomerObject() throws Exception {
        // given - precondition or setup
        Long fileId = 1L;
        Customer customer1 = new Customer("canan", 5);
        Customer customer2 = new Customer("aliye", 6);
        byte[] fileData = new byte[20];
        new Random().nextBytes(fileData);
        File savedFile = new File("canan.cv", fileData, customer1);
        new Random().nextBytes(fileData);
        File updatedFile = new File("aliye.cv", fileData, customer2);
        given(fileRepository.findById(fileId)).willReturn(Optional.of(savedFile));
        given(fileRepository.save(updatedFile))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/files/{id}", fileId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFile))
                .with(csrf()));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updatedFile.getName())))
                .andExpect(jsonPath("$.id", is(updatedFile.getId())))
                .andExpect(jsonPath("$.fileContents",
                        is(updatedFile.getFileContents())));

    }

    // JUnit test for update customer REST API - negative scenario
    @Test
    @WithMockUser
    public void givenUpdatedCustomer_whenUpdateCustomer_thenReturn404() throws Exception {
        // given - precondition or setup
        long fileId = 1L;
        Customer savedCustomer = new Customer("aliye", 7);
        Customer updatedEmployee = new Customer("veli", 8);
        given(fileRepository.findById(fileId)).willReturn(Optional.empty());
        given(fileRepository.save(any(File.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/files/{id}", fileId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
                .with(csrf()));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for delete customer REST API
    @Test
    @WithMockUser
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given - precondition or setup
        long fileId = 1L;
        willDoNothing().given(fileRepository).deleteById(fileId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/files/{id}", fileId)
                .with(csrf()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

}