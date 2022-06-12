package com.diploma.youtube;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.R;

public class VideoOptionsAdapter extends RecyclerView.Adapter<VideoOptionsAdapter.ViewHolder> {
    VideoSettingsService videoSettingsService;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button videoOption;
        private final ImageButton deleteVideoOption;

        public ViewHolder(View view) {
            super(view);
            videoOption = view.findViewById(R.id.streaming_option_button);
            deleteVideoOption = view.findViewById(R.id.delete_streaming_option_button);
        }

        public Button getVideoOption() {
            return videoOption;
        }

        public ImageButton getDeleteVideoOption() {
            return deleteVideoOption;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param service Video setting service that contain all video setting options
     * by RecyclerView.
     */
    public VideoOptionsAdapter(VideoSettingsService service) {
        videoSettingsService = service;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.streaming_option_button, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getVideoOption()
                .setText(videoSettingsService.getVideoSetting(position).getVideoId());
        viewHolder.getDeleteVideoOption().setOnClickListener(v -> {
            videoSettingsService.removeSetting(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, videoSettingsService.getDataSetSize());
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (videoSettingsService == null) {
            return 0;
        }
        return videoSettingsService.getDataSetSize();
    }
}
