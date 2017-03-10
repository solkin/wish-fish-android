package com.tomclaw.wishlists.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ViewFlipper;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tomclaw.wishlists.R;
import com.tomclaw.wishlists.main.view.ContactsView;
import com.tomclaw.wishlists.main.view.ContactsView_;
import com.tomclaw.wishlists.main.view.IdeasView;
import com.tomclaw.wishlists.main.view.IdeasView_;
import com.tomclaw.wishlists.main.view.MainView;
import com.tomclaw.wishlists.main.view.ProfileView;
import com.tomclaw.wishlists.main.view.ProfileView_;
import com.tomclaw.wishlists.main.view.WishesView;
import com.tomclaw.wishlists.main.view.WishesView_;
import com.tomclaw.wishlists.util.ColorHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity
        extends AppCompatActivity
        implements MainView.ActivityCallback {

    private static final String KEY_WISHES_VIEW = "wishes_view";
    private static final String KEY_IDEAS_VIEW = "ideas_view";
    private static final String KEY_CONTACTS_VIEW = "contacts_view";
    private static final String KEY_PROFILE_VIEW = "profile_view";

    @ViewById
    Toolbar toolbar;

    @ViewById
    ViewFlipper mainViews;

    @ViewById
    AHBottomNavigation bottomNavigation;

    private MainView mainView;

    private WishesView wishesView;
    private IdeasView ideasView;
    private ContactsView contactsView;
    private ProfileView profileView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle wishesState = null;
        Bundle ideasState = null;
        Bundle contactsState = null;
        Bundle profileState = null;
        if (savedInstanceState != null) {
            wishesState = savedInstanceState.getBundle(KEY_WISHES_VIEW);
            ideasState = savedInstanceState.getBundle(KEY_IDEAS_VIEW);
            contactsState = savedInstanceState.getBundle(KEY_CONTACTS_VIEW);
            profileState = savedInstanceState.getBundle(KEY_PROFILE_VIEW);
        }
        wishesView = WishesView_.build(this, wishesState);
        ideasView = IdeasView_.build(this, ideasState);
        contactsView = ContactsView_.build(this, contactsState);
        profileView = ProfileView_.build(this, profileState);

        // TODO: use some credentials check to run authentication.
        AuthActivity_.intent(this).start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_WISHES_VIEW, wishesView.onSaveState());
        outState.putBundle(KEY_IDEAS_VIEW, ideasView.onSaveState());
        outState.putBundle(KEY_CONTACTS_VIEW, contactsView.onSaveState());
        outState.putBundle(KEY_PROFILE_VIEW, profileView.onSaveState());
    }

    @AfterViews
    void initToolbar() {
        setSupportActionBar(toolbar);
    }

    @AfterViews
    void initMainViews() {
        mainViews.addView(wishesView);
        mainViews.addView(ideasView);
        mainViews.addView(contactsView);
        mainViews.addView(profileView);

        mainViews.setDisplayedChild(0);
    }

    @AfterViews
    void initBottomNavigation() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.my_wish_list, R.drawable.gift, R.color.color_accent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.ideas, R.drawable.ideas, R.color.color_accent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.contacts, R.drawable.contacts, R.color.color_accent);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.profile, R.drawable.face, R.color.color_accent);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setDefaultBackgroundColor(ColorHelper.getAttributedColor(this, R.attr.bottom_bar_background));
        bottomNavigation.setAccentColor(getResources().getColor(R.color.color_accent));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.grey_dark));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                selectTab(position);
                return true;
            }
        });

        bottomNavigation.post(new Runnable() {
            @Override
            public void run() {
                selectTab(bottomNavigation.getCurrentItem());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        operateMainViews(new MainViewOperation() {
            @Override
            public void invoke(MainView mainView) {
                mainView.start();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        operateMainViews(new MainViewOperation() {
            @Override
            public void invoke(MainView mainView) {
                mainView.stop();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        operateMainViews(new MainViewOperation() {
            @Override
            public void invoke(MainView mainView) {
                mainView.destroy();
            }
        });
    }

    private void operateMainViews(MainViewOperation operation) {
        for (int c = 0; c < mainViews.getChildCount(); c++) {
            MainView mainView = (MainView) mainViews.getChildAt(c);
            operation.invoke(mainView);
        }
    }

    private void switchMainView(int index) {
        mainViews.setDisplayedChild(index);
        mainView = (MainView) mainViews.getChildAt(index);
        mainView.activate(this);

        invalidateOptionsMenu();
    }

    private void selectTab(int position) {
        switch (position) {
            case 0:
                showWishList();
                break;
            case 1:
                showContacts();
                break;
            case 2:
                showIdeas();
                break;
            case 3:
                showProfile();
                break;
        }
    }

    private void showWishList() {
        switchMainView(0);
    }

    private void showContacts() {
        switchMainView(1);
    }

    private void showIdeas() {
        switchMainView(2);
    }

    private void showProfile() {
        switchMainView(3);
    }

    private interface MainViewOperation {

        void invoke(MainView mainView);

    }

}
