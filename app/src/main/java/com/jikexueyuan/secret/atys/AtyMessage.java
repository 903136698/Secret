package com.jikexueyuan.secret.atys;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jikexueyuan.secret.Config;
import com.jikexueyuan.secret.R;
import com.jikexueyuan.secret.net.Comment;
import com.jikexueyuan.secret.net.GetComment;
import com.jikexueyuan.secret.net.PubComment;
import com.jikexueyuan.secret.tools.MD5Tool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by MiracleWong on 2015/7/3.
 */
public class AtyMessage extends ListActivity {

    @InjectView(R.id.tvMessage)
    TextView tvMessage;
    @InjectView(android.R.id.list)
    ListView list;
    @InjectView(R.id.etAddComment)
    EditText etAddComment;
    @InjectView(R.id.btnSendComment)
    Button btnSendComment;
    private String phone_md5, msg, msgId, token;

    private AtyMessageCommentListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_message);
        ButterKnife.inject(this);

        adapter = new AtyMessageCommentListAdapter(this);
        setListAdapter(adapter);

        Intent data = getIntent();
        phone_md5 = data.getStringExtra(Config.KEY_PHONE_MD5);
        msg = data.getStringExtra(Config.KEY_MSG);
        msgId = data.getStringExtra(Config.KEY_MSG_ID);
        token = data.getStringExtra(Config.KEY_TOKEN);

        tvMessage.setText(msg);


        getComments();

        final ProgressDialog pd = ProgressDialog.show(this, getResources().getString(R.string.connecting), getResources().getString(R.string.connecting_to_server));
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etAddComment.getText())){
                    Toast.makeText(AtyMessage.this, R.string.comment_content_can_not_be_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                new PubComment(MD5Tool.md5(Config.getCachedPhoneNum(AtyMessage.this)), token, etAddComment.getText().toString(), msgId, new PubComment.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        etAddComment.setText("");       // 清空

                        getComments();
                    }
                }, new PubComment.FailCallback() {
                    @Override
                    public void onFail(int errorCode) {
                        pd.dismiss();

                        if (errorCode==Config.RESULT_STATUS_INVALID_TOKEN) {

                            startActivity(new Intent(AtyMessage.this, AtyLogin.class));
                            finish();
                        }else{
                            Toast.makeText(AtyMessage.this, R.string.fail_to_pub_comment, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    private void getComments() {
        final ProgressDialog pd = ProgressDialog.show(this, getResources().getString(R.string.connecting), getResources().getString(R.string.connecting_to_server));
        new GetComment(phone_md5, token, msgId, 1, 20, new GetComment.SuccessCallback() {
            @Override
            public void onSuccess(String msgId, int page, int perpage, List<Comment> comments) {
                pd.dismiss();
                adapter.clear();
                adapter.addAll(comments);
            }
        }, new GetComment.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                pd.dismiss();

                if (errorCode== Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(AtyMessage.this, AtyLogin.class));
                    finish();
                }else{
                    Toast.makeText(AtyMessage.this, R.string.fail_to_get_comment, Toast.LENGTH_LONG).show();
                }
            }
        }
        );
    }


}
