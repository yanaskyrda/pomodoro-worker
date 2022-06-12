package com.diploma.timer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.R;

public class SessionOptionsAdapter extends RecyclerView.Adapter<SessionOptionsAdapter.ViewHolder> {
    SessionsSettingsService sessionsSettingsService;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button sessionOption;
        private final ImageButton editSessionOption;
        private final ImageButton deleteSessionOption;

        public ViewHolder(View view) {
            super(view);
            sessionOption = view.findViewById(R.id.session_option_button);
            editSessionOption = view.findViewById(R.id.edit_session_button);
            deleteSessionOption = view.findViewById(R.id.delete_session_button);
        }

        public Button getSessionOption() {
            return sessionOption;
        }

        public ImageButton getEditSessionOption() {
            return editSessionOption;
        }

        public ImageButton getDeleteSessionOption() {
            return deleteSessionOption;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param service Sessions setting service that contain all session setting options
     * by RecyclerView.
     */
    public SessionOptionsAdapter(SessionsSettingsService service) {
        sessionsSettingsService = service;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.session_option_button, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getSessionOption()
                .setText(sessionsSettingsService.getSessionSetting(position).getSimpleName());
        viewHolder.getDeleteSessionOption().setOnClickListener(v -> {
            sessionsSettingsService.removeSetting(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, sessionsSettingsService.getDataSetSize());
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sessionsSettingsService.getDataSetSize();
    }
}
