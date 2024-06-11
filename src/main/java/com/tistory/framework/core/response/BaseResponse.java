package com.tistory.framework.core.response;

import lombok.Data;

/**
 * base Response 객체
 */
@Data
public class BaseResponse<T> {
    private String code;

    private String message;

    private T result;

    public BaseResponse() {
        this.code = BaseResponseCode.SUCCESS.getCode();
        this.message = BaseResponseCode.SUCCESS.getMessage();
    }

    public BaseResponse(BaseResponseCode baseResponseCode) {
        this.code = baseResponseCode.getCode();
        this.message = baseResponseCode.getMessage();
    }
}
