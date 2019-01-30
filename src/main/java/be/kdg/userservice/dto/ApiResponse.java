package be.kdg.userservice.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse {
    private int status;
    private String message;
    private Object result;

    public ApiResponse(HttpStatus status, String message, Object result){
        this.status = status.value();
        this.message = message;
        this.result = result;
    }

    @Override
    public String toString() {
        return "ApiResponse [statusCode=" + status + ", message=" + message +"]";
    }
}
