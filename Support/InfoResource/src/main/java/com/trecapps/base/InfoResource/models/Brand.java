package com.trecapps.base.InfoResource.models;

import com.trecapps.base.InfoResource.security.Permission;
import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Table
@Entity
@javax.persistence.Entity
@Data
public class Brand {
    @Id
    Long id;

    String type;

    Integer link;

    String name;

    String sourceLanguage;

    // What users can manifest as this account
    @Transient
    List<Permission> permissions;
}
