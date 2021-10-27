package com.trecapps.users.models;

import com.microsoft.graph.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPost {

    boolean accountEnabled = true;

    String displayName;

    String mailNickname;

    String userPrincipalName;

    PasswordProfile passwordProfile;

    String mobilePhone;

    OffsetDateTime birthday;
}
