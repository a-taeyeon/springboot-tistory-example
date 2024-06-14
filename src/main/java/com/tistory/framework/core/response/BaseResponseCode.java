package com.tistory.framework.core.response;

/**
 * 0000, 9000 대를 Base Response Code로 사용하고
 * 나머지는 서비스 별로 사용한다.
 */
public enum BaseResponseCode {
    SUCCESS("0000", "성공하였습니다."),
    FAIL("9999", "SYSTEM ERROR"),


    REQUEST_NOT_FOUND("9101", "필수 값이 존재하지 않습니다."),
    REQUEST_NOT_SUPPORT_METHOD("9102", "지원하지 않는 요청입니다."),
    REQUEST_NOT_READABLE("9103", "처리 할 수 없는 데이터 입니다."),
    INVALID_DEFINITION("9104", "데이터 처리 실패"), // JSON 등 데이터 정의 가 잘못됨
    MEDIA_TYPE_NOT_SUPPORTED("9105", "지원하지 않는 형식입니다."),
    DATA_NOT_FOUND("9106", "조회된 데이터가 없습니다."), // 조회된 값이 없는 경우


    // SQL Exception 9200
    SQL_EXCEPTION("9200", "SQL Exception"),
    SQL_INSERT_EXCEPTION("9201", "등록 실패"),
    SQL_UPDATE_EXCEPTION("9202", "수정 실패"),
    SQL_DELETE_EXCEPTION("9203", "삭제 실패"),
    SQL_DUPLICATE_DATA("9102", "데이터 중복"),


    // Token 9300
    SUCCESS_AUTH("9300", "인증 성공"),
    INVALID_AUTH("9301", "인증 실패"),
    ISSUE_TOKEN("9310", "재발행 실패"),
    INCORRECT_TOKEN_TYPE("9302", "토큰 유형이 잘못되었습니다."),
    VERIFY_TOKEN_FAIL("9303", "토큰 유효성 검사에 실패하였습니다."),
    VERIFY_TOKEN_HAS_EXPIRED("9304", "토큰 만료되었습다.")
    ;


    private String code;
    private String message;

    BaseResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    // 양수면 성공에 대한 코드
    // 음수면 오류 코드
    public boolean isError(){
        return Integer.parseInt(code) > 0;
    }

    // 입력된 코드와 일치하는 BaseResponseCode 열거형 상수를 반환
    public boolean equals(String compare){
        return this.code.equals(compare);
    }
    public static BaseResponseCode to(String code) {
        for (BaseResponseCode element : BaseResponseCode.values()) {
            if (element.equals(code)) {
                return element;
            }
        }
        return SUCCESS;
    }
}
