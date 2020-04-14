package ga.demi.bakingapp.view;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ga.demi.bakingapp.R;
import ga.demi.bakingapp.model.Step;
import ga.demi.bakingapp.util.Constants;

public final class StepActivity extends AppCompatActivity {

    private String recipeName;
    private List<Step> stepsList;
    private int currentPosition;

    @BindView(R.id.tool_bar)
    protected Toolbar toolBar;

    @BindView(R.id.button_back)
    protected TextView buttonBack;

    @BindView(R.id.button_next)
    protected TextView buttonNext;

    @BindView(R.id.text_count_step)
    protected TextView textCountStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            recipeName = intent.getStringExtra(Constants.RECIPE_NAME);
            currentPosition = intent.getIntExtra(Constants.CURRENT_POSITION, 0);
            stepsList = intent.getParcelableArrayListExtra(Constants.STEPS);
        } else {
            recipeName = savedInstanceState.getString(Constants.RECIPE_NAME);
            currentPosition = savedInstanceState.getInt(Constants.CURRENT_POSITION, 0);
            stepsList = savedInstanceState.getParcelableArrayList(Constants.STEPS);
        }

        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            getSupportActionBar().setTitle(recipeName);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Drawable drawable = toolBar.getNavigationIcon();
            toolBar.setNavigationOnClickListener(v -> onBackPressed());

            if (drawable != null) {
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }

            if (savedInstanceState == null) {
                getNextFragment(currentPosition);
            }

            setTextCount(currentPosition);
            setButtonClicks();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(Constants.RECIPE_NAME, recipeName);
        outState.putInt(Constants.CURRENT_POSITION, currentPosition);
        outState.putParcelableArrayList(Constants.STEPS, (ArrayList<? extends Parcelable>) stepsList);
    }

    private void setTextCount(int currentPosition) {
        textCountStep.setText((currentPosition + 1) + "/" + stepsList.size());
    }

    private void setButtonClicks() {

        if (currentPosition == 0) {
            buttonBack.setVisibility(View.INVISIBLE);
        } else if (currentPosition == stepsList.size() - 1) {
            buttonNext.setText(getResources().getText(R.string.button_exit_text));
        }

        buttonNext.setOnClickListener(view -> {
            if (currentPosition == stepsList.size() - 2) {
                buttonNext.setText(getResources().getText(R.string.button_exit_text));
                ++currentPosition;
                getNextFragment(currentPosition);
            } else if (currentPosition == stepsList.size() - 1) {
                finish();
            } else {
                buttonBack.setVisibility(View.VISIBLE);
                ++currentPosition;
                getNextFragment(currentPosition);
            }

            setTextCount(currentPosition);
        });

        buttonBack.setOnClickListener(view -> {
            if (currentPosition == 1) {
                buttonBack.setVisibility(View.INVISIBLE);
                --currentPosition;
                getNextFragment(currentPosition);
            } else if (currentPosition == stepsList.size() - 1) {
                buttonNext.setText(getResources().getText(R.string.button_next_text));
                --currentPosition;
                getNextFragment(currentPosition);
            } else {
                --currentPosition;
                getNextFragment(currentPosition);
            }

            setTextCount(currentPosition);
        });
    }

    private void getNextFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.STEPS, stepsList.get(position));
        stepFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, stepFragment);
        transaction.commit();
    }
}