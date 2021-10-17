package com.trecapps.falsehoods.submit.controllers;

import com.trecapps.base.FalsehoodModel.models.FullFalsehood;
import com.trecapps.falsehoods.submit.services.MediaFalsehoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/Update/Media")
public class MediaFalsehoodController extends FalsehoodControllerBase{

    @Autowired
    MediaFalsehoodService mediaFalsehoodService;

    @PostMapping("/Submit")
    public ResponseEntity<String> submitMediaFalsehood(RequestEntity<FullFalsehood> falsehood)
    {
        return super.getResult(mediaFalsehoodService.submitFalsehood(falsehood.getBody()));
    }

    @PutMapping("/Metadata")
    public ResponseEntity<String> updateMetadata(RequestEntity<FullFalsehood> falsehood)
    {
        FullFalsehood obj = falsehood.getBody();
        return super.getResult(mediaFalsehoodService.editFalsehoodMetadata(obj.getMetadata(), obj.getContents()));
    }

    @PutMapping(value = "/Content", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateContents(RequestEntity<MultiValueMap<String, String>> request)
    {
        MultiValueMap<String, String> values = request.getBody();
        BigInteger id = null;
        try
        {
            id = new BigInteger(values.getFirst("Falsehood"));
        } catch (Exception e)
        {
            return new ResponseEntity<String>("Could not derive an id from the Falsehood field!", HttpStatus.BAD_REQUEST);
        }

        return super.getResult(mediaFalsehoodService.editFalsehoodContents(id,
                values.getFirst("Contents"), values.getFirst("Reason")));
    }
}
