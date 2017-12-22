package Android.Android;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpURLConnectionhelper {
	
    public static String get(String path)
    {
    	try {
            URL url = new URL(path.trim());
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if(200 == urlConnection.getResponseCode())
            {
                //得到输入流
                InputStream is =urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while(-1 != (len = is.read(buffer))){
                    baos.write(buffer,0,len);
                    baos.flush();
                }
                return baos.toString("utf-8");
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }   
    
    public static String post(String path,String post){
        URL url = null;
        try {
        	url = new URL(path.trim());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
            // httpURLConnection.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(post);//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while((len=bis.read(arr))!= -1){
                bos.write(arr,0,len);
                bos.flush();
            }
            bos.close();
            return bos.toString("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }   
        
	
	
	
	
    public static void put(String path)
         {
    	    URL url = null;
			try {
				url = new URL(path.trim());
			} catch (MalformedURLException exception) {
			    exception.printStackTrace();
			}
			HttpURLConnection httpURLConnection = null;
			DataOutputStream dataOutputStream = null;
			try {
			    httpURLConnection = (HttpURLConnection) url.openConnection();
			    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			    httpURLConnection.setRequestMethod("PUT");
			    httpURLConnection.setDoInput(true);
			    httpURLConnection.setDoOutput(true);
			    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			    dataOutputStream.write(1);
			    
			} catch (IOException exception) {
			    exception.printStackTrace();
			}  finally {
			    if (dataOutputStream != null) {
			        try {
			            dataOutputStream.flush();
			            dataOutputStream.close();
			        } catch (IOException exception) {
			            exception.printStackTrace();
			        }
			    }
			    if (httpURLConnection != null) {
			    	httpURLConnection.disconnect();
			    }
			}
		}

    public static void delete (String path)
    	{
			URL url = null;
			try {
				url = new URL(path.trim());
			} catch (MalformedURLException exception) {
			    exception.printStackTrace();
			}
			HttpURLConnection httpURLConnection = null;
			try {
			    httpURLConnection = (HttpURLConnection) url.openConnection();
			    httpURLConnection.setRequestProperty("Content-Type",
			                "application/x-www-form-urlencoded");
			    httpURLConnection.setRequestMethod("DELETE");
			    System.out.println(httpURLConnection.getResponseCode());
			} catch (IOException exception) {
			    exception.printStackTrace();
			} finally {         
			    if (httpURLConnection != null) {
			        httpURLConnection.disconnect();
			    }
 			}
		}
}
