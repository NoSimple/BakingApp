package ga.demi.bakingapp.util;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public final class SimpleIdlingResource implements IdlingResource {

    private static SimpleIdlingResource sIdlingResource;

    public static IdlingResource getIdlingResource() {
        if (sIdlingResource == null) {
            sIdlingResource = new SimpleIdlingResource();
        }
        return sIdlingResource;
    }

    public static void setIdleResource(boolean isIdleNow) {
        if (sIdlingResource == null) {
            sIdlingResource = new SimpleIdlingResource();
        }
        sIdlingResource.setIdleState(isIdleNow);
    }

    @Nullable
    private volatile IdlingResource.ResourceCallback mCallback;

    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(IdlingResource.ResourceCallback callback) {
        mCallback = callback;
    }


    private void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}