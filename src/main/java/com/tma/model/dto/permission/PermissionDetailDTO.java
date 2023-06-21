package com.tma.model.dto.permission;

import lombok.Data;

import java.util.UUID;

@Data
public class PermissionDetailDTO {

    private UUID id;

    private String code;

    private String name;


}
