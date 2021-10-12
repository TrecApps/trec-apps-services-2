package com.trecapps.falsehoods.falsehoodSearch.controllers;

import com.trecapps.base.FalsehoodModel.models.Falsehood;
import com.trecapps.base.FalsehoodModel.models.FullFalsehood;
import com.trecapps.falsehoods.falsehoodSearch.services.FalsehoodService;
import com.trecapps.falsehoods.falsehoodSearch.services.SearchFalsehood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/Search/Media")
public class MediaFalsehoodController {

    @Autowired
    FalsehoodService falsehoodService;

    @PostMapping("/Confirmed")
    public List<Falsehood> searchFalsehoodByParams(@RequestBody SearchFalsehood searchObj)
    {
        return falsehoodService.getConfirmedFalsehoodsBySearchFeatures(searchObj);
    }

    @PostMapping("/Rejected")
    public List<Falsehood> searchRFalsehoodByParams(@RequestBody SearchFalsehood searchObj)
    {
        return falsehoodService.getRejectedFalsehoodsBySearchFeatures(searchObj);
    }

    @GetMapping("/Media/searchSubmitted")
    public List<Falsehood> searchSubmittedFalsehoods(@RequestParam(value="size", defaultValue="20", required=false)int size,
                                                     @RequestParam(value="page", defaultValue="0", required=false)int page)
    {
        return falsehoodService.getSubmittedFalsehoods(size, page);
    }

    @GetMapping("/Media/id/{id}")
    public Mono<FullFalsehood> GetFalsehood(@PathVariable("id") BigInteger id)
    {
        return falsehoodService.getFalsehoodById(id);
    }
}
