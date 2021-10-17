package com.trecapps.falsehoods.submit.services;

import com.trecapps.base.FalsehoodModel.models.FullPublicFalsehood;
import com.trecapps.base.FalsehoodModel.models.PublicFalsehood;
import com.trecapps.base.FalsehoodModel.models.PublicFalsehoodRecords;
import com.trecapps.base.FalsehoodModel.repos.PublicFalsehoodRecordsRepo;
import com.trecapps.base.FalsehoodModel.repos.PublicFalsehoodRepo;
import com.trecapps.base.InfoResource.models.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class PublicFalsehoodService {

    @Autowired
    PublicFalsehoodRepo pfRepo;

    @Autowired
    PublicFalsehoodRecordsRepo cRepos;

    public String submitFalsehood(FullPublicFalsehood full)
    {
        String contents = full.getContents();
        PublicFalsehood falsehood = full.getMetadata();
        if(contents == null || falsehood == null)
            return "400: Missing details!";

        falsehood.setId(null);

        falsehood = pfRepo.save(falsehood);
        // To-Do: Set up Sotrage Client and send Contents of file to it


        // End To-Do
        BigInteger fId = falsehood.getId();



        List<Record> records = new ArrayList<>();
        records.add(new Record("Event", "Creation", new Date(Calendar.getInstance().getTime().getTime()), 0l, null));

        PublicFalsehoodRecords newRecords = new PublicFalsehoodRecords(fId,
                (byte)fId.divideAndRemainder(BigInteger.valueOf(20))[1].intValue(), records);

        cRepos.save(newRecords);


        return "";
    }

    public String editFalsehoodMetadata(PublicFalsehood falsehood, String comment)
    {
        BigInteger fId = falsehood.getId();
        if(!cRepos.existsById(fId) || !pfRepo.existsById(fId))
            return "404: Falsehood not documented!";

        falsehood = pfRepo.save(falsehood);

        PublicFalsehoodRecords records = cRepos.findById(fId).get();

        records.getRecords().add(new Record("Event", "Update", new Date(Calendar.getInstance().getTime().getTime()), 0l, comment));
        cRepos.save(records);
        return "";
    }

    public String editFalsehoodContents(BigInteger id, String contents, String comment)
    {
        if(!cRepos.existsById(id) || !pfRepo.existsById(id))
            return "404: Falsehood not documented!";

        // To-Do: Once Storage Client is set up, send new contents to it



        // End To-Do
        PublicFalsehoodRecords records = cRepos.findById(id).get();

        records.getRecords().add(new Record("Event", "Edit", new Date(Calendar.getInstance().getTime().getTime()), 0l, comment));
        cRepos.save(records);
        return "";

    }
}