package com.chibusoft.smartcinema;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.chibusoft.smartcinema.Architecture.AppDatabase;
import com.chibusoft.smartcinema.Architecture.MoviesRoom;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.List;

@RunWith(AndroidJUnit4.class)
public class VisualTest {

    private CountingIdlingResource mIdlingResource;
    private List<MoviesRoom> moviesRooms;
    private AppDatabase mDb;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);

        Context context = mActivityTestRule.getActivity();//.getApplicationContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        moviesRooms = mDb.moviesDao().loadAllMoviesTest();
    }



    @Test
    public void recycleview_display() {
        onView(withId(R.id.rv_movies)).check(matches(isDisplayed()));
    }

    @Test
    public void detailsActivity_display() {
        onView(ViewMatchers.withId(R.id.rv_movies)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.movieimage_details)).check(matches(isDisplayed()));

        onView(withId(R.id.movieimage_details)).check(matches(isDisplayed()));

        onView(withId(R.id.title_details)).check(matches(isDisplayed()));

        onView(withId(R.id.release_details)).check(matches(isDisplayed()));

        onView(withId(R.id.favorite_btn)).check(matches(isDisplayed()));

    }

    @Test
    public void testRoom() throws Exception {

        onView(ViewMatchers.withId(R.id.rv_movies)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.movieimage_details)).check(matches(isDisplayed()));

        //here we click button to add movie to database
        onView(ViewMatchers.withId(R.id.favorite_btn)).perform(click());

        assert (!moviesRooms.isEmpty());
    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }

        mDb.close();
    }

}
