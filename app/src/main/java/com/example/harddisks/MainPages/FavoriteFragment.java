package com.example.harddisks.MainPages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {

    private List<DiskDataClass> favoriteDisks;
    private Handler handler = new Handler();

    private ImageSwitcher imageSwitcherHeart;

    private DiskAdapter adapter;

    private boolean isRedHeart = true;
    private DatabaseReference userFavoriteDisksRef;
    private DiskDatabaseHelper diskDatabaseHelper;

    private ListView disksList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        userFavoriteDisksRef = FirebaseDatabase.getInstance().getReference()
                .child("user_data")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favorite_disks");

        diskDatabaseHelper = new DiskDatabaseHelper(requireContext());

        favoriteDisks = new ArrayList<>();
        adapter = new DiskAdapter(requireContext(), R.layout.list_item_disk, favoriteDisks);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        disksList = view.findViewById(R.id.disksList);

        ImageView home = (ImageView) view.findViewById(R.id.main_menu_logo);
        ImageView author = (ImageView) view.findViewById(R.id.user_logo);
        ImageView about_prog = (ImageView) view.findViewById(R.id.about_prog_logo);
        ImageView instruction = (ImageView) view.findViewById(R.id.instruction_logo);

        home.setOnClickListener(view1 -> navController.navigate(R.id.action_favoriteFragment_to_disksFragment));
        author.setOnClickListener(view1 -> navController.navigate(R.id.action_favoriteFragment_to_authorFragment));
        about_prog.setOnClickListener(view1 -> navController.navigate(R.id.action_favoriteFragment_to_programInfoFragment));
        instruction.setOnClickListener(view1 -> navController.navigate(R.id.action_favoriteFragment_to_instructionManualFragment));

        loadFavoriteDisks();


        handler.postDelayed(() -> {
            imageSwitcherHeart = view.findViewById(R.id.imageSwitcherHeart);
            if (imageSwitcherHeart != null) {
                setRedHeart(imageSwitcherHeart);
            }
        }, 100);
    }

    private void loadFavoriteDisks() {
        userFavoriteDisksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoriteDisks.clear();

                for (DataSnapshot diskSnapshot : dataSnapshot.getChildren()) {
                    String manufacturerCode = diskSnapshot.getValue(String.class);
                    if (manufacturerCode != null) {
                        // Здесь можно использовать manufacturerCode для получения диска из базы данных
                        DiskDatabaseHelper dbHelper = new DiskDatabaseHelper(requireContext());
                        DiskDataClass disk = dbHelper.getDiskByManufacturerCode(manufacturerCode);
                        if (disk != null) {
                            favoriteDisks.add(disk);
                        }
                    }
                }

                disksList.setAdapter(adapter);

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок
            }
        });
    }

    private void setRedHeart(ImageSwitcher imageSwitcherHeart) {
        ((ImageView) imageSwitcherHeart.getCurrentView()).setImageResource(R.drawable.red_big_heart);
    }

    private void addToFavorites(String manufacturerCode) {
        userFavoriteDisksRef.child(manufacturerCode).child("disk_code").setValue(manufacturerCode);
    }

    private void removeFromFavorites(String manufacturerCode) {
        userFavoriteDisksRef.child(manufacturerCode).removeValue();
    }
}