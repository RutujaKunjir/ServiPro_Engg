package com.example.servipro_engg.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.servipro_engg.List.ProductAddList;
import com.example.servipro_engg.R;

import java.util.List;

public class ProductAddAdapter extends RecyclerView.Adapter<ProductAddAdapter.ProductAddHolder>
{
    private List<ProductAddList> productAddLists;
    private Context context;

    public ProductAddAdapter(List<ProductAddList> productAddLists, Context context) {
        this.productAddLists = productAddLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAddAdapter.ProductAddHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(
                R.layout.add_product_list,
                viewGroup,
                false
        );
        return new ProductAddAdapter.ProductAddHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductAddAdapter.ProductAddHolder productAddHolder, final int position) {
        final ProductAddList productList = productAddLists.get(position);

        productAddHolder.product_name.setText(productList.getProductName());
        productAddHolder.product_price.setText(productList.getProductPrice());
        productAddHolder.product_Quanty.setText(productList.getProductQty());
        productAddHolder.product_totalAmt.setText(productList.getTotalAmount());

    }

    @Override
    public int getItemCount() {
        return productAddLists.size();
    }

    class ProductAddHolder extends RecyclerView.ViewHolder
    {
        TextView product_name,product_price,product_Quanty,product_totalAmt;

        public ProductAddHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_Quanty = itemView.findViewById(R.id.product_Quanty);
            product_totalAmt = itemView.findViewById(R.id.product_totalAmt);

        }
    }

}