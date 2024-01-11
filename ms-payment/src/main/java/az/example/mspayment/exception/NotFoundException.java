package az.example.mspayment.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException{
    private String code;

    public NotFoundException(String message){
        super(message);
        this.code=code;
    }



}
