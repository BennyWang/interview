package test.interview.net;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wanghaitao on 2017/12/12.
 */

public class RequestManagerImpl implements RequestManager {

    private List<RequestHandler> requestList = new ArrayList<>();


    @Override
    public void request(Request request,Callback callback) {
        HttpExecuteThread executeThread = new HttpExecuteThread(this,request);
        RequestHandler requestHandler = new RequestHandler();
        requestHandler.request = request;
        requestHandler.httpExecuteThread = executeThread;
        requestHandler.callback = callback;
        executeThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void cancelRequest(Request request) {
        Iterator<RequestHandler> iterator = requestList.iterator();
        while (iterator.hasNext()){
            RequestHandler requestHandler = iterator.next();
            if(requestHandler.request == request){
                requestHandler.httpExecuteThread.cancel(true);
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void cancelAllRequest() {
        for(RequestHandler requestHandler:requestList){
            requestHandler.httpExecuteThread.cancel(true);
        }
        requestList.clear();
    }

    @Override
    public void handleResponse(Request request, Response response) {
        for(RequestHandler requestHandler:requestList){
            if(requestHandler.request == request){
                requestHandler.callback.httpCallback(response);
                break;
            }
        }
    }

    private class RequestHandler{

        private Request request;

        private HttpExecuteThread httpExecuteThread;

        private Callback callback;

    }
}
