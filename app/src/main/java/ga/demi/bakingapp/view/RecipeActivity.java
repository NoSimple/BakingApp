package ga.demi.bakingapp.view;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import ga.demi.bakingapp.R;
import ga.demi.bakingapp.adapter.RecipeDetailAdapter;
import ga.demi.bakingapp.model.Ingredient;
import ga.demi.bakingapp.model.Step;
import ga.demi.bakingapp.util.Constants;
import ga.demi.bakingapp.util.StringBuilder;
import ga.demi.bakingapp.widget.AppWidget;

public final class RecipeActivity extends AppCompatActivity implements RecipeDetailAdapter.RecipeDetailItemClickListener {

    private String recipeName;
    private List<Ingredient> ingredientsList;
    private List<Step> stepsList;
    private RecipeDetailAdapter adapter;

    @BindView(R.id.tool_bar)
    protected Toolbar toolBar;

    @BindView(R.id.text_ingredients)
    protected TextView ingredientsText;

    @BindView(R.id.recipe_steps_list)
    protected RecyclerView recyclerSteps;

    @BindView(R.id.fab_widget)
    protected FloatingActionButton fabToWidget;

    @BindBool(R.bool.is_tablet)
    protected boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            recipeName = intent.getStringExtra(Constants.RECIPE_NAME);
            ingredientsList = intent.getParcelableArrayListExtra(Constants.INGREDIENTS);
            stepsList = intent.getParcelableArrayListExtra(Constants.STEPS);
        } else {
            recipeName = savedInstanceState.getString(Constants.RECIPE_NAME);
            ingredientsList = savedInstanceState.getParcelableArrayList(Constants.INGREDIENTS);
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
        }

        initComponents();

        fabToWidget.setOnClickListener(view -> {
            addWidget();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(Constants.RECIPE_NAME, recipeName);
        outState.putParcelableArrayList(Constants.INGREDIENTS, (ArrayList<? extends Parcelable>) ingredientsList);
        outState.putParcelableArrayList(Constants.STEPS, (ArrayList<? extends Parcelable>) stepsList);
    }

    @Override
    public void onRecipeDetailItemClick(int position) {

        if (isTablet) {
            initFragment(position);
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(Constants.RECIPE_NAME, recipeName);
            intent.putExtra(Constants.CURRENT_POSITION, position);
            intent.putParcelableArrayListExtra(Constants.STEPS, (ArrayList<? extends Parcelable>) stepsList);
            startActivity(intent);
        }
    }

    private void initComponents() {

        ingredientsText.setText(StringBuilder.setIngredients(ingredientsList));

        if (adapter == null) {
            adapter = new RecipeDetailAdapter(stepsList, this);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerSteps.setLayoutManager(layoutManager);
        recyclerSteps.setHasFixedSize(true);
        recyclerSteps.setAdapter(adapter);

        if (isTablet) {
            initFragment(0);
        }
    }

    private void addWidget() {
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, AppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipes_list);

        Toast.makeText(this, getString(R.string.text_save_widget), Toast.LENGTH_LONG).show();
    }

    private void initFragment(int position) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.STEPS, stepsList.get(position));
        stepFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, stepFragment);
        transaction.commit();
    }
}