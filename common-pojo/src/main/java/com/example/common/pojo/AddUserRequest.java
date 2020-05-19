package com.example.common.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class AddUserRequest implements Serializable {
    @NotNull
    @Size(min = 5, max = 30)
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;
}
