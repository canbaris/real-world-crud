package com.mibemolsoft.realworldcrud.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mibemolsoft.realworldcrud.domain.Customer;
import com.mibemolsoft.realworldcrud.domain.File;
import com.mibemolsoft.realworldcrud.repository.FileRepository;
import com.mibemolsoft.realworldcrud.service.FileStorageService;
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

// TODO: check file contents as well as name and id
// TODO: fix the tests, although they are as correct as possible, they are failing
@WebMvcTest
// tests cannot initialize the controller, 404 status code is returned, as a work around use import annotation
@Import(FileController.class)
@ContextConfiguration(classes = {FileRepository.class})
class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileRepository fileRepository;

    // Required for context initialization
    @MockBean
    private FileStorageService fileStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    // With mockuser the default user is: user and password is: password
    @WithMockUser
    public void givenFileObject_whenCreateFile_thenReturnSavedFile() throws Exception {

        // given
        Customer customer = new Customer("canan",1L);
        byte[] fileData = new byte[20];
        new Random().nextBytes(fileData);
        File file = new File("canan.cv", fileData, customer);
        given(fileRepository.save(any(File.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(post("/files")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(file))
                .with(csrf()));

        // then
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",
                        is(file.getName())))
                .andExpect(jsonPath("$.id",
                        is(file.getId()), Long.class));
    }

    @Test
    @WithMockUser
    public void givenListOfFiles_whenGetAllFiles_thenReturnFileList() throws Exception {
        // given
        List<File> listOfFiles = new ArrayList<>();
        Customer customer1 = new Customer("canan",2L);
        Customer customer2 = new Customer("fatma",3L);
        byte[] fileData = new byte[20];
        new Random().nextBytes(fileData);
        listOfFiles.add(new File("canan.cv", fileData, customer1));
        listOfFiles.add(new File("fatma.cv", fileData, customer2));
        given(fileRepository.findAll()).willReturn(listOfFiles);

        // when
        ResultActions response = mockMvc.perform(get("/files"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfFiles.size())));

    }

    // positive scenario - valid file id
    // test for GET file by id REST API
    @Test
    @WithMockUser
    public void givenFileId_whenGetFileById_thenReturnFileObject() throws Exception {
        // given
        long fileId = 1L;
        Customer customer = new Customer("canan", 4L);
        byte[] fileData = new byte[20];
        new Random().nextBytes(fileData);
        File file = new File("canan.cv", fileData, customer);
        given(fileRepository.findById(fileId)).willReturn(Optional.of(file));

        // when
        ResultActions response = mockMvc.perform(get("/files/{id}", fileId));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(customer.getName())))
                .andExpect(jsonPath("$.id", is(customer.getId())));

    }

    // negative scenario - valid customer id
    // test for GET customer by id REST API
    @Test
    @WithMockUser
    public void givenInvalidCustomerId_whenGetCustomerById_thenReturnEmpty() throws Exception {
        // given
        long fileId = 1;
        given(fileRepository.findById(fileId)).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/files/{id}", fileId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // test for update customer REST API - positive scenario
    @Test
    @WithMockUser
    public void givenUpdatedCustomer_whenUpdateCustomer_thenReturnUpdatedCustomerObject() throws Exception {
        // given
        long fileId = 1L;
        Customer customer1 = new Customer("canan", 5L);
        Customer customer2 = new Customer("aliye", 6L);
        byte[] fileData = new byte[20];
        new Random().nextBytes(fileData);
        File savedFile = new File("canan.cv", fileData, customer1);
        new Random().nextBytes(fileData);
        File updatedFile = new File("aliye.cv", fileData, customer2);
        given(fileRepository.findById(fileId)).willReturn(Optional.of(savedFile));
        given(fileRepository.save(updatedFile))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/files/{id}", fileId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFile))
                .with(csrf()));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updatedFile.getName())))
                .andExpect(jsonPath("$.id", is(updatedFile.getId())));

    }

    // test for update customer REST API - negative scenario
    @Test
    @WithMockUser
    public void givenUpdatedCustomer_whenUpdateCustomer_thenReturn404() throws Exception {
        // given
        long fileId = 1L;
        Customer savedCustomer = new Customer("aliye", 7L);
        Customer updatedEmployee = new Customer("veli", 8L);
        given(fileRepository.findById(fileId)).willReturn(Optional.empty());
        given(fileRepository.save(any(File.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/files/{id}", fileId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
                .with(csrf()));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // test for delete customer REST API
    @Test
    @WithMockUser
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given
        long fileId = 1L;
        willDoNothing().given(fileRepository).deleteById(fileId);

        // when
        ResultActions response = mockMvc.perform(delete("/files/{id}", fileId)
                .with(csrf()));

        // then
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

}