package ga.demi.bakingapp.view;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ga.demi.bakingapp.R;
import ga.demi.bakingapp.util.SimpleIdlingResource;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class RecipesActivityTest {

    @Rule
    public final ActivityTestRule<RecipesActivity> recipeActivityActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = SimpleIdlingResource.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void clickRecipe_OpenRecipeInfoActivity() {
        onView(withId(R.id.recipes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}