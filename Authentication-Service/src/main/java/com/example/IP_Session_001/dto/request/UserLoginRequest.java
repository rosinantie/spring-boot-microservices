package com.example.IP_Session_001.dto.request;

import com.example.IP_Session_001.Enum.RoleEnum;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequest {

    private String email;

    private String password;

    private String mobile;

    private String language;

    private Set<RoleEnum> roleEnum;          // 1 = Admin, 2 = User, etc.
}
