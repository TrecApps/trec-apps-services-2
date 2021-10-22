package com.trecapps.falsehoods.submit.services;

import com.trecapps.base.FalsehoodModel.models.*;
import com.trecapps.base.FalsehoodModel.repos.FalsehoodRecordsRepo;
import com.trecapps.base.FalsehoodModel.repos.FalsehoodRepo;
import com.trecapps.base.InfoResource.models.Record;
import com.trecapps.falsehoods.submit.config.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class MediaFalsehoodService {

    @Autowired
    FalsehoodRepo pfRepo;

    @Autowired
    FalsehoodRecordsRepo cRepos;

    @Autowired
    StorageClient storageClient;

    public Mono<String> submitFalsehood(FullFalsehood full, String subject)
    {
        String contents = full.getContents();
        Falsehood falsehood = full.getMetadata();
        if(contents == null || falsehood == null)
            return Mono.just("400: Missing details!");

        falsehood.setId(null);
        falsehood.setUserId(subject);
        falsehood = pfRepo.save(falsehood);
        BigInteger fId = falsehood.getId();
        // To-Do: Set up Sotrage Client and send Contents of file to it

        return storageClient.SubmitFalsehood("MediaFalsehood-" + fId, contents, subject)
                .map((String str) -> {

                    List<Record> records = new ArrayList<>();
                    records.add(new Record("Event", "Creation", new Date(Calendar.getInstance().getTime().getTime()), 0l, null));

                    cRepos.save(new FalsehoodRecords(fId,
                            (byte)fId.divideAndRemainder(BigInteger.valueOf(20))[1].intValue(), records));

                    return "";
                });
    }

    public String editFalsehoodMetadata(Falsehood falsehood, String comment)
    {
        BigInteger fId = falsehood.getId();
        if(!cRepos.existsById(fId) || !pfRepo.existsById(fId))
            return "404: Falsehood not documented!";

        Falsehood currentFalsehood = pfRepo.getById(fId);
        if(!currentFalsehood.getUserId().equals(falsehood.getUserId()))
            return "400: Cannot attempt to change the id of the submitter on the Falsehood!";

        falsehood = pfRepo.save(falsehood);

        FalsehoodRecords records = cRepos.findById(fId).get();

        records.getRecords().add(new Record("Event", "Update", new Date(Calendar.getInstance().getTime().getTime()), 0l, comment));
        cRepos.save(records);
        return "";
    }

    public Mono<String> editFalsehoodContents(BigInteger id, String contents, String comment, OidcUser principal)
    {
        if(!cRepos.existsById(id) || !pfRepo.existsById(id))
            return Mono.just("404: Falsehood not documented!");

        Falsehood metadata = pfRepo.getById(id);
        if(!principal.getSubject().equals(metadata.getUserId()))
            return Mono.just("401: Only the Owner of the Falsehood can change the contents");

        return storageClient.SubmitFalsehood("MediaFalsehood-" + metadata.getId(), contents, principal.getSubject())
                .map((String str) -> {
                    FalsehoodRecords records = cRepos.findById(id).get();

                    records.getRecords().add(new Record("Event", "Edit", new Date(Calendar.getInstance().getTime().getTime()), 0l, comment));
                    cRepos.save(records);
                    return "";
                });

    }
}
