package com.tomclaw.wishlists.main;

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

    @ViewById
    Toolbar toolbar;

    @ViewById
    ViewFlipper mainViews;

    @ViewById
    AHBottomNavigation bottomNavigation;

    private MainView mainView;

    @AfterViews
    void initToolbar() {
        setSupportActionBar(toolbar);
    }

    @AfterViews
    void initMainViews() {
        WishesView wishesView = WishesView_.build(this);
        mainViews.addView(wishesView);

        IdeasView ideasView = IdeasView_.build(this);
        mainViews.addView(ideasView);

        ContactsView contactsView = ContactsView_.build(this);
        mainViews.addView(contactsView);

        ProfileView profileView = ProfileView_.build(this);
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
