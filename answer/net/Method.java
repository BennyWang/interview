package test.interview.net;

/**
 * Created by wanghaitao on 2017/12/12.
 */

public enum Method {

    GET("GET"),

    POST("POST"),

    DELETE("DELETE"),

    PUT("PUT");

    private String value;

    Method(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
