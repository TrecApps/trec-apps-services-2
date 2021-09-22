package com.trecapps.internal.storage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.net.http.HttpHeaders;
import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileData
{
    Long userAccount;

    Date added;

    @Id
    String id;

    String fileName;

    byte fileType;

    byte nsfwRating;

    String ext;

    public static FileData getFileData(HttpHeaders headers)
    {
        Date added = new Date(Calendar.getInstance(TimeZone.getTimeZone("CST")).getTime().getTime());
        byte fileType = -1;

        String fileName = null, ext = null;
        byte nsfwRating = 0;

        Long account;
        Optional<String> type = headers.firstValue("Account");

        if(type.isEmpty())
            throw new IllegalArgumentException("Adding new File must have an account accosiated with it!");
        try
        {
            account = Long.parseLong(type.get());
        } catch(NumberFormatException ex)
        {
            throw new IllegalArgumentException("Failed to parse Account Number from provided field!", ex);
        }

        type = headers.firstValue("Content-type");


        if(type.isPresent())
        {
            fileType = convertFileType(type.get());
        }
        if(fileType < 0)
            throw new IllegalArgumentException("Headers did not provide valid 'Content-type' value");

        if(fileType == 2 || fileType == 3)
        {
            type = headers.firstValue("Extension");
            if(type.isEmpty())
                throw new IllegalArgumentException("Files with 'code' or 'document' for 'Content-type' MUST have a valid file 'Extension' field");
            ext = type.get();
        }

        type = headers.firstValue("FileName");

        if(type.isPresent())
            fileName = type.get();

        type = headers.firstValue("Content-mod");

        if(type.isPresent())
        {
            switch(type.get())
            {
                case "safe":
                case "clean":
                default:
                    break;
                    // If all relevent subjects were men, rating would be 0
                case "w-21":
                    nsfwRating = 1;
                    break;
                case "w-18":
                    nsfwRating = 2;
                    break;
                case "all-21":
                    nsfwRating = 3;
                    break;
                case "all-18":
                    nsfwRating = 4;
            }
        }

        return new FileData(account,added, null, fileName, fileType, nsfwRating, ext);
    }

    public static byte convertFileType(String type)
    {
        switch(type.toLowerCase())
        {
            case "application/html":
            case "text/html":
            case "html":
                return 1;
            case "code":
                return 2;
            case "document":
                return 3;
            case "image/gif":
                return 4;
            case "image/jpeg":
                return 5;
            case "image/tiff":
                return 6;
            case "image/png":
                return 7;
            default:
                return -1;
        }
    }
}
