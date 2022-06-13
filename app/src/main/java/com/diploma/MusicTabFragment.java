package com.diploma;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.spotify.MusicOptionsAdapter;
import com.diploma.spotify.MusicsSettingsService;
import com.diploma.spotify.SpotifyUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicTabFragment extends Fragment {

    public MusicTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MusicTabFragment.
     */
    public static MusicTabFragment newInstance() {
        return new MusicTabFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MusicsSettingsService musicsSettingsService = MusicsSettingsService.getInstance();
        RecyclerView recyclerView = view.findViewById(R.id.music_option_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        MusicOptionsAdapter adapter = new MusicOptionsAdapter(musicsSettingsService, null);
        recyclerView.setAdapter(adapter);

        final ImageButton saveSettingButton = view.findViewById(R.id.save_streaming_option_button);
        final TextInputLayout editTextLayout = view.findViewById(R.id.input_setting_layout);


        saveSettingButton.setOnClickListener(v -> {
            String playlistId = SpotifyUtils.getPlaylistIdFromUrl(
                    Objects.requireNonNull(editTextLayout.getEditText()).getText().toString());
            musicsSettingsService.addSetting(playlistId);
            adapter.notifyDataSetChanged();
            editTextLayout.getEditText().setText("");
        });
    }
}