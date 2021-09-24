package com.trecapps.internal.storage.repos;

import com.trecapps.internal.storage.models.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDataRepo extends JpaRepository<FileData, String> {

}
