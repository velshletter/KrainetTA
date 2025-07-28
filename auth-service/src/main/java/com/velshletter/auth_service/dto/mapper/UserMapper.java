package com.velshletter.auth_service.dto.mapper;


import com.velshletter.auth_service.dto.UserResponseDto;
import com.velshletter.auth_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponseDto toUserDto(User user);

}
