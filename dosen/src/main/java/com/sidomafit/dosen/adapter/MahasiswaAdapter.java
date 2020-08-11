package com.sidomafit.dosen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidomafit.dosen.model.Mahasiswa;
import com.sidomafit.dosen.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    private onClickMahasiswa clickMahasiswa;

    public void setClickMahasiswa(onClickMahasiswa clickMahasiswa) {
        this.clickMahasiswa = clickMahasiswa;
    }

    private final Context mContext;
    private List<Mahasiswa> list = new ArrayList<>();
    private TextView tvStatus;

    public MahasiswaAdapter(Context mContext, List<Mahasiswa> list, TextView tvStatus) {
        this.mContext = mContext;
        this.list = list;
        this.tvStatus = tvStatus;
    }

    public void setList(List<Mahasiswa> list) {
        this.list = list;
        if (list == null || list.size() == 0){
            tvStatus.setVisibility(View.VISIBLE);
        }else {
            tvStatus.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    public MahasiswaAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_mahasiswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Mahasiswa dataMahasiswa = list.get(position);
        holder.tvNama.setText(dataMahasiswa.getNama_mhs());
        holder.tvNim.setText(dataMahasiswa.getNim());
        if (dataMahasiswa.getTotal_poin() == 0){
            holder.tvPoin.setText(String.valueOf(0));
        }else {
            holder.tvPoin.setText(String.valueOf(dataMahasiswa.getTotal_poin()));
        }if (dataMahasiswa.getFoto_mhs().equals("default")){
            holder.imgMhs.setImageResource(R.drawable.img_profile);
        }else {
            Picasso.get().load(dataMahasiswa.getFoto_mhs()).into(holder.imgMhs);
        }

        holder.itemView.setOnClickListener(v -> clickMahasiswa.clickMahasiswa(list.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNama;
        final TextView tvNim;
        final TextView tvPoin;
        final ImageView imgMhs;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tv_nama_list_mhs);
            tvNim = itemView.findViewById(R.id.tv_nim_list_mhs);
            imgMhs = itemView.findViewById(R.id.img_list_mhs);
            tvPoin = itemView.findViewById(R.id.tv_poin_list_mhs);
        }
    }

    public interface onClickMahasiswa{
        void clickMahasiswa(Mahasiswa mahasiswa);
    }
}
