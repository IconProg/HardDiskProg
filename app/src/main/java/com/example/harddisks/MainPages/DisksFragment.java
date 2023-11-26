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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.harddisks.MainPages.HelpFunc.DiskAdapter;
import com.example.harddisks.MainPages.HelpFunc.DiskDataClass;
import com.example.harddisks.MainPages.HelpFunc.DiskDatabaseHelper;
import com.example.harddisks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class DisksFragment extends Fragment {

    private List<DiskDataClass> diskList;
    ImageSwitcher favorite_switcher;
    private ListView diskListView;
    private DiskAdapter adapter;

    private boolean isRedHeart = false;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference disksRef = database.getReference("disks_data");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    StorageReference imagesRef = storageRef.child("images/image.jpg");

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

        favorite_switcher = (ImageSwitcher) view.findViewById(R.id.imageSwitcherHeart);
        ImageView author = (ImageView) view.findViewById(R.id.user_logo);
        ImageView about_prog = (ImageView) view.findViewById(R.id.about_prog_logo);
        ImageView instruction = (ImageView) view.findViewById(R.id.instruction_logo);
        ImageView favorite = (ImageView) view.findViewById(R.id.favorite_logo);
        diskListView = (ListView) view.findViewById(R.id.disksList);

        author.setOnClickListener(view1 -> navController.navigate(R.id.action_disksFragment_to_authorFragment));
        about_prog.setOnClickListener(view1 -> navController.navigate(R.id.action_disksFragment_to_programInfoFragment));
        instruction.setOnClickListener(view1 -> navController.navigate(R.id.action_disksFragment_to_instructionManualFragment));
        favorite.setOnClickListener(view1 -> navController.navigate(R.id.action_disksFragment_to_favoriteFragment));

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

        DiskDataClass wdBlueDisk = new DiskDataClass("https://firebasestorage.googleapis.com/v0/b/harddisks-3f306.appspot.com/o/WDBLUE.png?alt=media&token=7eb1e999-2b0d-4680-9d34-0cebc394f259",
                "WD Blue", "131123", 1000, 6, 7200, 64, true);
        DiskDataClass toshibaDisk = new DiskDataClass("https://firebasestorage.googleapis.com/v0/b/harddisks-3f306.appspot.com/o/Toshiba.png?alt=media&token=5caf76a3-8f05-4e62-8111-24c96a7c4b54",
                "Toshiba P300", "HDWD110UZSVA", 1000, 6, 7200, 64, false);

        if (dbHelper.isManufacturerCodeUnique(wdBlueDisk.manufacturerCode)) {
            disksRef.child(wdBlueDisk.manufacturerCode).setValue(wdBlueDisk);
        }

        if (dbHelper.isManufacturerCodeUnique(toshibaDisk.manufacturerCode)) {
            disksRef.child(toshibaDisk.manufacturerCode).setValue(toshibaDisk);
        }

        diskList.clear();
        diskList.addAll(dbHelper.getAllDisks());

        adapter = new DiskAdapter(requireContext(), R.layout.list_item_disk, diskList);
        diskListView.setAdapter(adapter);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}