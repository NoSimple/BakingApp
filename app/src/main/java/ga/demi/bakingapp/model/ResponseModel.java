package ga.demi.bakingapp.model;

import java.util.List;

import ga.demi.bakingapp.util.ErrorType;

public class ResponseModel {

    private List<Recipe> recipes = null;
    private ErrorType errorType = null;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType textError) {
        this.errorType = textError;
    }
}