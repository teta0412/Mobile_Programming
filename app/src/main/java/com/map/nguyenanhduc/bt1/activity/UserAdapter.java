package com.map.nguyenanhduc.bt1.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;


import com.map.nguyenanhduc.bt1.R;
import com.map.nguyenanhduc.bt1.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private OnUserClickListener listener;
    private Context context;

    public interface OnUserClickListener {
        void onEditClick(User user);
        void onDeleteClick(User user);
    }

    public UserAdapter(Context context, List<User> users, OnUserClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateUsers(List<User> newUsers) {
        this.users = newUsers;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        TextView phoneTextView;
        ImageButton menuButton;

        UserViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            menuButton = itemView.findViewById(R.id.menuButton);
        }

        void bind(final User user, final OnUserClickListener listener) {
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
            phoneTextView.setText(user.getTelephoneNum());

            menuButton.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, menuButton);
                popup.inflate(R.menu.user_item_menu);
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.menu_edit){
                        listener.onEditClick(user);
                            return true;
                    }else if (item.getItemId() == R.id.menu_delete) {
                        listener.onDeleteClick(user);
                        return true;
                    }
                    return true;
                });
                popup.show();
            });
        }
    }
}
