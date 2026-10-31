package com.example.ocms.source.io;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MaterialResponseDto {
    private Long id;
    private String title;
    private String link;
    private LocalDateTime createdAt;
}