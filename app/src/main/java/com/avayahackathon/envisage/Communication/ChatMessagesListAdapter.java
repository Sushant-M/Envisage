package com.avayahackathon.envisage.Communication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.avaya.clientservices.messaging.Message;
import com.avayahackathon.envisage.R;

import java.util.List;

/**
 * Created by yogita on 6/5/17.
 */

public class ChatMessagesListAdapter extends ArrayAdapter<Message> {
    private final List<Message> items;
    private final int layoutResourceId;


    public ChatMessagesListAdapter(Activity activity, int layoutResourceId, List<Message> items) {
        super(activity, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.items = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ChatHolder holder;
        if(view == null) {
            //initialise the holder item once for better performance.
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);
            holder = new ChatHolder();
            fillHolder(holder, position, view);
        }
        else
        {
            holder = (ChatHolder) view.getTag();
            if(holder == null)
            {
                holder = new ChatHolder();
                fillHolder(holder, position, view);
            }
        }
        String temp;
        temp = holder.item.getBody();
        holder.messageText.setText(temp);
        return view;
    }

    public static class ChatHolder {
        Message item;
        TextView messageText;
    }

    private void fillHolder(ChatHolder holder, int position, View view)
    {
        holder.item = items.get(position);
        TextView messageTextTmp;
        if (!holder.item.isFromMe()) {
           // holder.messageText = (TextView) view.findViewById(R.id.recived_message);
           // messageTextTmp = (TextView) view.findViewById(R.id.sent_message);
          //  messageTextTmp.setVisibility(View.INVISIBLE);
        } else {
            //holder.messageText = (TextView) view.findViewById(R.id.sent_message);
           // messageTextTmp = (TextView) view.findViewById(R.id.recived_message);

        }
      //  messageTextTmp.setVisibility(View.INVISIBLE);
    }
}
