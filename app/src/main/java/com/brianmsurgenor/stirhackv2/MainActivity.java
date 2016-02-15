package com.brianmsurgenor.stirhackv2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This is genuinely the worst code ever written. Like really really awful. I started writing
 * it about 3 hours ago, 2 of which I spent eating.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView test;
    private ArrayList<Integer> AIACtextViews, AIACapiLayouts;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ArrayList<String> AIACapis = new ArrayList<>();
        AIACtextViews = new ArrayList<>();
        AIACapiLayouts = new ArrayList<>();
        AIACapis.add("http://dogfish.tech/api/api1/LAX");
        AIACapis.add("http://dogfish.tech/api/api2/FR");
        AIACapis.add("http://dogfish.tech/api/api3/USD");
        AIACapis.add("http://dogfish.tech/api/api4?auth=222&user_id=524549267&count=15");
        AIACapis.add("http://dogfish.tech/api/api5?auth=222&?latlon=51.489935,-0.111237");
        AIACapis.add("http://dogfish.tech/api/api6?token=a99736e36cfaa89ce58fa24d2911eb6b&timeline=dogfishmobile&count=30&result_type=recent");
        AIACapis.add("http://dogfish.tech/api/api7?token=a99736e36cfaa89ce58fa24d2911eb6b&latlon=56.143168,-3.917977");
        AIACapis.add("http://dogfish.tech/api/api7?token=a99736e36cfaa89ce58fa24d2911eb6b&channel=google");

        i = 0;

        AIACtextViews.add(R.id.api1);
        AIACapiLayouts.add(R.id.api1Layout);
        AIACtextViews.add(R.id.api2);
        AIACapiLayouts.add(R.id.api2Layout);
        AIACtextViews.add(R.id.api3);
        AIACapiLayouts.add(R.id.api3Layout);
        AIACtextViews.add(R.id.api4);
        AIACapiLayouts.add(R.id.api4Layout);
        AIACtextViews.add(R.id.api5);
        AIACapiLayouts.add(R.id.api5Layout);
        AIACtextViews.add(R.id.api6);
        AIACapiLayouts.add(R.id.api6Layout);
        AIACtextViews.add(R.id.api7);
        AIACapiLayouts.add(R.id.api7Layout);
        AIACtextViews.add(R.id.api8);
        AIACapiLayouts.add(R.id.api8Layout);

        for(int i=0;i<AIACapis.size();i++) {
            new HttpAsyncTask().execute(AIACapis.get(i));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        startActivity(new Intent(MainActivity.this, MainActivity.class));
        return true;

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this,GraphActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void display(String JSON) {

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(120,120);
        layoutParams.setMargins(10, 10, 10, 10);

        if(JSON.contains("\"response_code\":200")) {
            test = (TextView) findViewById(AIACtextViews.get(i));
            int x = i+1;
            String[] ahhh = JSON.split(" ");
            test.setText("API " + x + " OK\nResponse Time: " + ahhh[0] + "ms");

            RelativeLayout greenDot = new RelativeLayout(this);
            if(Integer.parseInt(ahhh[0]) > 900) {
                greenDot.setBackgroundResource(R.drawable.orange);
            } else {
                greenDot.setBackgroundResource(R.drawable.green);
            }
            greenDot.setGravity(Gravity.CENTER);

            LinearLayout layout = (LinearLayout) findViewById(AIACapiLayouts.get(i));
            layout.addView(greenDot, layoutParams);

            i++;
        } else {
            test = (TextView) findViewById(AIACtextViews.get(i));
            int x = i+1;
            test.setText("API " + x + " NOT OK");

            RelativeLayout greenDot = new RelativeLayout(this);
            greenDot.setBackgroundResource(R.drawable.red);
            greenDot.setGravity(Gravity.CENTER);

            LinearLayout layout = (LinearLayout) findViewById(AIACapiLayouts.get(i));
            layout.addView(greenDot, layoutParams);
            i++;
        }

    }

    private String getJSON(String urls) {

        InputStream inputStream = null;
        String result = "";

        try{
            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse httpResponse = httpClient.execute(new HttpGet(urls));

            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
            result = "error";
        }

//        Log.d("HIYA",result);
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        long startTime = 0;

        @Override
        protected String doInBackground(String... urls) {

            startTime = System.currentTimeMillis();
            return getJSON(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            startTime = System.currentTimeMillis() - startTime;

            display(startTime + " " + result);
        }
    }
}
