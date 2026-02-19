package com.codegnan.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavouriteResponseDTO {

    private Long id;
    private Integer userId;
    private Long productId;
    private LocalDateTime addedAt;
}

