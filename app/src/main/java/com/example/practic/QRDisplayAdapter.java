package com.example.practic;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QRDisplayAdapter extends
        RecyclerView.Adapter<QRDisplayAdapter.ViewHolder>{

    private List<QRCodeDisplay> QRDisplayList;
    public QRDisplayAdapter(List<QRCodeDisplay> QRCodes){QRDisplayList = QRCodes;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgQR;
        public TextView tvTitle, tvTime, tvPlace, tvDate, tvWorkPlace, tvLocker;

        public ViewHolder(View itemView) {
            super(itemView);
            imgQR       = itemView.findViewById(R.id.imageView_QR_Image);
            tvTitle     = itemView.findViewById(R.id.textView_QR_Name);
            tvTime      = itemView.findViewById(R.id.textView_QR_Time);
            tvDate      = itemView.findViewById(R.id.textView_QR_Date);
            tvPlace     = itemView.findViewById(R.id.textView_QR_Place);
            tvWorkPlace = itemView.findViewById(R.id.textView_QR_Workplace);
            tvLocker    = itemView.findViewById(R.id.textView_QR_Locker);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View thisView = inflater.inflate(R.layout.recyclerview_qr_item, parent, false);
        // Return a new holder instance
        return new ViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QRCodeDisplay QR = QRDisplayList.get(position);

        ImageView imgQR = holder.imgQR;
        TextView title  = holder.tvTitle;
        TextView time   = holder.tvTime;
        TextView date   = holder.tvDate;
        TextView place  = holder.tvPlace;
        TextView workPlace = holder.tvWorkPlace;
        TextView locker = holder.tvLocker;

        imgQR.setImageBitmap(QR.getImg());
        title.setText(QR.getTitle());
        time.setText(QR.getTime());
        date.setText(QR.getDate());

        if (QR.getPlace() == null) place.setVisibility(View.GONE);
        else place.setText(QR.getPlace());

        if (QR.getWorkplace() == null) workPlace.setVisibility(View.GONE);
        else workPlace.setText(QR.getWorkplace());

        if (QR.getLocker() == null) locker.setVisibility(View.GONE);
        else locker.setText(QR.getLocker());

        holder.imgQR.setOnClickListener(view -> {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(view.getContext());
            LayoutInflater factory = LayoutInflater.from(view.getContext());
            final View dialogView = factory.inflate(R.layout.qr_enlarge_dialog, null);
            ImageView QRimg = dialogView.findViewById(R.id.imageview_dialog);
            QRimg.setImageBitmap(QR.getImg());
            alertadd.setView(dialogView);
            alertadd.show();
        });
    }

    @Override
    public int getItemCount() {
        return QRDisplayList.size();
    }
}
