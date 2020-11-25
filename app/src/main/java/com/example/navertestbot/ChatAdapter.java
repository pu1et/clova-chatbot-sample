package com.example.navertestbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatItem> mChatList = null;

    public ChatAdapter(ArrayList<ChatItem> chatList){ this.mChatList = chatList; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == Constants.LEFT_MSG){
            view = inflater.inflate(R.layout.left_msg, parent, false);
            return new LeftViewHolder(view);
        }else{
            view = inflater.inflate(R.layout.right_msg, parent, false);
            return new RightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof LeftViewHolder){
            ((LeftViewHolder)viewHolder).left_chat_name.setText(mChatList.get(position).getName());
            ((LeftViewHolder)viewHolder).left_chat_msg.setText(mChatList.get(position).getMsg());
            ((LeftViewHolder)viewHolder).left_chat_time.setText(mChatList.get(position).getTime());
            ((LeftViewHolder)viewHolder).left_chat_time.requestFocus(View.FOCUS_DOWN);
        }else{
            ((RightViewHolder)viewHolder).right_chat_msg.setText(mChatList.get(position).getMsg());
            ((RightViewHolder)viewHolder).right_chat_time.setText(mChatList.get(position).getTime());
            ((RightViewHolder)viewHolder).right_chat_time.requestFocus(View.FOCUS_DOWN);
        }
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }


    @Override
    public int getItemViewType(int position){ return mChatList.get(position).getViewType(); }

    public class LeftViewHolder extends RecyclerView.ViewHolder{
        CircleImageView left_chat_icon;
        TextView left_chat_name;
        TextView left_chat_msg;
        TextView left_chat_time;


        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);

            left_chat_icon = itemView.findViewById(R.id.left_chat_icon);
            left_chat_name = itemView.findViewById(R.id.left_chat_name);
            left_chat_msg = itemView.findViewById(R.id.left_chat_msg);
            left_chat_time = itemView.findViewById(R.id.left_chat_time);

        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{

        TextView right_chat_msg;
        TextView right_chat_time;


        public RightViewHolder(@NonNull View itemView) {
            super(itemView);

            right_chat_msg = (TextView) itemView.findViewById(R.id.right_chat_msg);
            right_chat_time = (TextView) itemView.findViewById(R.id.right_chat_time);

        }
    }
}
