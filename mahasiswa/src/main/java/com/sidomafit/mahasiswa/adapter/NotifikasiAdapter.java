package com.sidomafit.mahasiswa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sidomafit.mahasiswa.model.Kesalahan;
import com.sidomafit.mahasiswa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<Kesalahan> list = new ArrayList<>();
    private TextView tvStatus;
    private onClickNotifikasi clickNotifikasi;

    public void setClickNotifikasi(onClickNotifikasi clickNotifikasi) {
        this.clickNotifikasi = clickNotifikasi;
    }

    public NotifikasiAdapter(Context mContext, ArrayList<Kesalahan> list, TextView tvStatus) {
        this.mContext = mContext;
        this.list = list;
        this.tvStatus = tvStatus;
    }

    public void setList(ArrayList<Kesalahan> list) {
        this.list = list;
        if (list == null || list.size() == 0){
            tvStatus.setVisibility(View.VISIBLE);
        }else {
            tvStatus.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    public NotifikasiAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_laporan_saya, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Kesalahan kesalahan = list.get(position);
        holder.tvDesk.setText(kesalahan.getNama_pengirim());
        holder.tvPesan.setText(kesalahan.getDeskripsi_kesalahan());
        holder.tvWaktu.setText(kesalahan.getWaktu_pengiriman());

        if (kesalahan.getFoto_mhs().equals("default")){
            holder.image.setImageResource(R.drawable.img_profile);
        }else {
            Picasso.get().load(kesalahan.getFoto_mhs()).into(holder.image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNotifikasi.clikNotifikasi(list.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvPesan;
        final TextView tvDesk;
        final TextView tvWaktu;
        final ImageView image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPesan = itemView.findViewById(R.id.tv_nama_list_laporan);
            tvDesk = itemView.findViewById(R.id.tv_desk_kesalahan_list_laporan);
            image = itemView.findViewById(R.id.img_list_laporan);
            tvWaktu = itemView.findViewById(R.id.tv_waktu_list_laporan);
        }
    }

    public interface onClickNotifikasi{
        void clikNotifikasi(Kesalahan kesalahan);
    }
}
