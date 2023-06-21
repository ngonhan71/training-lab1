package com.tma.mapper;

import com.tma.model.dto.user.UserBasicDTO;
import com.tma.model.dto.user.UserCreateDTO;
import com.tma.model.dto.user.UserRegisterDTO;
import com.tma.model.entity.user.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AppUser fromRegisterToEntity(UserRegisterDTO userRegisterDTO);

    AppUser fromCreateToEntity(UserCreateDTO userCreateDTO);

    UserBasicDTO fromEntityToBasic(AppUser appUser);
}
