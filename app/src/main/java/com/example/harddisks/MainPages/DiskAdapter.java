package com.example.harddisks.MainPages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harddisks.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DiskAdapter extends ArrayAdapter<DiskDataClass> {
    private boolean isRedHeart = false;

    private ImageSwitcher imageSwitcher;

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

        ImageSwitcher imageSwitcher = view.findViewById(R.id.imageSwitcherHeart);
        ImageView imageViewDisk = view.findViewById(R.id.imageViewDisk);
        TextView mainTextDisk = view.findViewById(R.id.mainTextDisk);
        TextView textSecond = view.findViewById(R.id.textSecond);
        TextView textViewTopNumber = view.findViewById(R.id.textViewTopNumber);

        if(imageSwitcher != null) {
            imageSwitcher.setOnClickListener(view2 -> {
                isRedHeart = !isRedHeart;
                if (isRedHeart) {
                    imageSwitcher.setImageResource(R.drawable.red_big_heart);
                } else {
                    imageSwitcher.setImageResource(R.drawable.big_heart);
                }
            });
        }

        if (disk != null) {

            Picasso.get().load(disk.getImageUrl()).into(imageViewDisk);

            if(disk.hasRaid()){
            mainTextDisk.setText(disk.getCapacity() + " ГБ Жесткий диск " + disk.getModel() + " [" + disk.getManufacturerCode() + "] " + disk.getSpeedInterface() + " Гбит/с, " + disk.getSpindleSpeed() + " об/мин, кэш память - " +
                    disk.getCacheSize() + ", RAID");
            } else {
                mainTextDisk.setText(disk.getCapacity() + "ГБ Жесткий диск " + disk.getModel() + " [" + disk.getManufacturerCode() + "] " + disk.getSpeedInterface() + " Гбит/с, " + disk.getSpindleSpeed() + " об/мин, кэш память - " +
                        disk.getCacheSize());
            }

            textSecond.setText("Мы добавим описание позже");



            // textViewTopNumber.setText("Top #" + (position + 1));
        }

        return view;
    }

}