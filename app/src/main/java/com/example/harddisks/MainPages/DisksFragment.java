package com.example.harddisks.MainPages;

import static android.content.ContentValues.TAG;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.harddisks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DisksFragment extends Fragment {

    private List<DiskDataClass> diskList;
    private ListView diskListView;
    private DiskAdapter adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference disksRef = database.getReference("disks_data");

    private DiskDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_disks, container, false);

        dbHelper = new DiskDatabaseHelper(requireContext());


        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        diskList = dbHelper.getAllDisks();
        getDisk();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        ImageView author = (ImageView) view.findViewById(R.id.user_logo);
        ImageView about_prog = (ImageView) view.findViewById(R.id.about_prog_logo);
        ImageView instruction = (ImageView) view.findViewById(R.id.instruction_logo);
        ImageView favorite = (ImageView) view.findViewById(R.id.favorite_logo);
        ImageView comparison = (ImageView) view.findViewById(R.id.comparison_logo);
        diskListView = (ListView) view.findViewById(R.id.disksList);

        author.setOnClickListener(view1 -> navController.navigate(R.id.action_disksFragment_to_authorFragment));
        about_prog.setOnClickListener(view1 -> navController.navigate(R.id.action_disksFragment_to_programInfoFragment));
        instruction.setOnClickListener(view1 -> navController.navigate(R.id.action_disksFragment_to_instructionManualFragment));
        favorite.setOnClickListener(view1 -> navController.navigate(R.id.action_disksFragment_to_favoriteFragment));
        comparison.setOnClickListener(view1 -> navController.navigate(R.id.action_disksFragment_to_comprasionFragment));


    }

    public void getDisk(){
        disksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DiskDataClass> disks = new ArrayList<>();

                for (DataSnapshot diskSnapshot : dataSnapshot.getChildren()) {
                    DiskDataClass diskDataClass = diskSnapshot.getValue(DiskDataClass.class);
                    if (diskDataClass != null) {
                        disks.add(diskDataClass);
                    }
                }

                dbHelper.loadDisksFromFirebase(disks);

                addDisk();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error reading data", error.toException());
            }
        });

    }

    public void addDisk(){

        DiskDataClass newDiskDataClass = new DiskDataClass("https://c.dns-shop.ru/thumb/st1/fit/200/200/0c62856a847ab02f71ec3c59ef4b9d63/531e1e880acf7fac191e35a5d616bb6b920d77fa31e8929909b1d7c5985e3b58.jpg",
                "WD Blue", "131123", 1000, 7200, 64, true);

        if (dbHelper.isManufacturerCodeUnique(newDiskDataClass.manufacturerCode)) {
            String key = disksRef.push().getKey();
            disksRef.child(key).setValue(newDiskDataClass);
        } else {

        }

        adapter = new DiskAdapter(requireContext(), R.layout.list_item_disk, diskList);
        diskListView.setAdapter(adapter);
    }
}