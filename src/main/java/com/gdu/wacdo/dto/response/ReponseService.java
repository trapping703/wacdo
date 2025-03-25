package com.gdu.wacdo.dto.response;

import com.gdu.wacdo.status.CodeReponse;
import lombok.Data;

@Data
public class ReponseService<T extends Object> {

    private CodeReponse status;
    private Object data;
    private Exception exception;

    public ReponseService(CodeReponse status, Object data) {
        this.status = status;
        this.data = data;
    }

    public ReponseService(CodeReponse status, Object data, Exception exception) {
        this.status = status;
        this.data = data;
        this.exception = exception;
    }

    public static ReponseService reponse(CodeReponse status, Object data) {
        return new ReponseService(status, data);
    }

    public static ReponseService reponse(CodeReponse status, Object data, Exception exception) {
        return new ReponseService(status, data, exception);
    }

    public T getObjetRetour() {
        return (T) data;
    }

    public boolean isOk() {
        return this.status.isOK();
    }

    public boolean isError() {
        return this.status.isError();
    }

    public boolean isEmpty() {
        return this.status.isEmpty();
    }
}
