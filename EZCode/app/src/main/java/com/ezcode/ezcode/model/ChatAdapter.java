package com.ezcode.ezcode.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcode.ezcode.MainActivity;
import com.ezcode.ezcode.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatAdapter extends ArrayAdapter<Chat> {
    private Context context;
    int resource;
    ArrayList<Chat> arrChat;


    public ChatAdapter(Context context, int resource,ArrayList<Chat> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrChat = objects;
    }


    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
       if(convertView==null){
           LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(resource,null);

           viewHolder.avatar = convertView.findViewById(R.id.chat_avatar);
           viewHolder.username = convertView.findViewById(R.id.chat_username);
           viewHolder.content = convertView.findViewById(R.id.chat_content);
           convertView.setTag(viewHolder);
       }else {
            viewHolder = (ViewHolder)convertView.getTag();
       }
        Chat chat= arrChat.get(position);

        Picasso.with(context).load(chat.getUserAvatar()).into(viewHolder.avatar);

        viewHolder.username.setText(chat.getUserName());
        viewHolder.content.setText(chat.getChatContent());
       return convertView;

    }
    private class ViewHolder{
        CircleImageView avatar;
        TextView username;
        TextView content;
    }
}
