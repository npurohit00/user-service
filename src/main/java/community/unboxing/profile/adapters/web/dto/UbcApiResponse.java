package community.unboxing.profile.adapters.web.dto;

import java.util.Collections;
import java.util.List;

import lombok.Builder;

@Builder
public class UbcApiResponse<T> {

    private final T result;
    private final boolean success;
    private final List<ErrorMessages> errorMessages;

    public T getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<ErrorMessages> getErrorMessages() {
        return errorMessages == null || errorMessages.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(errorMessages);
    }

    @Builder
    public static class ErrorMessages {

        private String errorCode;
        private String errorMessage;

        public String getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

    }

}