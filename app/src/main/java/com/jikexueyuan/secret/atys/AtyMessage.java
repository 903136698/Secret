package com.jikexueyuan.secret.atys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jikexueyuan.secret.Config;
import com.jikexueyuan.secret.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by MiracleWong on 2015/7/3.
 */
public class AtyMessage extends Activity {

    @InjectView(R.id.tvMessage)
    TextView tvMessage;
    @InjectView(android.R.id.list)
    ListView list;
    @InjectView(R.id.etAddComment)
    EditText etAddComment;
    @InjectView(R.id.btnSendComment)
    Button btnSendComment;
    private String phone_md5, msg, msgId, token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_message);
        ButterKnife.inject(this);

        Intent data = getIntent();
        phone_md5 = data.getStringExtra(Config.KEY_PHONE_MD5);
        msg = data.getStringExtra(Config.KEY_MSG);
        msgId = data.getStringExtra(Config.KEY_MSG_ID);
        token = data.getStringExtra(Config.KEY_TOKEN);

        tvMessage.setText(msg);
    }


}
