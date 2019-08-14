package test.interview.net;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wanghaitao on 2017/12/12.
 */

public class HttpExecuteThread extends AsyncTask<Void,Void,Response> {

    private final static int CONNECT_TIMEOUT = 10*1000;
    private final static int READ_TIMEOUT = 10*1000;

    private final RequestManager requestManager;
    private final Request request;


    public HttpExecuteThread(RequestManager requestManager,Request request){
        this.requestManager = requestManager;
        this.request = request;
    }


    @Override
    protected Response doInBackground(Void... params) {
        Method method = request.getMethod();
        String paramsString = request.paramsString();

        try {
            String str = request.getUrl();
            if(method != Method.POST){
                str = str + "?" + request.paramsString();
            }
            URL url = new URL(str);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method.getValue());// 提交模式
            connection.setConnectTimeout(CONNECT_TIMEOUT);//连接超时 单位毫秒
            connection.setReadTimeout(READ_TIMEOUT);//读取超时 单位毫秒
            connection.setDoOutput(true);
            connection.setDoInput(true);
            PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            if(method == Method.POST){
                printWriter.write(paramsString);
                // flush输出流的缓冲
                printWriter.flush();
            }
            int code = connection.getResponseCode();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                bos.write(arr, 0, len);
                bos.flush();
            }
            bos.close();
            String data = bos.toString("utf-8");
            return new Response(code,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(Response.HTTP_ERROR,"http error");
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        requestManager.handleResponse(request,response);
    }
}
