package com.example.demo.Model.DTO;

import com.example.demo.Model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private long id;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private List<Address> addresses;
}
