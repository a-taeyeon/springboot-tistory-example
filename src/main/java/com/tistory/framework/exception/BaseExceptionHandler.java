package com.tistory.framework.exception;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.tistory.framework.core.response.BaseResponse;
import com.tistory.framework.core.response.BaseResponseCode;
import com.tistory.framework.core.utils.BaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler extends RuntimeException {
    /**
     * BaseException 이외 모든 Exception 처리
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<BaseResponse> handleCustomException(Exception e) {

        log.error(BaseUtils.getPrintStackTrace(e));

        log.error(">>>> excption Class : {}", e.getClass().getSimpleName());

        if (e instanceof HttpMessageNotReadableException) {
            BaseResponse res = new BaseResponse(BaseResponseCode.REQUEST_NOT_READABLE);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(res);

        }

        if (e instanceof HttpMediaTypeNotSupportedException) {
            BaseResponse res = new BaseResponse(BaseResponseCode.MEDIA_TYPE_NOT_SUPPORTED);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(res);

        }

        if (e instanceof InvalidDefinitionException) {
            BaseResponse res = new BaseResponse(BaseResponseCode.INVALID_DEFINITION);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(res);
        }

        // 요청 메소드가 다름
        if (e instanceof HttpRequestMethodNotSupportedException) {
            BaseResponse res = new BaseResponse(BaseResponseCode.REQUEST_NOT_SUPPORT_METHOD);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(res);

        }

        // 중복키
        if (e instanceof DuplicateKeyException) {
            BaseResponse res = new BaseResponse(BaseResponseCode.SQL_DUPLICATE_DATA);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(res);
        }

        BaseResponse res = new BaseResponse();
        res.setCode(e.getClass().getSimpleName());
        res.setMessage(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(res);
    }

    /**
     * BaseException 으로 처리 하는것은 아래로 처리
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BaseException.class})
    protected ResponseEntity<BaseResponse> handleCustomException(BaseException e) {

        BaseResponse res = new BaseResponse();
        res.setCode(e.getCode());
        res.setMessage(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }
}
