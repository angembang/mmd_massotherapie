package fr.mmdmassotherapie.backend.massage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MassageNotFoundException extends RuntimeException {

    public MassageNotFoundException(String slug) {
        super("Massage not found: " + slug);
    }
}
