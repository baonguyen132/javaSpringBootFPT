package net.codejava.Application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import net.codejava.Application.dto.request.UserCreationRequest;
import net.codejava.Application.dto.request.UserUpdateRequest;
import net.codejava.Application.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    void userUpdate(UserUpdateRequest request, @MappingTarget User user);
}