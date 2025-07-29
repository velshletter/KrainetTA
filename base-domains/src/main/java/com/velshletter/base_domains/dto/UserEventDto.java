package com.velshletter.base_domains.dto;

import lombok.Builder;

@Builder
public record UserEventDto(
        ActionType actionType,
        String username,
        String email,
        String password
) {}
