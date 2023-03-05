package com.mibemolsoft.realworldcrud.service;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import com.mibemolsoft.realworldcrud.domain.File;
import com.mibemolsoft.realworldcrud.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mibemolsoft.realworldcrud.repository.FileRepository;

@Service
public class FileStorageService {

  @Autowired
  private FileRepository fileRepository;

  @Autowired
  private CustomerRepository customerRepository;

  public File store(MultipartFile file, long customerId) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    // findById is an anti-pattern https://vladmihalcea.com/spring-data-jpa-findbyid/
    // TODO: add isPresent() checks
    File fileToSave = new File(fileName, file.getBytes(), customerRepository.findById(customerId).get());

    return fileRepository.save(fileToSave);
  }

  // TODO: if file does not exist return empty json response
  public File getFile(long id) {
    return fileRepository.findById(id).get();
  }

  public Stream<File> getAllFiles() {
    return fileRepository.findAll().stream();
  }
}