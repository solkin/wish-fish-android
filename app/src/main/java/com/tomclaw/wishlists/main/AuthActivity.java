package com.tomclaw.wishlists.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.tomclaw.wishlists.R;
import com.tomclaw.wishlists.main.view.HintEditText;
import com.tomclaw.wishlists.util.CountriesProvider;
import com.tomclaw.wishlists.util.Country;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BeforeTextChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import static com.tomclaw.wishlists.main.CountryActivity.EXTRA_COUNTRY;

/**
 * Created by solkin on 09.03.17.
 */
@EActivity(R.layout.activity_auth)
public class AuthActivity extends AppCompatActivity {

    private static final int REQUEST_COUNTRY_CODE = 1;

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView countrySelect;

    @ViewById
    TextView countryCode;

    @ViewById
    HintEditText phoneNumber;

    @Bean
    CountriesProvider countriesProvider;

    private int characterAction = -1;
    private int actionPosition;
    private boolean ignoreOnPhoneChange = false;

    @AfterViews
    void init() {
        try {
            Country country = countriesProvider.getCountryByCurrentLocale("US");
            onCountryChanged(country);
        } catch (CountriesProvider.CountryNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterViews
    void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle(R.string.your_phone);
    }

    @SuppressLint("DefaultLocale")
    private void onCountryChanged(Country country) {
        countrySelect.setText(country.getName());
        countryCode.setText(String.format("+%d", country.getCode()));
        phoneNumber.setHintText(country.getPhoneFormat().replace('X', '-'));
    }

    @BeforeTextChange
    void phoneNumberBeforeTextChanged(CharSequence s, int start, int count, int after) {
        if (count == 0 && after == 1) {
            characterAction = 1;
        } else if (count == 1 && after == 0) {
            if (s.charAt(start) == ' ' && start > 0) {
                characterAction = 3;
                actionPosition = start - 1;
            } else {
                characterAction = 2;
            }
        } else {
            characterAction = -1;
        }
    }

    @AfterTextChange
    public void phoneNumberAfterTextChanged(Editable s) {
        if (ignoreOnPhoneChange) {
            return;
        }
        int start = phoneNumber.getSelectionStart();
        String phoneChars = "0123456789";
        String str = phoneNumber.getText().toString();
        if (characterAction == 3) {
            str = str.substring(0, actionPosition) + str.substring(actionPosition + 1, str.length());
            start--;
        }
        StringBuilder builder = new StringBuilder(str.length());
        for (int a = 0; a < str.length(); a++) {
            String ch = str.substring(a, a + 1);
            if (phoneChars.contains(ch)) {
                builder.append(ch);
            }
        }
        ignoreOnPhoneChange = true;
        String hint = phoneNumber.getHintText();
        if (hint != null) {
            for (int a = 0; a < builder.length(); a++) {
                if (a < hint.length()) {
                    if (hint.charAt(a) == ' ') {
                        builder.insert(a, ' ');
                        a++;
                        if (start == a && characterAction != 2 && characterAction != 3) {
                            start++;
                        }
                    }
                } else {
                    builder.insert(a, ' ');
                    if (start == a + 1 && characterAction != 2 && characterAction != 3) {
                        start++;
                    }
                    break;
                }
            }
        }
        phoneNumber.setText(builder);
        if (start >= 0) {
            phoneNumber.setSelection(start <= phoneNumber.length() ? start : phoneNumber.length());
        }
        phoneNumber.onTextChange();
        ignoreOnPhoneChange = false;
    }

    @EditorAction
    boolean phoneNumberEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            onNextClicked();
            return true;
        }
        return false;
    }

    @Click(R.id.country_select)
    void onCountrySelectClicked() {
        CountryActivity_
                .intent(this)
                .startForResult(REQUEST_COUNTRY_CODE);
    }

    @Click(R.id.next_button)
    void onNextClicked() {

    }

    @OnActivityResult(REQUEST_COUNTRY_CODE)
    void onResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Country country = data.getParcelableExtra(EXTRA_COUNTRY);
            onCountryChanged(country);
        }
    }
}
