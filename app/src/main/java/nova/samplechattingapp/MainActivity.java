package nova.samplechattingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private String html;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private String ip = "115.68.231.13";
    private int port = 9999;

    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText et = (EditText) findViewById(R.id.EditText01);
        Button btn = (Button) findViewById(R.id.Button01);


        // Socket making thread
        new Thread(new Runnable() {
            public void run() {
                try {
                    setSocket(ip, port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final TextView tv = (TextView) findViewById(R.id.TextView01);
                final ScrollView sv = (ScrollView) findViewById(R.id.scrollView01);
                Log.w("ChattingStart", "Start Thread");
                tv.append("--채팅 시작--\n");
                while (true) {
                    try {
                        html = dis.readUTF();
                        if (html != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.append(html + "\n");
                                    sv.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // "send" button listener
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (et.getText().toString() != null && !et.getText().toString().equals("")) {
                    String return_msg = et.getText().toString();
                    et.setText(""); // clear edit text view




                    try {

                        SendMessageTask sendMessageTask = new SendMessageTask(return_msg);

                        sendMessageTask.execute();


//                        dos.writeUTF(return_msg);


                    }catch (Exception e){

                        e.printStackTrace();


                    }
                }
            }
        });
    }

    public void setSocket(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public class SendMessageTask extends AsyncTask<Integer, String, String> {


        // context를 가져오는 생성자. 이를 통해 메인 액티비티의 함수에 접근할 수 있다.

        String msg;
        public SendMessageTask(String msg){

            this.msg = msg;

        }

        @Override
        protected String doInBackground(Integer... integers) {


            try {
                dos.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            //게시물에 좋아요를 적용/취소하였다.

        }

    }





}

