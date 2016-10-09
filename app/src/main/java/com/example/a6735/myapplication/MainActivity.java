package com.example.a6735.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import pbmsg.UserProto;

public class MainActivity extends AppCompatActivity {

    private Button Bu_send;

    private Socket socket = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // setContentView(R.layout);
        Bu_send = (Button) findViewById(R.id.send_button);
        Bu_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //网络请求必须在线程中完成
                new  Thread() {
                    public void run() {
                        //send_text();
                        new_send();
                    }
                }.start();

                //接收
                new Thread() {
                    public  void run() {
                        //get_text();
                    }
                }.start();
            }
        });
    }
    /*发送数据*/
    private void send_text() {
        try {
            socket = new Socket("192.168.1.103", 6789);
            UserProto.User uproto = UserProto.User.newBuilder().setID(147258369)
                    .setPassword("123456").setUserName("jerome").build();
            uproto.writeTo(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*接收数据*/
    private void get_text() {
        while (true) {
            if(null != socket) {
                try {
                    InputStream inputStream = socket.getInputStream();
                    if(null != inputStream) {
                        byte len[] = new byte[1024];
                        int count = inputStream.read(len);
                        byte[] temp = new byte[count];
                        for (int i = 0; i < count; i++) {
                            temp[i] = len[i];
                        }

                        UserProto.User user = UserProto.User.parseFrom(temp);
                        Log.i("zwq","id:"+user.getID() + "____" + user.getUserName()
                                + "____" + user.getPassword());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }

            }
        }
    }
    /*新的方式*/
    private void new_send() {
        //AndroidNetworking.initialize(getApplicationContext());

        UserProto.User uproto = UserProto.User.newBuilder().setID(147258369)
                .setPassword("123456").setUserName("jerome").build();
        byte[] content = uproto.toByteArray();

//        AndroidNetworking.post("http://192.168.1.103:8080/HttpTest/")
//                //.addBodyParameter("firstname", "Amit")
//                //.addBodyParameter("lastname", "Shekhar")
//                .addByteBody(content)
//                .setTag("test")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONArray(new JSONArrayRequestListener() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        // do anything with response
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });
        try {
            URL httpUrl = new URL("http://192.168.1.103:8080/HttpTest");
            HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            OutputStream out = conn.getOutputStream();
            String TTT="name=123&age=789";
            out.write(TTT.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str=reader.readLine())!=null) {
                sb.append(str);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

//    MobadsRequest adrequest = MobadsRequest.newBuilder().setRequestId(requestId).setAdslot(adslot).build();
//    byte[] content = adrequest.toByteArray();
//    HttpClient client = new HttpClient();
//    PostMethod postMethod = new PostMethod(URL);
//    postMethod.addRequestHeader("Content-Type", "application/octet-stream;charset=utf-8");
//    postMethod.setRequestEntity(new ByteArrayRequestEntity(content ));
//    client.executeMethod(postMethod);
    //注意content-type 设置为application/octet-stream。
}
