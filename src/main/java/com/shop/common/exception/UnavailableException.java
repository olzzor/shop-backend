package com.shop.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * 유효성 검사 실패 시 발생하는 예외입니다.
 * 이 예외는 400 BAD REQUEST 상태 코드의 HTTP 응답을 생성합니다.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnavailableException extends RuntimeException {
    private final String errorCode;
    private Map<String, Object> body;

    /**
     * 지정된 오류 코드 및 세부 메시지로 새 UnavailableException을 구성합니다.
     *
     * @param errorCode 오류 코드.
     * @param message   세부 메시지.
     */
    public UnavailableException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 지정된 오류 코드, 세부 메시지 및 본문으로 새 UnavailableException을 구성합니다.
     *
     * @param errorCode 오류 코드.
     * @param message   세부 메시지.
     * @param body      응답 본문.
     */
    public UnavailableException(String errorCode, String message, Map<String, Object> body) {
        super(message);
        this.errorCode = errorCode;
        this.body = body;
    }

    /**
     * 지정된 세부 메시지와 원인으로 새 UnavailableException을 구성합니다.
     *
     * @param message 세부 메시지.
     * @param cause   원인.
     */
    public UnavailableException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "";
    }

    /**
     * 이 예외와 연결된 오류 코드를 반환합니다.
     *
     * @return 오류 코드.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 이 예외와 연결된 응답 본문을 반환합니다.
     *
     * @return 응답 본문.
     */
    public Map<String, Object> getBody() {
        return body;
    }
}
