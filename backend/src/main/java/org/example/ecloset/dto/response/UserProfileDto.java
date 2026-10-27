package org.example.ecloset.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private Integer userId;
    private String username;
    private String email;
    private LocalDate birthday;
    private BigDecimal latitude;
    private BigDecimal longitude;
}