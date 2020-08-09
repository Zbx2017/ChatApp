package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chattest1.MessageActivity;
import com.example.chattest1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import Model.Chat;
import Model.User;

public class AudioMessageAdapter extends RecyclerView.Adapter<AudioMessageAdapter.ViewHolder> {

    public static final int MSG_AUDIO_LEFT = 0;
    public static final int MSG_AUDIO_RIGHT = 1;
    private Context mcontext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public AudioMessageAdapter(Context mcontext, List<Chat> mChat, String imageurl) {
        this.mcontext = mcontext;
        this.mChat = mChat;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public AudioMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_AUDIO_RIGHT){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_audio_right, parent, false);
            return new AudioMessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_audio_left, parent, false);
            return new AudioMessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AudioMessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText("Audio Message");
        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        }else {
            Glide.with(mcontext).load(imageurl).into(holder.profile_image);
        }

        if(position == mChat.size()-1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            }else{
                holder.txt_seen.setText("Delivered");
            }

        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;


        public ViewHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);

        }
    }

    @Override
    public int getItemViewType(int position){
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_AUDIO_RIGHT;
        }else {
            return MSG_AUDIO_LEFT;
        }
    }

}

