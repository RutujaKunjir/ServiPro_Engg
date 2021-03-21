package com.example.servipro_engg.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.servipro_engg.Activity.ServiceDetails;
import com.example.servipro_engg.List.ProductAddList;
import com.example.servipro_engg.List.ServiceList;
import com.example.servipro_engg.List.TaskList;
import com.example.servipro_engg.R;
import com.example.servipro_engg.utils.Constants;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceHolder>
{
    private List<ServiceList> productAddLists;
    private Context context;
    ColorStateList colorStateList;
    private TaskList mData;
    private String AMcADD,AMCPOSITION;

    public ServiceAdapter(List<ServiceList> productAddLists, Context context) {
        this.productAddLists = productAddLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceAdapter.ServiceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Intent intent = ((Activity) context).getIntent();
        mData = intent.getParcelableExtra(Constants.TASK_DATA);
        AMcADD = intent.getStringExtra("ADDAMC");
        AMCPOSITION = intent.getStringExtra("AMCPOSITION");

        View itemView = LayoutInflater.from(context).inflate(
                R.layout.service_layout,
                viewGroup,
                false
        );
        return new ServiceAdapter.ServiceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceAdapter.ServiceHolder productAddHolder, final int position) {
        final ServiceList productList = productAddLists.get(position);

        productAddHolder.serveNo.setText(productList.getServeNo());
        productAddHolder.service_call_no.setText(productList.getService_call_no());
        productAddHolder.cust_id.setText(productList.getCust_id());
        productAddHolder.serveDate.setText(productList.getServeDate());
        productAddHolder.spareCheck.setChecked(productList.isSelected());

        if (productAddHolder.spareCheck.isChecked()){
            colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked}, // unchecked
                            new int[]{android.R.attr.state_checked} , // checked
                    },
                    new int[]{
                            Color.parseColor("#868686"),
                            Color.parseColor("#ffa900"),
                    }
            );

           // oneTime.setTextColor(getResources().getColor(R.color.dark_gray));
            CompoundButtonCompat.setButtonTintList(productAddHolder.spareCheck,colorStateList);

            productAddHolder.serveDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(mCtx,"attended",Toast.LENGTH_SHORT).show();
                    Intent updateIntent=  new Intent (context, ServiceDetails.class);
                    updateIntent.putExtra(Constants.TASK_DATA,mData);// AMcADD,AMCPOSITION
                    updateIntent.putExtra("ADDAMC",""+AMcADD);
                    updateIntent.putExtra("AMCPOSITION",""+AMCPOSITION);
                    updateIntent.putExtra("Cust_id",""+productAddHolder.cust_id.getText().toString());
                    updateIntent.putExtra("Service_call_no",""+productAddHolder.service_call_no.getText().toString());
                    context.startActivity(updateIntent);

                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return productAddLists.size();
    }

    class ServiceHolder extends RecyclerView.ViewHolder
    {
        TextView serveNo,serveDate,service_call_no,cust_id;
        CheckBox spareCheck;

        public ServiceHolder(@NonNull View itemView) {
            super(itemView);

            serveNo = itemView.findViewById(R.id.serveNo);
            cust_id = itemView.findViewById(R.id.cust_id);
            service_call_no = itemView.findViewById(R.id.service_call_no);
            serveDate = itemView.findViewById(R.id.serveDate);
            spareCheck = itemView.findViewById(R.id.spareChechbox);

        }
    }

}