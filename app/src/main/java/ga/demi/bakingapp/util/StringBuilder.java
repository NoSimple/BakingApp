package ga.demi.bakingapp.util;

import java.util.List;

import ga.demi.bakingapp.model.Ingredient;

public class StringBuilder {

    public static String setIngredients(List<Ingredient> ingredientsList) {

        java.lang.StringBuilder builder = new java.lang.StringBuilder();

        for (Ingredient ingredient : ingredientsList) {
            builder.append("\u2713");
            builder.append(" ");
            builder.append(ingredient.getIngredient());
            builder.append(" (");
            builder.append(ingredient.getQuantity());
            builder.append(ingredient.getMeasure());
            builder.append(")");
            builder.append('\n');
        }
        return builder.toString();
    }
}