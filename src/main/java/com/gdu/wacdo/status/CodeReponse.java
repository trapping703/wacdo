package com.gdu.wacdo.status;


public enum CodeReponse {

    OK,
    ERROR,
    EMPTY;

    public boolean isOK() {
        return this == OK;
    }

    public boolean isError() {
        return this == ERROR;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
