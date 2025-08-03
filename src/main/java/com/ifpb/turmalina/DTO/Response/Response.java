package com.ifpb.turmalina.DTO.Response;

public record Response<T>(String httpStatus, String message, T objeto) {
}
