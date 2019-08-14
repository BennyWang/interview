package test.interview.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wanghaitao on 2017/12/12.
 */

public class Request {

    private String url;
    private Method method;
    private Map<String, String> params = new HashMap<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String paramsString(){
        if(params.isEmpty()){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator
                = params.entrySet().iterator();
        stringBuilder.append("?");
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = iterator.next();
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            stringBuilder.append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return params.toString();
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method=" + method +
                ", params=" + params +
                '}';
    }
}
