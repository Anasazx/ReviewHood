package tn.anasazx.tunirate.enums;


public enum CompanyRole {
    HEAD,
    WORKER;

    public boolean canDeleteComments() {
        return this == HEAD || this == WORKER;
    }


}

