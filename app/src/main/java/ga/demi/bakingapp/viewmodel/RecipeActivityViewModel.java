package ga.demi.bakingapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ga.demi.bakingapp.model.Recipe;
import ga.demi.bakingapp.model.ResponseModel;
import ga.demi.bakingapp.network.RequestApiBuilder;
import ga.demi.bakingapp.util.ErrorType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivityViewModel extends ViewModel {

    private ResponseModel responseModel = new ResponseModel();
    private MutableLiveData<ResponseModel> liveData = new MutableLiveData<>();

    public LiveData<ResponseModel> getRecipesLiveData() {

        RequestApiBuilder.getInstanceRepository().getBakingListRequest().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NotNull Call<List<Recipe>> call, @NotNull Response<List<Recipe>> response) {
                if (response.body() != null) {
                    responseModel.setRecipes(response.body());
                    responseModel.setErrorType(null);
                    liveData.setValue(responseModel);
                } else {
                    responseModel.setRecipes(null);
                    responseModel.setErrorType(ErrorType.EMPTY_DATA);
                    liveData.setValue(responseModel);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Recipe>> call, Throwable t) {
                responseModel.setRecipes(null);
                responseModel.setErrorType(ErrorType.NETWORK_CONNECTION);
                liveData.setValue(responseModel);
            }
        });

        return liveData;
    }
}