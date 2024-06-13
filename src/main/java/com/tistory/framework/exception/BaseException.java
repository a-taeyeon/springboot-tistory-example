package com.tistory.framework.exception;

import com.tistory.framework.core.response.BaseResponseCode;

import java.util.Map;

public class BaseException extends RuntimeException {
    private static final long serialVersionUID = -1L;
    private final String code;
    private Map<String, Object> info;

    /**
     * SystemResponseCode 로 에러처리
     * @param response
     */
    public BaseException(BaseResponseCode response) {
        super(response.getMessage());
        this.code = response.getCode();
    }

    /**
     * SystemResponseCode 코드와 임의 메세지 처리
     * @param response
     * @param message
     */
    public BaseException(BaseResponseCode response, String message) {
        super(message);
        this.code = response.getCode();
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 일반 메세지 처리 오류코드는 9999 Fixed
     * @param message
     */
    public BaseException(String message) {
        super(message);
        this.code = BaseResponseCode.FAIL.getCode();
    }

    /**
     * Excetion 던지는 경우 info (에 데이터를 전달할때 사용)
     * @param message
     * @param info
     */
    public BaseException(String message, Map<String, Object> info) {
        super(message);
        this.code = BaseResponseCode.FAIL.getCode();
        this.info = info;

    }

    public Map<String, Object> getInfo() {
        return this.info;
    }

    public String getCode() {
        return code;
    }
}
