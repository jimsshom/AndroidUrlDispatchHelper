package com.jimsshom.androidurldispatchhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

public class RedirectActivity extends AppCompatActivity {
    private String currentDefaultBrowser = "";
    private String currentSpecialBrowser = "";

    private HashSet<String> rule1Set = new HashSet<String>();
    private HashSet<String> rule2Set = new HashSet<String>();
    private HashSet<String> rule3Set = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        loadFromPersistentStore();

        Intent intent = getIntent();
        Uri uri = intent.getData();

        String url = uri.toString();
        TextView textView = findViewById(R.id.urlPlaceHolderTextView);
        textView.setText(url);

        initRule();
        if (isSpecialUrl(uri)) {
            redirectUrl(currentSpecialBrowser, uri);
        } else {
            redirectUrl(currentDefaultBrowser, uri);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    private void initRule() {
        initRule1();
        initRule2();
        initRule3();
    }

    private void initRule2() {
        HashSet<String> newRule2Set = new HashSet<String>();
        try {
            InputStream is = getResources().openRawResource(R.raw.rule2);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                newRule2Set.add(line.trim());
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                newRule2Set.add(line.trim());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rule2Set = newRule2Set;
            System.out.println(rule2Set.size());
        }
    }

    private void initRule1() {
        HashSet<String> newRule1Set = new HashSet<String>();
        try {
            InputStream is = getResources().openRawResource(R.raw.rule1);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                newRule1Set.add(line.trim());
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                newRule1Set.add(line.trim());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rule1Set = newRule1Set;
            System.out.println(rule1Set.size());
        }
    }

    private void initRule3() {
        HashSet<String> newRule3Set = new HashSet<String>();
        try {
            InputStream is = getResources().openRawResource(R.raw.rule3);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                newRule3Set.add(line.trim());
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                newRule3Set.add(line.trim());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rule3Set = newRule3Set;
            System.out.println(rule3Set.size());
        }
    }

    private void loadFromPersistentStore() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        currentDefaultBrowser = preferences.getString(Constants.KEY_DEFAULT_BROWSER, "");
        currentSpecialBrowser = preferences.getString(Constants.KEY_SPECIAL_BROWSER, "");
    }

    private void redirectUrl(String packageName, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage(packageName);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            //intent.setPackage(null);
            //startActivity(intent);
            System.out.println("Cannot find activity! packageName:" + packageName);
        }
    }

    private boolean isSpecialUrl(Uri uri) {
        if (isIp(uri)) {
            if (isLocalAddress(uri.getHost())) {
                System.out.println("localAddress: " + uri.getHost());
                return false;
            }
            String country = queryGeoIp(uri.getHost());
            if ("CN".equals(country) || "Unknown".equals(country)) {
                return false;
            }
            return true;
        }
        if (rule1(uri)) {
            return true;
        }
        if (rule2(uri)) {
            return true;
        }
        if (rule3(uri)) {
            return true;
        }

        return false;
    }

    private boolean isLocalAddress(String ip) {
        if ("127.0.0.1".equals(ip) || "0.0.0.0".equals(ip)) {
            return true;
        }
        /**
         * 10.0.0.0/8：10.0.0.0～10.255.255.255
         * 172.16.0.0/12：172.16.0.0～172.31.255.255
         * 192.168.0.0/16：192.168.0.0～192.168.255.255
         *
         * https://blog.csdn.net/weixin_38626376/article/details/74530583
         */
        //TODO: 判断局域网地址
        try {
            byte[] addr = InetAddress.getByName(ip).getAddress();

            final byte b0 = addr[0];
            final byte b1 = addr[1];
            //10.x.x.x/8
            final byte SECTION_1 = 0x0A;
            //172.16.x.x/12
            final byte SECTION_2 = (byte) 0xAC;
            final byte SECTION_3 = (byte) 0x10;
            final byte SECTION_4 = (byte) 0x1F;
            //192.168.x.x/16
            final byte SECTION_5 = (byte) 0xC0;
            final byte SECTION_6 = (byte) 0xA8;
            switch (b0) {
                case SECTION_1:
                    return true;
                case SECTION_2:
                    if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                        return true;
                    }
                    break;
                case SECTION_5:
                    switch (b1) {
                        case SECTION_6:
                            return true;
                    }
                default:
                    return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String queryGeoIp(String ip) {
        long ts = System.currentTimeMillis();
        InputStream inputStream = null;
        DatabaseReader reader = null;
        try {
            AssetManager assets = getAssets();
            inputStream = assets.open("GeoLite2-Country.mmdb");
            reader = new DatabaseReader.Builder(inputStream).build();
            CountryResponse response = reader.country(InetAddress.getByName(ip));
            Country country = response.getCountry();
            System.out.println("GeoIp: " + ip + " " + country.getIsoCode());
            return country.getIsoCode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("GeoIp rt: " + String.valueOf(System.currentTimeMillis() - ts));
        }
        return "Unknown";
    }

    private boolean isIp(Uri uri) {
        String host = uri.getHost();
        for (char c : host.toCharArray()) {
            if (c == '.') {
                continue;
            }
            if (c >= '0' && c <= '9') {
                continue;
            }
            return false;
        }
        System.out.println("IsIp: " + host);
        return true;
    }

    private boolean rule2(Uri uri) {
        String host = uri.getHost();
        System.out.println("rule2: " + host);

        for (String keyword : rule2Set) {
            if (host.indexOf(keyword) >= 0) {
                return true;
            }
        }
        return false;
    }

    private boolean rule1(Uri uri) {
        String host = uri.getHost();
        System.out.println("rule1: " + host);
        String[] split = host.split("\\.", -1);
        String testPattern = "." + split[split.length-1];
        if (rule1Set.contains(testPattern)) {
            return true;
        }
        return false;
    }

    private boolean rule3(Uri uri) {
        String host = uri.getHost();
        System.out.println("rule3: " + host);
        String[] split = host.split("\\.", -1);
        for (String s : split) {
            System.out.println(s);
        }
        System.out.println("===================================================");
        int i = split.length-1;
        String testPattern = split[i];
        i -= 1;
        while (i >= 0) {
            testPattern = split[i] + "." + testPattern;
            System.out.println(testPattern);
            if (rule3Set.contains(testPattern)) {
                return true;
            }
            i -= 1;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Constants.KEY_DEFAULT_BROWSER, currentDefaultBrowser);
        outState.putString(Constants.KEY_SPECIAL_BROWSER, currentSpecialBrowser);
        outState.putSerializable("rule1Set", rule1Set);
        outState.putSerializable("rule2Set", rule2Set);
        outState.putSerializable("rule3Set", rule3Set);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        currentDefaultBrowser = savedInstanceState.getString(Constants.KEY_DEFAULT_BROWSER);
        currentSpecialBrowser = savedInstanceState.getString(Constants.KEY_SPECIAL_BROWSER);

        rule1Set = (HashSet<String>) savedInstanceState.getSerializable("rule1Set");
        rule2Set = (HashSet<String>) savedInstanceState.getSerializable("rule2Set");
        rule3Set = (HashSet<String>) savedInstanceState.getSerializable("rule3Set");

        super.onRestoreInstanceState(savedInstanceState);
    }
}
