package io.kr.assignmentserver.extension.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionMessage {

    private final String errorMsg;

    @Builder
    public ExceptionMessage(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
