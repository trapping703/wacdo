package com.gdu.wacdo.dto.response;

import com.gdu.wacdo.status.CodeReponse;
import lombok.Data;

@Data
public class ReponseService<T> {

    private CodeReponse status;
    private T data;
    private Exception exception;

    public ReponseService(CodeReponse status, T data) {
        this.status = status;
        this.data = data;
    }

    public ReponseService(CodeReponse status, T data, Exception exception) {
        this.status = status;
        this.data = data;
        this.exception = exception;
    }

    public static <T> ReponseService<T> reponse(CodeReponse status, T data) {
        return  new ReponseService<T>(status, data);
    }

    public static <T> ReponseService<T> reponse(CodeReponse status, T data, Exception exception) {
        return  new ReponseService<T>(status, data, exception);
    }

    public boolean isOk(){
        return this.status.equals(CodeReponse.OK);
    }
}
