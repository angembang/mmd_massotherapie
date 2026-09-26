package fr.mmdmassotherapie.backend.bo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogicResult<T> {
    // Getters & Setters
    private String code;
    private String message;
    private T data;

    // Constructors
    public LogicResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public LogicResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}

