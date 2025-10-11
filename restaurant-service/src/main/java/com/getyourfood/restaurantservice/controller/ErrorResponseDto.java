package com.getyourfood.restaurantservice.controller;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
  private final LocalDateTime timeStamp;
  private final String error;
  private final String message;

  public static ErrorResponseDto of(String error, String message) {
    return ErrorResponseDto.builder()
        .timeStamp(LocalDateTime.now())
        .error(error)
        .message(message)
        .build();
  }
}
