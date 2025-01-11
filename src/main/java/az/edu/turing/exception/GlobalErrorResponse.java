package az.edu.turing.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalErrorResponse {

    private UUID requestId;
    private String errorCode;
    private String errorMessage;
    private LocalDateTime timeStamp;
}
