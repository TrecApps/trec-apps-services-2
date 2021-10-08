package com.trecapps.internal.storage.controllers;

import com.microsoft.azure.storage.StorageException;
import com.trecapps.internal.storage.models.FileData;
import com.trecapps.internal.storage.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> postFile(RequestEntity<String> request)
    {
        String result = fileService.uploadFile(request);

        if(result.startsWith("CLIENT:"))
            return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
        if(result.startsWith("500"))
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        if(result.startsWith("502"))
            return new ResponseEntity<>(result, HttpStatus.BAD_GATEWAY);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteFile(RequestEntity request)
    {
        HttpHeaders headers = request.getHeaders();
        String id = headers.getFirst("FileId");
        Long userId = null;
        try
        {
            userId = Long.parseLong(headers.getFirst("UserId"));
        }catch(NumberFormatException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        switch(fileService.deleteFile(id, userId))
        {
            case 401:
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            case 403:
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            case 404:
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            case 500:
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            case 502:
                return new ResponseEntity(HttpStatus.BAD_GATEWAY);
            default:
                return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/downloadMetadata")
    public ResponseEntity<FileData> getFileData(RequestEntity request)
    {
        HttpHeaders headers = request.getHeaders();
        Long userId = null;
        try
        {
            String userIdStr = headers.getFirst("UserId");
            userId = Long.parseLong(userIdStr);
        }catch(NumberFormatException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            FileData content = fileService.getFileData(headers.getFirst("FileId"),headers.getFirst("App"), userId);
            return new ResponseEntity<>(content, HttpStatus.OK);
        } catch (IllegalAccessException e)
        {
            String ex = e.getMessage();
            HttpStatus stat = ex.startsWith("Unknown") ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(stat);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<String> getFile(RequestEntity request)
    {
        HttpHeaders headers = request.getHeaders();
        Long userId = null;
        try
        {
            String userIdStr = headers.getFirst("UserId");
            if(userIdStr != null)
                userId = Long.parseLong(userIdStr);
        }catch(NumberFormatException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            String content = fileService.getFile(headers.getFirst("FileId"),headers.getFirst("App"), userId);
            return new ResponseEntity<>(content, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (StorageException e) {
            return new ResponseEntity<>("Could not Access Azure Storage", HttpStatus.BAD_GATEWAY);
        } catch (IllegalAccessException e) {
            String ex = e.getMessage();
            HttpStatus stat = ex.startsWith("Unknown") ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(ex, stat);
        }
    }

}
