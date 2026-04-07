package com.example.myfirstapp.MemeAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myfirstapp.MemeModel;
import com.example.myfirstapp.R;

import java.util.ArrayList;

public class MemeAdapter extends RecyclerView.Adapter<MemeAdapter.ViewHolder> {

    Context context;
    ArrayList<MemeModel> list;
    OnItemClickListener listener;
    int lastPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(MemeModel model, int position);
    }

    public MemeAdapter(Context context, ArrayList<MemeModel> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MemeModel model = list.get(position);

        if (model.getImageUri() != null) {
            Glide.with(context).load(model.getImageUri()).centerCrop().into(holder.imageView);
        } else {
            Glide.with(context).load(model.getImageRes()).centerCrop().into(holder.imageView);
        }

        holder.txtTop.setText(model.getName());

        // Slide up animation
        if (position > lastPosition) {
            @SuppressLint("ResourceType") Animation animation = AnimationUtils.loadAnimation(context, R.anim.itemslideup);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

        // Click press animation
        holder.itemView.setOnClickListener(v -> {
            @SuppressLint("ResourceType") Animation press = AnimationUtils.loadAnimation(context, R.anim.btnpress);
            v.startAnimation(press);
            press.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation a) {}
                @Override public void onAnimationRepeat(Animation a) {}
                @Override public void onAnimationEnd(Animation a) {
                    listener.onItemClick(model, holder.getAdapterPosition());
                }
            });
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtTop;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgTemplate);
            txtTop = itemView.findViewById(R.id.txtTop);
        }
    }
}