package ga.demi.bakingapp;

import ga.demi.bakingapp.model.Example;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public final class Repository {

    private static Repository mRepository;
    private Retrofit mRetrofit;

    public static Repository getInstanceRepository() {
        if (mRepository == null) {
            mRepository = new Repository();
        }
        return mRepository;
    }

    private Repository() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<Example> getBakingListRequest() {
        return mRetrofit.create(IRequestAPI.class).getBakingList();
    }
}

interface IRequestAPI {
    @GET()
    Call<Example> getBakingList();
}