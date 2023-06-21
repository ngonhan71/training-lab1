package com.tma.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsePageDTO {
    private Object data;
    private Object limit;
    private Object currentPage;
    private Object totalItems;
    private Object totalPages;
}
