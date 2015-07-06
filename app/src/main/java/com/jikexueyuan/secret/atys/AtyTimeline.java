package com.jikexueyuan.secret.atys;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.jikexueyuan.secret.Config;
import com.jikexueyuan.secret.R;
import com.jikexueyuan.secret.ld.MyContacts;
import com.jikexueyuan.secret.net.Message;
import com.jikexueyuan.secret.net.TimeLine;
import com.jikexueyuan.secret.net.UploadContacts;
import com.jikexueyuan.secret.tools.MD5Tool;

import java.util.List;

/**
 * Created by MiracleWong on 2015/7/3.
 */
public class  AtyTimeline extends ListActivity {

    private String phone_num;
    private String phone_md5;
    private String token;

    private AtyTimelineMessageListAdapter adapter=null;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_timeline);

        adapter = new AtyTimelineMessageListAdapter(this);
        setListAdapter(adapter);

        phone_num = getIntent().getStringExtra(Config.KEY_PHONE_NUM);
        token = getIntent().getStringExtra(Config.KEY_TOKEN);
        phone_md5 = MD5Tool.md5(phone_num);

        final ProgressDialog pd = ProgressDialog.show(this,getResources().getString(R.string.connecting),getResources().getString(R.string.connecting_to_server));

        new UploadContacts(phone_md5, token, MyContacts.getContactsJSONString(this), new UploadContacts.SuccessCallback() {
            @Override
            public void onSuccess() {
                pd.dismiss();

                loadMessage();
            }
        }, new UploadContacts.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                pd.dismiss();

                if (errorCode==Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(AtyTimeline.this, AtyLogin.class));
                    finish();
                }else{
                    loadMessage();
                }
            }
        });
    }

    private void loadMessage() {
        final ProgressDialog pd = ProgressDialog.show(this,getResources().getString(R.string.connecting),getResources().getString(R.string.connecting_to_server));

        new TimeLine(phone_md5, token, 1, 20, new TimeLine.SuccessCallback() {

            @Override
            public void onSuccess(int page, int perpage, List<Message> timeline) {
                pd.dismiss();

                adapter.clear();
                adapter.addAll(timeline);
            }
        }, new TimeLine.FailCallback() {

            @Override
            public void onFail(int errorCode) {
                pd.dismiss();

                if (errorCode==Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(AtyTimeline.this, AtyLogin.class));
                    finish();
                }else{
                    Toast.makeText(AtyTimeline.this, R.string.fail_to_load_timeline_data, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Message msg = adapter.getItem(position);
        Intent i = new Intent(this, AtyMessage.class);
        i.putExtra(Config.KEY_MSG, msg.getMsg());
        i.putExtra(Config.KEY_MSG_ID, msg.getMsgId());
        i.putExtra(Config.KEY_PHONE_MD5, msg.getPhone_md5());
        i.putExtra(Config.KEY_TOKEN, token);
        startActivity(i);
    }
}
