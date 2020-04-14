package ga.demi.bakingapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import ga.demi.bakingapp.model.Recipe;
import ga.demi.bakingapp.util.Constants;

import static android.content.Context.MODE_PRIVATE;

public class DataPreferences {

    public static void setRecipePreference(Context context, Recipe recipe) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.GET_PREFERENCE, MODE_PRIVATE);

        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().apply();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.GET_RECIPE, new Gson().toJson(recipe)).apply();
    }

    public static Recipe getRecipePreference(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(Constants.GET_PREFERENCE, MODE_PRIVATE);
        String recipeJSON = sharedPref.getString(Constants.GET_RECIPE, "");
        return new Gson().fromJson(recipeJSON, Recipe.class);
    }
}