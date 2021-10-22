package com.trecapps.falsehoods.submit.controllers;

import com.trecapps.base.FalsehoodModel.models.Falsehood;
import com.trecapps.base.FalsehoodModel.models.FalsehoodUser;
import com.trecapps.base.FalsehoodModel.models.FullPublicFalsehood;
import com.trecapps.base.FalsehoodModel.models.PublicFalsehood;
import com.trecapps.falsehoods.submit.services.PublicFalsehoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RestController
@RequestMapping("/Update/Public")
public class PublicFalsehoodController extends FalsehoodControllerBase{

    @Autowired
    PublicFalsehoodService publicFalsehoodService;

    @PostMapping("/Submit")
    public Mono<ResponseEntity<String>> submitMediaFalsehood(RequestEntity<FullPublicFalsehood> falsehood,
                                                             @AuthenticationPrincipal OidcUser principal)
    {
        FalsehoodUser user = principal.getClaim("FalsehoodUser");
        if(user.getCredibility() < MIN_CREDIT_SUBMIT_NEW)
            return Mono.just(new ResponseEntity<String>
                    ("Your Credibility Is too low. Please wait until it is set to five before trying again!",
                            HttpStatus.FORBIDDEN));
        return publicFalsehoodService.submitFalsehood(falsehood.getBody(), principal.getSubject())
                .map((String message) -> super.getResult(message))
                .onErrorResume((Throwable ex) -> Mono.just(super.getResult(ex.getMessage())));
    }

    @PutMapping("/Metadata")
    public ResponseEntity<String> updateMetadata(RequestEntity<FullPublicFalsehood> falsehood,
                                                 @AuthenticationPrincipal OidcUser principal)
    {
        FullPublicFalsehood obj = falsehood.getBody();
        PublicFalsehood metaData = obj.getMetadata();
        FalsehoodUser user = principal.getClaim("FalsehoodUser");
        if(!principal.getSubject().equals(metaData.getUserId()) && user.getCredibility() < MIN_CREDIT_UPDATE_METADATA)
            return new ResponseEntity<String>
                    ("Your Credibility needs to be 400 points or above to change the metadata of another user's Falsehood!",
                            HttpStatus.FORBIDDEN);
        return super.getResult(publicFalsehoodService.editFalsehoodMetadata(obj.getMetadata(), obj.getContents()));
    }

    @PutMapping(value = "/Content", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> updateContents(RequestEntity<MultiValueMap<String, String>> request,
                                                 @AuthenticationPrincipal OidcUser principal)
    {
        MultiValueMap<String, String> values = request.getBody();
        BigInteger id = null;
        try
        {
            id = new BigInteger(values.getFirst("Falsehood"));
        } catch (Exception e)
        {
            return Mono.just(new ResponseEntity<String>("Could not derive an id from the Falsehood field!", HttpStatus.BAD_REQUEST));
        }

        return publicFalsehoodService.editFalsehoodContents(id,
                values.getFirst("Contents"), values.getFirst("Reason"),principal)
                .map((String message) ->super.getResult(message))
                .onErrorResume((Throwable ex) -> Mono.just(super.getResult(ex.getMessage())));
    }
}
