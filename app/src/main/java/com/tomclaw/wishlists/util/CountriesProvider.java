package com.tomclaw.wishlists.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Solkin on 22.10.2014.
 */
@EBean(scope = EBean.Scope.Singleton)
public class CountriesProvider {

    @RootContext
    Context context;

    private List<Country> countries = new ArrayList<>();

    public List<Country> getCountries() {
        if (countries.isEmpty()) {
            try {
                InputStream stream = context.getApplicationContext().getResources().getAssets()
                        .open("countries.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] args = line.split(";");
                    int code = Integer.parseInt(args[0]);
                    String shortName = args[1];
                    String name = args[2];
                    String phoneFormat = "";
                    if (args.length > 3) {
                        phoneFormat = args[3];
                    }
                    Country c = new Country(name, code, shortName, phoneFormat);
                    countries.add(c);
                }
                reader.close();
                stream.close();
            } catch (Exception ex) {
                Logger.d(ex.getMessage());
            }
            Collections.sort(countries);
        }
        return countries;
    }

    public Country getCountryByCurrentLocale(String defaultLocale) throws CountryNotFoundException {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simCountryIso = manager.getSimCountryIso().toUpperCase();
        String networkCountryIso = manager.getNetworkCountryIso().toUpperCase();
        String localeCountryIso = Locale.getDefault().getCountry().toUpperCase();
        String countryIso;
        if (!TextUtils.isEmpty(simCountryIso)) {
            countryIso = simCountryIso;
        } else if (!TextUtils.isEmpty(simCountryIso)) {
            countryIso = networkCountryIso;
        } else {
            countryIso = localeCountryIso;
        }
        return getCountryByLocale(countryIso, defaultLocale);
    }

    public Country getCountryByLocale(String locale, String defaultLocale) throws CountryNotFoundException {
        List<Country> countries = getCountries();
        Country defaultCountry = null;
        for (Country country : countries) {
            if (TextUtils.equals(country.getShortName(), defaultLocale)) {
                defaultCountry = country;
            }
            if (TextUtils.equals(country.getShortName(), locale)) {
                return country;
            }
        }
        if (defaultCountry != null) {
            return defaultCountry;
        }
        throw new CountryNotFoundException();
    }

    public Country getCountryByCode(int code) throws CountryNotFoundException {
        List<Country> countries = getCountries();
        for (Country country : countries) {
            if (country.getCode() == code) {
                return country;
            }
        }
        throw new CountryNotFoundException();
    }

    public class CountryNotFoundException extends Throwable {
    }
}
