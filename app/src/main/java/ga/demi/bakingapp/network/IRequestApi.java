package ga.demi.bakingapp.network;

import java.util.List;

import ga.demi.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IRequestApi {

    @GET("baking.json")
    Call<List<Recipe>> getBakingListApi();
}