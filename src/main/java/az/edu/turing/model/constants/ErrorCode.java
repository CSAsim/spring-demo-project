package az.edu.turing.model.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorCode {

    public static final String NOT_FOUND = "not_found";
    public static final String ALREADY_EXISTS = "already_exists";
    public static final String INVALID_INPUT = "invalid_input";
    public static final String BAD_REQUEST = "bad_request";
}
