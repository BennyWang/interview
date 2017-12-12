package test.interview.net;


/**
 * Created by wanghaitao on 2017/12/12.
 */

public interface RequestManager extends HttpHandler{

    void request(Request request,Callback callback);

    void cancelRequest(Request request);

    void cancelAllRequest();

}
