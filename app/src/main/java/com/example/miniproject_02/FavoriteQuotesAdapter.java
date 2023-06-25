package com.example.miniproject_02;

import android.content.Context;
import android.text.style.DynamicDrawableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject_02.Models.Quote;

import java.util.ArrayList;

public class FavoriteQuotesAdapter extends RecyclerView.Adapter<FavoriteQuotesAdapter.FavoriteQuotesViewHolder> {

    Context context;
    ArrayList<Quote> quotesList;

    public FavoriteQuotesAdapter(Context context , ArrayList<Quote> quotesList) {
        this.context = context;
        this.quotesList = quotesList;
    }

    @NonNull
    @Override
    public FavoriteQuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_quote , parent , false);
        return new FavoriteQuotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteQuotesViewHolder holder, int position) {
        Quote quote = quotesList.get(position);

        holder.favQuote.setText(quote.quoteDesign(context , R.drawable.ic_quote , DynamicDrawableSpan.ALIGN_CENTER));
        holder.favAuthor.setText(quote.authorDesign());

    }

    @Override
    public int getItemCount() {
        return quotesList.size();
    }

    public class FavoriteQuotesViewHolder extends RecyclerView.ViewHolder{
        TextView favQuote , favAuthor;
        public FavoriteQuotesViewHolder(@NonNull View itemView) {
            super(itemView);
            favQuote = itemView.findViewById(R.id.quote_item_tv);
            favAuthor = itemView.findViewById(R.id.author_item_tv);
        }
    }
}
