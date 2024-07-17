package com.madeeasy.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}

