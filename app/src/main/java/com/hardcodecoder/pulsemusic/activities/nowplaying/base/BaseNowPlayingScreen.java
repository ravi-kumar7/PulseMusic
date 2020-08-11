package com.hardcodecoder.pulsemusic.activities.nowplaying.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;
import com.hardcodecoder.pulsemusic.PMS;
import com.hardcodecoder.pulsemusic.R;
import com.hardcodecoder.pulsemusic.helper.MediaProgressUpdateHelper;
import com.hardcodecoder.pulsemusic.model.MusicModel;
import com.hardcodecoder.pulsemusic.singleton.TrackManager;
import com.hardcodecoder.pulsemusic.storage.AppFileManager;


public abstract class BaseNowPlayingScreen extends Fragment implements MediaProgressUpdateHelper.Callback {

    private final TrackManager mTrackManager = TrackManager.getInstance();
    private MediaController mController;
    private MediaController.TransportControls mTransportControls;
    private MediaProgressUpdateHelper mUpdateHelper;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PMS.ServiceBinder serviceBinder = (PMS.ServiceBinder) service;
            mController = serviceBinder.getMediaController();
            mTransportControls = mController.getTransportControls();
            mUpdateHelper = new MediaProgressUpdateHelper(mController, BaseNowPlayingScreen.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private boolean mCurrentItemFavorite = false;

    @CallSuper
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        connectToService();
    }

    @CallSuper
    @Override
    public void onMetadataDataChanged(MediaMetadata metadata) {
        updateFavoriteItem();
        updateRepeat();
    }

    @CallSuper
    @Override
    public void onPlaybackStateChanged(PlaybackState state) {
        updateRepeat();
    }

    protected void setUpSliderControls(Slider progressSlider) {
        progressSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                mUpdateHelper.stop();
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                // Pass progress in milli seconds
                mTransportControls.seekTo((long) slider.getValue() * 1000);
                onProgressChanged((int) slider.getValue());
            }
        });
        progressSlider.setLabelFormatter(value -> DateUtils.formatElapsedTime((long) value));
    }

    protected void resetSliderValues(Slider slider, long valueTo) {
        slider.setValue(0);
        slider.setValueTo(valueTo);
    }

    protected void setUpSkipControls(ImageView skipPrev, ImageView skipNext) {
        skipPrev.setOnClickListener(v -> mTransportControls.skipToPrevious());
        skipNext.setOnClickListener(v -> mTransportControls.skipToNext());
    }

    protected void toggleRepeatMode() {
        boolean repeat = !mTrackManager.isCurrentTrackInRepeatMode();
        mTrackManager.repeatCurrentTrack(repeat);
        onRepeatStateChanged(repeat);
    }

    protected void toggleFavorite() {
        if (mCurrentItemFavorite) {
            AppFileManager.deleteFavorite(mTrackManager.getActiveQueueItem());
            Toast.makeText(getContext(), getString(R.string.removed_from_fav), Toast.LENGTH_SHORT).show();
        } else {
            AppFileManager.addItemToFavorites(mTrackManager.getActiveQueueItem());
            Toast.makeText(getContext(), getString(R.string.added_to_fav), Toast.LENGTH_SHORT).show();
        }
        updateFavoriteItem();
    }

    protected void togglePlayPause() {
        if (null == mController.getPlaybackState()) return;
        PlaybackState state = mController.getPlaybackState();

        if (state.getState() == PlaybackState.STATE_PLAYING) mTransportControls.pause();
        else mTransportControls.play();
    }


    protected void togglePlayPauseAnimation(ImageView playPauseBtn, PlaybackState state) {
        if (null == state || null == getContext() || null == playPauseBtn) return;

        if (state.getState() == PlaybackState.STATE_PLAYING) {
            Drawable d = getContext().getDrawable(R.drawable.play_to_pause_linear_out_slow_in);
            playPauseBtn.setImageDrawable(d);
            if (d instanceof AnimatedVectorDrawable) ((AnimatedVectorDrawable) d).start();
        } else {
            Drawable d = getContext().getDrawable(R.drawable.pause_to_play);
            playPauseBtn.setImageDrawable(d);
            if (d instanceof AnimatedVectorDrawable) ((AnimatedVectorDrawable) d).start();
        }
    }

    protected String getUpNextText() {
        MusicModel nextItem = TrackManager.getInstance().getNextQueueItem();
        String upNextText;
        if (null != nextItem)
            upNextText = getString(R.string.up_next_title).concat(" ").concat(nextItem.getTrackName());
        else upNextText = getString(R.string.up_next_title_none);
        return upNextText;
    }

    protected String getFormattedElapsedTime(long elapsedTime) {
        return DateUtils.formatElapsedTime(elapsedTime);
    }

    private void updateRepeat() {
        onRepeatStateChanged(mTrackManager.isCurrentTrackInRepeatMode());
    }

    private void updateFavoriteItem() {
        AppFileManager.isItemAFavorite(mTrackManager.getActiveQueueItem(), result ->
                onFavoriteStateChanged((mCurrentItemFavorite = result)));
    }

    private void connectToService() {
        if (null != getActivity()) {
            Intent intent = new Intent(getActivity(), PMS.class);
            getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public abstract void onRepeatStateChanged(boolean repeat);

    public abstract void onFavoriteStateChanged(boolean isFavorite);

    @Override
    public void onDestroy() {
        if (null != mUpdateHelper)
            mUpdateHelper.destroy();
        if (null != getActivity())
            getActivity().unbindService(mServiceConnection);
        super.onDestroy();
    }
}