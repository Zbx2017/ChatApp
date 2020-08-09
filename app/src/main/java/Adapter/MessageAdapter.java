package Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chattest1.MessageActivity;
import com.example.chattest1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.util.List;

import Model.Chat;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_AUDIO_LEFT = 2;
    public static final int MSG_AUDIO_RIGHT = 3;
    private Context mcontext;
    private List<Chat> mChat;
    private String imageurl;
    private MediaPlayer player;
    public static final String LOG_TAG = "play_audio";
    private String fileName = null;
    private StorageReference reference;
    private String local = null;

    FirebaseUser fuser;

    public MessageAdapter(Context mcontext, List<Chat> mChat, String imageurl) {
        this.mcontext = mcontext;
        this.mChat = mChat;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else if(viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else if (viewType == MSG_AUDIO_RIGHT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_audio_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_audio_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        final Chat chat = mChat.get(position);
        if (!chat.isIsaudio()) {
            holder.show_message.setText(chat.getMessage());
        }else{
            holder.show_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // right
                    if(chat.getSender().equals(fuser.getUid())) {
                        fileName = chat.getMessage();
                        local = Environment.getExternalStorageDirectory().getAbsolutePath();
                        local += "/Download/" + fileName;
                        startPlaying();
                    } else {
                        fileName = chat.getMessage();
                        System.out.println(fileName);
                        reference = FirebaseStorage.getInstance().getReference().child("Audio").child(fileName);
                        local =Environment.getExternalStorageDirectory().getAbsolutePath();
                        local += "/Download/"+fileName;
                        reference.getFile(new File(local)).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                if(taskSnapshot!=null){
                                   startPlaying();
                                }
                            }
                        });
                    }
                }
            });
        }


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

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(local);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });
//            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
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
        if(mChat.get(position).getSender().equals(fuser.getUid()) && !mChat.get(position).isIsaudio()){
            return MSG_TYPE_RIGHT;
        }else if(!mChat.get(position).getSender().equals(fuser.getUid()) && !mChat.get(position).isIsaudio()){
            return MSG_TYPE_LEFT;
        }else if(mChat.get(position).getSender().equals(fuser.getUid()) && mChat.get(position).isIsaudio()){
            return MSG_AUDIO_RIGHT;
        }else {
            return MSG_AUDIO_LEFT;
        }
    }


}

