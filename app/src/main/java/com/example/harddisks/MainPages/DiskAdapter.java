package com.example.harddisks.MainPages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harddisks.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DiskAdapter extends ArrayAdapter<DiskDataClass> {

    private Context context;
    private int resource;

    public DiskAdapter(@NonNull Context context, int resource, @NonNull List<DiskDataClass> disks) {
        super(context, resource, disks);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }
        DiskDataClass disk = getItem(position);

        ImageView imageViewDisk = view.findViewById(R.id.imageViewDisk);
        TextView mainTextDisk = view.findViewById(R.id.mainTextDisk);
        TextView textSecond = view.findViewById(R.id.textSecond);
        ImageView imageViewHeart = view.findViewById(R.id.imageViewHeart);
        TextView textViewTopNumber = view.findViewById(R.id.textViewTopNumber);

        if (disk != null) {

            Picasso.get().load(disk.getImageUrl()).into(imageViewDisk);

            mainTextDisk.setText(disk.getModel());
            textSecond.setText("Код производителя: " + disk.getManufacturerCode());

            imageViewHeart.setOnClickListener(v -> {

            });


            textViewTopNumber.setText("Top #" + (position + 1));
        }

        return view;
    }
}