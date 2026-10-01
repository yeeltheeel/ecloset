package org.example.ecloset.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest {

    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String username;

    private LocalDate birthday;

    @Size(max = 100, message = "Локация не может быть длиннее 100 символов")
    private String location;
}