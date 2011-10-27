package com.danikula.androidkit.aibolit.ui;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.danikula.androidkit.aibolit.Aibolit;
import com.danikula.androidkit.aibolit.R;
import com.danikula.androidkit.aibolit.annotation.InjectOnClickListener;
import com.danikula.androidkit.aibolit.annotation.InjectOnTextChangedListener;
import com.danikula.androidkit.aibolit.annotation.InjectResource;
import com.danikula.androidkit.aibolit.annotation.InjectView;

public class AibolitChatActivity extends Activity {

    @InjectView(R.id.messageEditText)
    private EditText messageEditText;

    @InjectView(R.id.symbolsCountTextVew)
    private TextView symbolsCountTextVew;

    @InjectView(R.id.historyListView)
    private ListView historyListView;

    @InjectResource(R.string.symbols_count)
    private String symbolsCountPattern;

    private LogAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_activity);
        Aibolit.doInjections(this);

        adapter = new LogAdapter(this, android.R.layout.simple_list_item_2);
        historyListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(getString(R.string.menu_classic)).setIntent(new Intent(this, ClassicChatActivity.class));
        menu.add(getString(R.string.menu_test)).setIntent(new Intent(this, TestInjectActivity.class));
        return super.onCreateOptionsMenu(menu);
    }

    @InjectOnClickListener(R.id.sendButton)
    private void onSendButtonClick(View v) {
        String text = messageEditText.getText().toString();
        adapter.add(new Message(new Date(), text));
        messageEditText.getEditableText().clear();
    }

    @InjectOnClickListener(R.id.clearHistoryButton)
    private void onClearHistoryButtonClick(View v) {
        adapter.clear();
    }

    @InjectOnTextChangedListener(R.id.messageEditText)
    public void onMessageTextChanged(CharSequence s, int start, int before, int count) {
        String symbolsCountText = String.format(symbolsCountPattern, s.length());
        symbolsCountTextVew.setText(symbolsCountText);
    }

    private class LogAdapter extends MutableListAdapter<Message> {

        @InjectView(android.R.id.text1)
        private TextView messageTextView;

        @InjectView(android.R.id.text2)
        private TextView timeTextView;

        public LogAdapter(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        protected void bind(Message message, View view, int position) {
            Aibolit.doInjections(this, view);
            timeTextView.setText(message.getTime().toString());
            messageTextView.setText(message.getText());
        }
    }
}