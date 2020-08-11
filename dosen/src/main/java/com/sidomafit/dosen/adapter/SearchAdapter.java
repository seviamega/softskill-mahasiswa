package com.sidomafit.dosen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidomafit.dosen.model.SearchModel;
import com.sidomafit.dosen.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final Context mContext;
    private List<SearchModel> list = new ArrayList<>();
    private onClickDaftarKesalahan onClickDaftarKesalahan;

    public void setOnClickDaftarKesalahan(SearchAdapter.onClickDaftarKesalahan onClickDaftarKesalahan) {
        this.onClickDaftarKesalahan = onClickDaftarKesalahan;
    }

    public SearchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<SearchModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        SearchModel searchModel = list.get(position);
        holder.tvTitle.setText(searchModel.getKesalahan());
        holder.itemView.setOnClickListener(v -> onClickDaftarKesalahan.getItemKesalahan(list.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_search);
        }
    }

    public interface onClickDaftarKesalahan{
        void getItemKesalahan(SearchModel posotion);
    }
}