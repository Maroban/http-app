package com.javaex.http;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListAsyncTask listAsyncTask = new ListAsyncTask();
        listAsyncTask.execute();

    }

    /*** ListAsyncTask(이너 클래스) ***/
    public class ListAsyncTask extends AsyncTask<Void, Integer, List<GuestbookVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<GuestbookVo> doInBackground(Void... voids) {

            List<GuestbookVo> gList = null;

            // 서버에 연결 후 요청을 한다.
            try {
                //url 생성
                URL url = new URL("http://58.234.223.146:8088/mysite5/api/guestbook/list");

                //url 연결
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setConnectTimeout(10000);

                // 요청방식 POST
                conn.setRequestMethod("POST");

                //요청시 데이터 형식 json
                conn.setRequestProperty("Content-Type", "application/json");

                //응답시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json");

                //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoOutput(true);

                //InputStream으로 서버로 부터 응답을 받겠다는 옵션.
                conn.setDoInput(true);

                // 응답코드 200이 정상
                int resCode = conn.getResponseCode();

                Log.d("Study", "" + resCode);

                if (resCode == 200) {
                    // Stream을 통해 통신하며, 데이터 형식은 json으로 한다.
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);

                    String jsonData = "";

                    while (true) {
                        String line = br.readLine();

                        if (line == null) {
                            break;
                        }
                        jsonData = jsonData + line;
                    }

                    Gson gson = new Gson();
                    gList = gson.fromJson(jsonData, new TypeToken<List<GuestbookVo>>() {
                    }.getType());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return gList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<GuestbookVo> gList) {

            Log.d("Study", "" + gList.size());

            super.onPostExecute(gList);
        }
    }


}