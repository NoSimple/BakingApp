package ga.demi.bakingapp.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ga.demi.bakingapp.R;
import ga.demi.bakingapp.model.Step;
import ga.demi.bakingapp.util.Constants;

public final class StepFragment extends Fragment {

    @BindView(R.id.video_player)
    protected PlayerView videoPlayer;

    @BindView(R.id.text_title_description)
    protected TextView textTitleDescription;

    @BindView(R.id.text_description)
    protected TextView textDescription;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private String videoUrl = null;
    private SimpleExoPlayer player;
    private Step step;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            step = bundle.getParcelable(Constants.STEPS);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(step);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT >= 24) {
            if (videoUrl != null) {
                initPlayer();
            } else {
                videoPlayer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Util.SDK_INT < 24 || player == null)) {
            if (videoUrl != null) {
                initPlayer();
            } else {
                videoPlayer.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    private void init(Step step) {

        if (step != null) {
            textTitleDescription.setText(R.string.text_title_description);
            textDescription.setText(step.getDescription());

            if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
                videoUrl = step.getVideoURL();
            } else if (step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty()) {
                videoUrl = step.getThumbnailURL();
            } else {
                videoUrl = null;
            }
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getActivity()), "baking-app");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private void initPlayer() {
        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);

        player = ExoPlayerFactory.newSimpleInstance(Objects.requireNonNull(getActivity()));
        videoPlayer.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }
}