package com.tma.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseModelDTO {
    private boolean isSuccess;
    private Object data;
    private ErrorDTO error;
}
