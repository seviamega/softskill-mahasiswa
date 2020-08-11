package com.sidomafit.dosen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidomafit.dosen.model.Kelas;
import com.sidomafit.dosen.R;

import java.util.ArrayList;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<Kelas> mList;
    private OnclickItemAdapter onclickItemAdapter;

    public void setmList(ArrayList<Kelas> mList) {
        this.mList = mList;
    }

    public void setOnclickItemAdapter(OnclickItemAdapter onclickItemAdapter) {
        this.onclickItemAdapter = onclickItemAdapter;
    }

    public KelasAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_kelas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Kelas kelas = mList.get(position);
        holder.tvKelas.setText(kelas.getKelas());

        holder.itemView.setOnClickListener(v -> onclickItemAdapter.onClickItem(mList.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvKelas;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKelas = itemView.findViewById(R.id.tv_list_kelas);
        }
    }

    public interface OnclickItemAdapter{
        void onClickItem(Kelas kelas);
    }
}
