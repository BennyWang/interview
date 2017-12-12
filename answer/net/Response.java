package test.interview.net;

/**
 * Created by wanghaitao on 2017/12/12.
 */

public class Response {

    public static final int HTTP_ERROR = -1;

    private int code;

    private Object data;

    protected Response(int code,Object data){
        this.code = code;
        this.data = data;
    }


    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
