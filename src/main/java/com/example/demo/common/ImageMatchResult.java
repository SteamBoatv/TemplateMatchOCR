package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageMatchResult {
    private String templateName;
    private String softWareName;
    private LocalDateTime matchTime;
    private List<String> sentences;
}
