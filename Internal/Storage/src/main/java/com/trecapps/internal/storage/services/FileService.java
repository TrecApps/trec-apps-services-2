package com.trecapps.internal.storage.services;

import com.microsoft.azure.storage.StorageException;
import com.trecapps.internal.storage.models.FileData;
import com.trecapps.internal.storage.repos.FileDataRepo;
import com.trecapps.internal.storage.repos.StorageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class FileService {

    @Autowired
    FileDataRepo tableRepo;

    @Autowired
    StorageRepo storageRepo;

    public String uploadFile(RequestEntity<String> request)
    {
        try
        {
            FileData data = FileData.getFileData(request.getHeaders());

            // To-Do: Use Azure Cognitive Services to assess maturity level of pictures
            byte type = data.getFileType();
            if(type > 3)
            {
                // We are dealing with an image
                // To-Do: Use Azure Cognitive Services to assess maturity level of pictures
            }

            boolean useBase64 = type > 3 ? true : (type == 3 ? !"md".equals(data.getExt()) : false);

            data = tableRepo.save(data);

            storageRepo.uploadContent(data.getApp(), data.getId(), request.getBody(),useBase64);
        } catch(IllegalArgumentException e)
        {
            return "CLIENT: " + e.getMessage();
        }
        catch(StorageException e)
        {
            return "502: " + e.getMessage();
        }
        catch(Exception e)
        {
            return "500: " + e.getMessage();
        }
        return "";
    }

    public FileData getFileData(String id, String app, Long userId) throws IllegalAccessException
    {
        if(!tableRepo.existsById(id))
            return null;

        // To-Do: Determine wither access is allowed

        return tableRepo.getById(id);
    }

    public String getFile(String id, String app, Long userId) throws URISyntaxException, IOException, StorageException, IllegalAccessException {
        if(!tableRepo.existsById(id))
            return "404";

        // To-Do: Determine wither access is allowed

        FileData data = tableRepo.getById(id);
        byte type = data.getFileType();
        boolean useBase64 = type > 3 ? true : (type == 3 ? !"md".equals(data.getExt()) : false);


        return storageRepo.getContent(data.getApp(), id, useBase64);
    }

    public int deleteFile(String id, Long userId)
    {
        if(userId == null)
            return 401;

        if(!tableRepo.existsById(id))
            return 404;

        FileData data = tableRepo.getById(id);
        if(!userId.equals(data.getUserAccount()))
            return 403;

        try
        {
            storageRepo.deleteContent(data.getApp(), id);

            tableRepo.delete(data);
        } catch (URISyntaxException e) {
            return 500;
        } catch (StorageException e) {
            return 502;
        }
        return 204;
    }
}
