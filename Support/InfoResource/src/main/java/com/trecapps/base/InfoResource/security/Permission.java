package com.trecapps.base.InfoResource.security;

import lombok.Data;

import java.util.List;

@Data
public class Permission {

    String user;

    List<String> permission;
}
