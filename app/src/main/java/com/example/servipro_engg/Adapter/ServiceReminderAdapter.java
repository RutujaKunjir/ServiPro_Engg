package com.example.servipro_engg.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.servipro_engg.Activity.AddMachineAmc;
import com.example.servipro_engg.Activity.AddServiceReminder;
import com.example.servipro_engg.List.TaskList;
import com.example.servipro_engg.R;
import com.example.servipro_engg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ServiceReminderAdapter extends RecyclerView.Adapter<ServiceReminderAdapter.ServiceReminderHolder> implements Filterable
{
    private Context mCtx;
    private List<TaskList> vendorLists;
    private List<TaskList> saleListsFull;

    public ServiceReminderAdapter(Context mCtx, List<TaskList> vendorLists) {
        this.mCtx = mCtx;
        this.vendorLists = vendorLists;
        saleListsFull = new ArrayList<>(vendorLists);
    }

    @NonNull
    @Override
    public ServiceReminderAdapter.ServiceReminderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_new_task, null);
        return new ServiceReminderAdapter.ServiceReminderHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceReminderAdapter.ServiceReminderHolder holder, final int position) {
        final TaskList vendorList = vendorLists.get(position);

        holder.name_txt.setText(vendorList.getCust_Name());
        holder.phone_txt.setText(vendorList.getCust_Mobile1());
        holder.address_txt.setText(vendorList.getCust_Address());
        holder.email_txt.setText(vendorList.getCust_Email());
        holder.dateTime_txt.setText(vendorList.getCreated_Date());
        holder.service_call_no_txt.setText(vendorList.getCall_No());
        String status_of_work = vendorList.getStatus_id();
        holder.status_txt.setText(status_of_work);

        holder.serv_Date_txt.setText(vendorList.getService_Date());

        holder.optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent updateIntent = new Intent(mCtx, AddServiceReminder.class);
                updateIntent.putExtra(Constants.TASK_DATA,vendorList);
                mCtx.startActivity(updateIntent);
                ((Activity)mCtx).finish();
            }
        });

        holder.phone_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ vendorList.getCust_Mobile1()));
                mCtx.startActivity(intent);

            }
        });
        holder.email_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendMail();
                //String recipient = vendorList.getCust_email();
                //  Uri.fromParts("mailto", "abc@gmail.com", null));
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setData(Uri.parse("email:"+ vendorList.getCust_Email()));
                intent1.setType("message/rfc822");
                mCtx.startActivity(Intent.createChooser(intent1,"Choose an email client"));
            }
        });
        holder.address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent (Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("http://maps.google.co.in/maps?q=" + vendorList.getCust_Address() ));
                //  if (intent2.resolveActivity(holder.setText.vendorList.getCust_address()) != null) {
                // intent2.setData(Uri.parse("geo:0,0?q=37.423156,-122.084917 (" +  vendorList.getCust_address()")"));
                mCtx.startActivity(intent2);

                //}
            }
        });

    }

    @Override
    public int getItemCount() {
        return vendorLists.size();
    }

    class ServiceReminderHolder extends RecyclerView.ViewHolder
    {
        private TextView serv_Date_txt,name_txt,email_txt,address_txt,service_call_no_txt, phone_txt,status_txt,dateTime_txt;
        private RelativeLayout optionBtn;
        private CardView root_view;

        public ServiceReminderHolder(@NonNull View itemView)
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
            serv_Date_txt = itemView.findViewById(R.id.serv_Date_txt);
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
                    if (item.getCust_Name().toLowerCase().contains(filterPattern) ||
                    item.getCust_Mobile1().toLowerCase().contains(filterPattern))
                    {
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
