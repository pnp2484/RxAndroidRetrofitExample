package com.pritesh.rxandroidretrofitexample.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pritesh.rxandroidretrofitexample.R;
import com.pritesh.rxandroidretrofitexample.models.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context context;
    private List<User> mUserList;

    public UserAdapter(Context context, List<User> mUserList) {
        this.context = context;
        this.mUserList = mUserList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = mUserList.get(position);

        // Displaying user full name
        holder.txtUserName.setText(user.getName());

        // Changing User Name color to random color
        holder.txtUserName.setTextColor(getRandomMaterialColor("400"));

        // Displaying user email
        holder.txtEmail.setText(user.getEmail());

        // Displaying user phone number
        holder.txtPhoneNumber.setText(user.getPhone());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    /**
     * Chooses random color defined in res/array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtUserName)
        TextView txtUserName;

        @BindView(R.id.txtEmail)
        TextView txtEmail;

        @BindView(R.id.txtPhoneNumber)
        TextView txtPhoneNumber;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
