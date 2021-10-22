package com.trecapps.internal.storage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    String userAccount;

    Date added;

    @Id
    String id;

    String fileName;

    byte fileType;

    byte nsfwRating;

    String ext;

    String app; // Use for File Container
    
    public static Optional<String> getHeaderValue(HttpHeaders headers, String header)
    {
        String val = headers.getFirst(header);
        return val != null ? Optional.of(val) : Optional.empty();
    }

    public static FileData getFileData(HttpHeaders headers)
    {
        Optional<String> type = getHeaderValue(headers, "App");
        if (type.isEmpty())
            throw new IllegalArgumentException("Need App name to determine Storage container");
        String appName = type.get();

        Date added = new Date(Calendar.getInstance(TimeZone.getTimeZone("CST")).getTime().getTime());
        byte fileType = -1;

        String fileName = null, ext = null;
        byte nsfwRating = 0;

        String account;
        type = getHeaderValue(headers, "Account");

        if(type.isEmpty())
            throw new IllegalArgumentException("Adding new File must have an account associated with it!");
        account = (type.get());


        type = getHeaderValue(headers, "Content-type");


        if(type.isPresent())
        {
            fileType = convertFileType(type.get());
        }
        if(fileType < 0)
            throw new IllegalArgumentException("Headers did not provide valid 'Content-type' value");

        if(fileType == 2 || fileType == 3)
        {
            type = getHeaderValue(headers, "Extension");
            if(type.isEmpty())
                throw new IllegalArgumentException("Files with 'code' or 'document' for 'Content-type' MUST have a valid file 'Extension' field");
            ext = type.get();
        }

        type = getHeaderValue(headers, "FileName");

        if(type.isPresent())
            fileName = type.get();

        type = getHeaderValue(headers, "Content-mod");

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

        return new FileData(account,added, null, fileName, fileType, nsfwRating, ext, appName);
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
