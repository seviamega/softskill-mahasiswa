package com.sidomafit.dosen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidomafit.dosen.model.Kesalahan;
import com.sidomafit.dosen.R;

import java.util.ArrayList;
import java.util.List;

public class KesalahanAdapter extends RecyclerView.Adapter<KesalahanAdapter.ViewHolder> {

    private final Context mContext;
    private List<Kesalahan> list = new ArrayList<>();
    private TextView tvStatus;
    private onClickKesalahan clickKesalahan;

    public void setClickKesalahan(onClickKesalahan clickKesalahan) {
        this.clickKesalahan = clickKesalahan;
    }

    public KesalahanAdapter(Context mContext, List<Kesalahan> list, TextView tvStatus) {
        this.mContext = mContext;
        this.list = list;
        this.tvStatus = tvStatus;
    }

    public void setList(List<Kesalahan> list) {
        this.list = list;
        if (list == null || list.size() == 0){
            tvStatus.setVisibility(View.VISIBLE);
        }else {
            tvStatus.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    public KesalahanAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_daftar_kesalahan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Kesalahan kesalahan = list.get(position);
        holder.tvPoin.setText(kesalahan.getPoin_kesalahan());
        holder.tvKesalahan.setText(kesalahan.getDeskripsi_kesalahan());
        holder.tvPengirim.setText(mContext.getString(R.string.dikirim) +" "+ kesalahan.getNama_pengirim());
        holder.tvWaktu.setText(kesalahan.getWaktu_pengiriman());

        holder.itemView.setOnClickListener(v -> clickKesalahan.clickKesalahan(list.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvPoin;
        final TextView tvKesalahan;
        final TextView tvPengirim;
        final TextView tvWaktu;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPoin = itemView.findViewById(R.id.tv_poin_list_kesalahan);
            tvKesalahan = itemView.findViewById(R.id.tv_desk_list_kesalahan);
            tvPengirim = itemView.findViewById(R.id.tv_pengirim_list_kesalahan);
            tvWaktu = itemView.findViewById(R.id.tv_waktu_list_kesalahan);
        }
    }

    public interface onClickKesalahan{
        void clickKesalahan(Kesalahan kesalahan);
    }
}
