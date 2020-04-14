package ga.demi.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import ga.demi.bakingapp.R;
import ga.demi.bakingapp.data.DataPreferences;
import ga.demi.bakingapp.model.Ingredient;
import ga.demi.bakingapp.model.Recipe;
import ga.demi.bakingapp.util.StringBuilder;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Recipe recipe;
    private List<Ingredient> ingredientsList = new ArrayList<>();
    private Context context;

    public WidgetRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        recipe = DataPreferences.getRecipePreference(context);
        ingredientsList.clear();
        ingredientsList.addAll(recipe.getIngredients());
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return ingredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_item);
        remoteView.setTextViewText(R.id.widget_recipe_title, recipe.getName());
        remoteView.setTextViewText(R.id.widget_recipe_text, StringBuilder.setIngredients(ingredientsList));

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}