package com.nexpay.account_service.util;

public record DefaultResponse<T> (Integer status, String message, T data) {
}
