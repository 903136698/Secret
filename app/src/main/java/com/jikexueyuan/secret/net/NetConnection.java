package com.jikexueyuan.secret.net;

import android.os.AsyncTask;

import com.jikexueyuan.secret.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by MiracleWong on 2015/7/3.
 */
public class NetConnection {
    public NetConnection(final String url, final HttpMethod method, final SuccessCallback successCallback, final FailCallback failCallback, final String... kvs){
        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... params) {
                StringBuffer paramsStr = new StringBuffer();
                for (int i= 0; i < kvs.length; i+=2){
                    paramsStr.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
                }
                try {
                    URLConnection uc;

                    switch (method){
                        // POST的时候是以流的方式
                        case POST:
                            uc = new URL(url).openConnection();
                            uc.setDoOutput(true);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(), Config.CHARSET));
                            bw.write(paramsStr.toString());     // 写到本机内存
                            bw.flush();         // 立即发送到服务器
                            break;
                        // GET的时候直接放入到URL的参数当中去
                        default:
                            uc = new URL(url+"?"+paramsStr.toString()).openConnection();
                            break;
                    }
                    System.out.println("Request url:" + uc.getURL());
                    System.out.println("Request data:" + paramsStr);

                    BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), Config.CHARSET));
                    String line = null;

                    StringBuffer result = new StringBuffer();
                    while ((line=br.readLine())!=null){
                        result.append(line);
                    }
                    System.out.println("Result"+result);
                    return  result.toString();
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            // 此方法是在doInBackground()方法执行之后，将其返回值
            @Override
            protected void onPostExecute(String result) {

                if (result != null){
                    if (successCallback != null){
                        successCallback.onSuccess(result);
                    }
                }else {
                    if (failCallback != null){
                        failCallback.onFail();
                    }
                }
                super.onPostExecute(result);
            }
        }.execute();
    }

    public static interface SuccessCallback{
        void onSuccess(String result);
    }

    public static interface FailCallback{
        void onFail();
    }
}
