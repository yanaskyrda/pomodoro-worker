package com.diploma.spotify;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.R;
import com.diploma.youtube.VideoSettingsService;

public class MusicOptionsAdapter extends RecyclerView.Adapter<MusicOptionsAdapter.ViewHolder> {
    MusicsSettingsService musicsSettingsService;
    AlertDialog activeAlertDialog;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button musicOption;
        private final ImageButton deleteMusicOption;

        public ViewHolder(View view) {
            super(view);
            musicOption = view.findViewById(R.id.streaming_option_button);
            deleteMusicOption = view.findViewById(R.id.delete_streaming_option_button);
        }

        public Button getMusicOption() {
            return musicOption;
        }

        public ImageButton getDeleteMusicOption() {
            return deleteMusicOption;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param service Music setting service that contain all video setting options
     * by RecyclerView.
     */
    public MusicOptionsAdapter(MusicsSettingsService service, AlertDialog musicSettingsDialog) {
        musicsSettingsService = service;
        activeAlertDialog = musicSettingsDialog;
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
        viewHolder.getMusicOption()
                .setText(SpotifyUtils.getPlaylistTitleFromId(
                        musicsSettingsService.getMusicSetting(position).getPlaylistId(), 30));
        viewHolder.getMusicOption().setOnClickListener(v -> {
            musicsSettingsService.setActiveSetting(musicsSettingsService.getMusicSetting(position));
            dismissPopup();
        });
        viewHolder.getDeleteMusicOption().setOnClickListener(v -> {
            musicsSettingsService.removeSetting(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, musicsSettingsService.getDataSetSize());
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (musicsSettingsService == null) {
            return 0;
        }
        return musicsSettingsService.getDataSetSize();
    }

    private void dismissPopup() {
        activeAlertDialog.dismiss();
    }
}
