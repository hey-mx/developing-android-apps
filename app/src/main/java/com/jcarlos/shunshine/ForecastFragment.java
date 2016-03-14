package com.jcarlos.shunshine;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class ForecastFragment extends Fragment {

    String[] forecast;
    ArrayAdapter<String> mForecastAdapter;
    final String TAG_NAME = this.getClass().getSimpleName();

    public ForecastFragment() {
        forecast = new String[] {
                "Today - Sunny - 88 / 63",
                "Tomorrow - Foggy - 70 / 46",
                "Weds - Cloudy - 72 / 63",
                "Thurs - Rainy - 64 / 51",
                "Fri - Foggy - 70 / 46",
                "Sat - Sunny - 76 / 68"
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        ArrayList<String> forecastList = new ArrayList<String>(Arrays.asList(forecast));
        mForecastAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview, forecastList);
        ListView listView = (ListView)rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchWeatherTask task = new FetchWeatherTask();
            try {
                forecast = task.execute(72000, 7).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchWeatherTask extends AsyncTask<Integer, Void, String[]> {
        @Override
        protected String[] doInBackground(Integer... params) {
            String[] result = null;
            Integer count = params.length;
            Log.d(this.getClass().getSimpleName(), count.toString());
            if (params.length > 1) {
                int numOfDays = params[1];
                result = UtilsHelper.GetForecastFromApi(params[0], numOfDays);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] forecastArray) {
            if (forecastArray != null) {
                mForecastAdapter.clear();
                for (String forecastStr : forecastArray) {
                    mForecastAdapter.add(forecastStr);
                }
            }
        }
    }
}
