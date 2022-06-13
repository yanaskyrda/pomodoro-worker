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

import com.diploma.youtube.VideoOptionsAdapter;
import com.diploma.youtube.VideoSettingsService;
import com.diploma.youtube.YoutubeUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoTabFragment extends Fragment {

    public VideoTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VideoTabFragment.
     */
    public static VideoTabFragment newInstance() {
        return new VideoTabFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VideoSettingsService videoSettingsService = VideoSettingsService.getInstance();
        RecyclerView recyclerView = view.findViewById(R.id.video_option_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        VideoOptionsAdapter adapter = new VideoOptionsAdapter(videoSettingsService, null);
        recyclerView.setAdapter(adapter);

        final ImageButton saveSettingButton = view.findViewById(R.id.save_streaming_option_button);
        final TextInputLayout editTextLayout = view.findViewById(R.id.input_setting_layout);


        saveSettingButton.setOnClickListener(v -> {
            String videoId = YoutubeUtils.getVideoIdFromUrl(
                    Objects.requireNonNull(editTextLayout.getEditText()).getText().toString());
            videoSettingsService.addSetting(videoId);
            adapter.notifyDataSetChanged();
            editTextLayout.getEditText().setText("");
        });
    }
}