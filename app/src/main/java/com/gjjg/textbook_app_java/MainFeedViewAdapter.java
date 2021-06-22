package com.gjjg.textbook_app_java;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gjjg.textbook_app_java.models.Post;

import java.util.ArrayList;

public class MainFeedViewAdapter extends RecyclerView.Adapter<MainFeedViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> data;

    public MainFeedViewAdapter(Context context, ArrayList<Post> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_ascii_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (data.size() > 0) {
            holder.feedProfileTextView.setText(data.get(position).getSenderName());
            holder.feedAsciiTextView.setText(data.get(position).getImage());
        }

        holder.feedProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtil.getInstance().setCurrentUser(data.get(position).getSenderId());
                Intent myIntent = new Intent(context, ProfileActivity.class);
                context.startActivity(myIntent);
            }
        });

        holder.feedCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ASCII Art", holder.feedAsciiTextView.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(context, "ASCII Art copied!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView feedAsciiTextView;
        TextView feedProfileTextView;
        ImageButton feedCopyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            feedAsciiTextView = itemView.findViewById(R.id.feedAsciiTextView);
            feedProfileTextView = itemView.findViewById(R.id.feedProfileTextView);
            feedCopyButton = itemView.findViewById(R.id.feedCopyButton);
        }
    }
}
