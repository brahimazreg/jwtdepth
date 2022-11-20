package com.brahim.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProjection {
    private Integer id;
    private String name;
    private String contact;
    private String email;
    private String role;
    private String status;

}
