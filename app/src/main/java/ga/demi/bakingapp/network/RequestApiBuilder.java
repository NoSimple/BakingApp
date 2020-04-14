package ga.demi.bakingapp.network;

import java.util.List;

import ga.demi.bakingapp.util.Constants;
import ga.demi.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RequestApiBuilder {

    private static RequestApiBuilder requestApiBuilder;
    private Retrofit retrofit;

    public static RequestApiBuilder getInstanceRepository() {
        if (requestApiBuilder == null) {
            requestApiBuilder = new RequestApiBuilder();
        }
        return requestApiBuilder;
    }

    private RequestApiBuilder() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<List<Recipe>> getBakingListRequest() {
        return retrofit.create(IRequestApi.class).getBakingListApi();
    }
}