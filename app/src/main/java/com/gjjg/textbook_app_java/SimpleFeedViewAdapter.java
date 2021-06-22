package com.gjjg.textbook_app_java;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SimpleFeedViewAdapter extends RecyclerView.Adapter<SimpleFeedViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> data;

    public SimpleFeedViewAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_ascii_simple_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (data.size() > 0) {
            holder.simpleFeedAsciiTextView.setText(data.get(position));
        }

        holder.simpleFeedAsciiTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ASCII Art", holder.simpleFeedAsciiTextView.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(context, "ASCII Art copied!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView simpleFeedAsciiTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            simpleFeedAsciiTextView = itemView.findViewById(R.id.simpleFeedAsciiTextView);
        }
    }
}
