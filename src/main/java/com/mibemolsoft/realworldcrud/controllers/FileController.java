package com.mibemolsoft.realworldcrud.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mibemolsoft.realworldcrud.domain.Customer;
import com.mibemolsoft.realworldcrud.domain.File;
import com.mibemolsoft.realworldcrud.messages.ResponseFile;
import com.mibemolsoft.realworldcrud.messages.ResponseMessage;
import com.mibemolsoft.realworldcrud.repository.FileRepository;
import com.mibemolsoft.realworldcrud.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@CrossOrigin("http://localhost:8080")
public class FileController {

    @Autowired
    private FileStorageService storageService;

    @Autowired
    private FileRepository fileRepository;

    @PostMapping(value="/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("customerId") Long id) {
        String message = "";
        try {
            storageService.store(file, id);
            message = "File uploaded successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "There has been an error while uploading the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getAllFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(String.valueOf(dbFile.getId()))
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getFileContents().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        File dbFile = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getName() + "\"")
                .body(dbFile.getFileContents());
    }


    @PutMapping("/files/{id}")
    public ResponseEntity<File> updateFile(@PathVariable("id") long id, @RequestBody File file) {
        Optional<File> fileData = fileRepository.findById(id);

        if (fileData.isPresent()) {
            File fileToUpdate = fileData.get();
            fileToUpdate.setName(file.getName());
            return new ResponseEntity<>(fileRepository.save(fileToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable("id") long id) {
        try {
            fileRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/files")
    public ResponseEntity<HttpStatus> deleteAllFiles() {
        try {
            fileRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}