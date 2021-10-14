package com.trecapps.falsehoods.falsehoodReview.services;

import com.trecapps.base.FalsehoodModel.models.Falsehood;
import com.trecapps.base.FalsehoodModel.models.FalsehoodRecords;
import com.trecapps.base.FalsehoodModel.repos.FalsehoodRecordsRepo;
import com.trecapps.base.FalsehoodModel.repos.FalsehoodRepo;
import com.trecapps.base.InfoResource.models.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;

@Service
public class MediaFalsehoodsService {
    @Autowired
    FalsehoodRepo repo;

    @Autowired
    FalsehoodRecordsRepo cRepos;

    public String addVerdict(BigInteger id, String approve, String comment)
    {
        // Make sure target falsehood is in repos
        if(!cRepos.existsById(id) || !repo.existsById(id))
            return "404: Falsehood does not exist!";

        // Make Sure Request hasn't been rejected or approved
        Falsehood f = repo.getById(id);
        int s = f.getStatus();
        if(s > 1)
            return "400: Falsehood already has a Final Verdict!";

        // To-Do: Once User Support is added, make sure that this user is not the submitting user


        // End To-DO

        Record record = new Record("Verdict", approve, new Date(Calendar.getInstance().getTime().getTime()), 0l, comment);

        Optional<FalsehoodRecords> oRecords = cRepos.findById(id);
        if(oRecords.isEmpty())
            return "500: Failed to detect Absence of Specific Falsehood!";

        FalsehoodRecords records = oRecords.get();
        records.getRecords().add(record);
        records = cRepos.save(records);

        int appCount = 0, safeRej = 0, penRej = 0;
        for(Record r: records.getRecords())
        {
            if("Verdict".equals(r.getRecordType()))
            {
                switch(r.getDetails())
                {
                    case "Approved":
                        appCount++;
                        break;
                    case "Safe-Reject":
                        safeRej++;
                        break;
                    case "Penalize":
                        penRej++;
                }
            }
        }

        if(appCount >= (2 * (safeRej + penRej)))
            f.setStatus((byte)2);
        if((safeRej + penRej) >= (appCount * 2))
        {
            f.setStatus((byte)5);
            if(penRej > safeRej)
            {
                // To-Do: Penalize Submitter

                // End To-Do
            }
        }
        repo.save(f);

        return "";
    }
}
