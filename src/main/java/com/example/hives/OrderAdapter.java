package com.example.hives;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderDetails> orders;

    public OrderAdapter(List<OrderDetails> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewcart, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderDetails order = orders.get(position);
        holder.tvUserName.setText(order.getUserName());
        holder.tvUserPhone.setText(order.getUserPhone());
        holder.tvAddress.setText(order.getAddress());
        holder.tvTotalAmount.setText(String.valueOf(order.getTotalAmount()));

        // Display order items
        StringBuilder items = new StringBuilder();
        for (OrderItem item : order.getOrderItems()) {
            items.append(item.getName())
                    .append(" - ").append(item.getQuantity())
                    .append(" x ").append(item.getPrice())
                    .append("\n");
        }
        holder.tvOrderItems.setText(items.toString());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserPhone, tvAddress, tvTotalAmount, tvOrderItems;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvOrderItems = itemView.findViewById(R.id.tvOrderItems);
        }
    }
}

