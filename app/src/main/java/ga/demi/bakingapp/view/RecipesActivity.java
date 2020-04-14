package ga.demi.bakingapp.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ga.demi.bakingapp.R;
import ga.demi.bakingapp.adapter.RecipeAdapter;
import ga.demi.bakingapp.data.DataPreferences;
import ga.demi.bakingapp.model.Recipe;
import ga.demi.bakingapp.util.Constants;
import ga.demi.bakingapp.util.ErrorType;
import ga.demi.bakingapp.viewmodel.RecipeActivityViewModel;

public final class RecipesActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    private RecipeActivityViewModel activityViewModel;
    private RecipeAdapter adapter;
    private List<Recipe> recipesList;

    @BindView(R.id.tool_bar)
    protected Toolbar toolBar;

    @BindView(R.id.refresh_recipes)
    protected SwipeRefreshLayout refreshRecipes;

    @BindView(R.id.recipes_list)
    protected RecyclerView recyclerRecipes;

    @BindView(R.id.progress_loading)
    protected ProgressBar progressLoading;

    @BindView(R.id.text_error)
    protected TextView textError;

    @BindView(R.id.button_connect)
    protected Button buttonConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        activityViewModel = new ViewModelProvider(this).get(RecipeActivityViewModel.class);

        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            getSupportActionBar().setTitle(R.string.recipes_title);
        }

        getRecipesData(false);

        refreshRecipes.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refreshRecipes.setOnRefreshListener(() -> getRecipesData(true));
        buttonConnect.setOnClickListener(view -> getRecipesData(false));
    }

    @Override
    public void onRecipeItemClick(int position) {
        Recipe recipe = recipesList.get(position);
        DataPreferences.setRecipePreference(this, recipe);

        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(Constants.RECIPE_NAME, recipe.getName());
        intent.putParcelableArrayListExtra(Constants.INGREDIENTS, (ArrayList<? extends Parcelable>) recipe.getIngredients());
        intent.putParcelableArrayListExtra(Constants.STEPS, (ArrayList<? extends Parcelable>) recipe.getSteps());
        startActivity(intent);
    }

    private void getRecipesData(Boolean isRefresh) {

        if (isRefresh) {
            refreshRecipes.setRefreshing(true);
        } else {
            showProgress(true);
        }

        activityViewModel.getRecipesLiveData().observe(this, recipes -> {

            if (recipes.getErrorType() == null) {
                recipesList = recipes.getRecipes();
                if (isRefresh) {
                    notifyRecycler(recipes.getRecipes());
                } else {
                    setAdapter(recipes.getRecipes());
                }
                showErrorMessage(null);
            } else {
                showErrorMessage(recipes.getErrorType());
                showProgress(false);
            }
        });
    }

    private void setAdapter(List<Recipe> recipesList) {

        if (adapter == null) {
            adapter = new RecipeAdapter(recipesList, this);
        }

        LinearLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count));
        recyclerRecipes.setLayoutManager(layoutManager);
        recyclerRecipes.setHasFixedSize(true);
        recyclerRecipes.setAdapter(adapter);

        showProgress(false);
    }

    private void notifyRecycler(List<Recipe> recipesList) {
        adapter.notifyRecipesList(recipesList);
        refreshRecipes.setRefreshing(false);
    }

    private void showProgress(Boolean show) {
        if (show) {
            progressLoading.setVisibility(View.VISIBLE);
        } else {
            progressLoading.setVisibility(View.GONE);
        }
    }

    private void showErrorMessage(@Nullable ErrorType error) {

        if (error != null) {
            textError.setText(error.getErrorName());
            textError.setVisibility(View.VISIBLE);
            refreshRecipes.setVisibility(View.GONE);
            if (error == ErrorType.NETWORK_CONNECTION) {
                buttonConnect.setVisibility(View.VISIBLE);
            } else {
                buttonConnect.setVisibility(View.GONE);
            }
        } else {
            refreshRecipes.setVisibility(View.VISIBLE);
            textError.setVisibility(View.GONE);
            buttonConnect.setVisibility(View.GONE);
        }
    }
}