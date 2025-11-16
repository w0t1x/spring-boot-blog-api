package org.example.dto;

import lombok.Data;

@Data
public class AuthorDto {
    private Long id;
    private String name;
    private String email;

    public AuthorDto() {
    }

    public AuthorDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
