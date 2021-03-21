package com.example.servipro_engg.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.servipro_engg.Activity.CustAmcDetails;
import com.example.servipro_engg.List.TaskList;
import com.example.servipro_engg.R;
import com.example.servipro_engg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MultipleAdapter extends RecyclerView.Adapter<MultipleAdapter.TaskHolder> implements Filterable
{
    private Context mCtx;
    private List<TaskList> vendorLists;
    private List<TaskList> saleListsFull;

    public MultipleAdapter(Context mCtx, List<TaskList> vendorLists) {
        this.mCtx = mCtx;
        this.vendorLists = vendorLists;
        saleListsFull = new ArrayList<>(vendorLists);
    }

    @NonNull
    @Override
    public MultipleAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_new_task3, null);
        return new MultipleAdapter.TaskHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final MultipleAdapter.TaskHolder holder, final int position) {
        final TaskList vendorList = vendorLists.get(position);

        holder.name_txt.setText(vendorList.getCust_Name());
        holder.phone_txt.setText(vendorList.getCust_Mobile1());
        holder.address_txt.setText(vendorList.getCust_Address());
        holder.email_txt.setText(vendorList.getService_Date());
        holder.dateTime_txt.setText(vendorList.getCreated_Date());
        holder.service_call_no_txt.setText(vendorList.getCall_No());
        String status_of_work = vendorList.getStatus_id();
        holder.status_txt.setText(status_of_work);

        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(mCtx,"attended",Toast.LENGTH_SHORT).show();
                Intent updateIntent=  new Intent (mCtx, CustAmcDetails.class);
                updateIntent.putExtra(Constants.TASK_DATA,vendorList);
                updateIntent.putExtra("ADDAMC",""+vendorLists.size());
                updateIntent.putExtra("AMCPOSITION",""+position);
                mCtx.startActivity(updateIntent);
                ((Activity)mCtx).finish();
                Log.v("Adapter ADDAMC = ",""+vendorLists.size());
                Log.v("Adapter AMCPOSITION = ",""+position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vendorLists.size();
    }

    class TaskHolder extends RecyclerView.ViewHolder
    {
        private TextView name_txt,email_txt,address_txt,service_call_no_txt, phone_txt,status_txt,dateTime_txt;
        private RelativeLayout optionBtn;
        private CardView root_view;

        public TaskHolder(@NonNull View itemView)
        {
            super(itemView);

            name_txt = itemView.findViewById(R.id.name_txt);
            email_txt = itemView.findViewById(R.id.email_txt);
            dateTime_txt = itemView.findViewById(R.id.dateTime_txt);
            address_txt = itemView.findViewById(R.id.address_txt);
            phone_txt = itemView.findViewById(R.id.phone_txt);
            optionBtn = itemView.findViewById(R.id.option_btn);
            service_call_no_txt = itemView.findViewById(R.id.service_call_no_txt);
            status_txt = itemView.findViewById(R.id.status_txt);
            root_view = itemView.findViewById(R.id.root_view);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TaskList> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(saleListsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (TaskList item : saleListsFull) {
                    if (item.getCust_Name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            vendorLists.clear();
            vendorLists.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
