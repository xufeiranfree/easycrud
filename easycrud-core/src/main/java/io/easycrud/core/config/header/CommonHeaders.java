package io.easycrud.core.config.header;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonHeaders {
    private String language;
    private String userId;

    // Constructors, getters, and setters
}