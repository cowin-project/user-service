package com.userService.user.service.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PagedResponse<T> {
    List<T> content;
    int page;
    int size;
    long totalElements;
    int totalPages;
}
