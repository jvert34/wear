package com.jean_philippe.projetwear;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Ferdousur Rahman Sarker on 3/17/2018.
 */

public class TaskHome extends AppCompatActivity {
    public static final int TASK_POSTPONE_MINUTES = 8;
    public static String KEY_ID = "id";
    public static String KEY_CATEGORY = "category";
    public static String KEY_TASK = "task";
    public static String KEY_DATE = "date";
    public static String KEY_DESCRIPTION = "description";
    public static String KEY_HOUR = "hour";
    Activity activity;
    TaskDBHelper mydb;
    NoScrollListView taskListToday, taskListTomorrow, taskListUpcoming;
    NestedScrollView scrollView;
    ProgressBar loader;
    ArrayList<HashMap<String, String>> tomorrowList = new ArrayList<HashMap<String, String>>();
    TextView todayText, tomorrowText, upcomingText;
    ArrayList<HashMap<String, String>> todayList = new ArrayList<HashMap<String, String>>();

    ArrayList<HashMap<String, String>> upcomingList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_home);

        activity = TaskHome.this;
        mydb = new TaskDBHelper(activity);
        Toolbar toolbar = findViewById(R.id.toolbarMain);

        setSupportActionBar(toolbar);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        loader = (ProgressBar) findViewById(R.id.loader);
        taskListToday = (NoScrollListView) findViewById(R.id.taskListToday);
        taskListTomorrow = (NoScrollListView) findViewById(R.id.taskListTomorrow);
        taskListUpcoming = (NoScrollListView) findViewById(R.id.taskListUpcoming);

        todayText = (TextView) findViewById(R.id.todayText);
        tomorrowText = (TextView) findViewById(R.id.tomorrowText);
        upcomingText = (TextView) findViewById(R.id.upcomingText);

        //Button btnCat = (Button)findViewById(R.id.buttonCat);

        /*btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TaskByCategory.class);
                startActivity(i);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResult(newText);
                return false;
            }
        });


        return true;
    }


    protected void filterResult(String pattren) {


        ArrayList<HashMap<String, String>> todays = new ArrayList<>();
        ArrayList<HashMap<String, String>> tomarrow = new ArrayList<>();
        ArrayList<HashMap<String, String>> upcoming = new ArrayList<>();


        for (HashMap<String, String> item : todayList) {

            if (Objects.requireNonNull(item.get(TaskHome.KEY_CATEGORY)).toLowerCase().contains(pattren)) {
                todays.add(item);

            } else if (Objects.requireNonNull(item.get(TaskHome.KEY_DATE)).contains(pattren)) {
                todays.add(item);

            }


        }

        for (HashMap<String, String> item : tomorrowList) {

            if (Objects.requireNonNull(item.get(TaskHome.KEY_CATEGORY)).toLowerCase().contains(pattren)) {
                tomarrow.add(item);

            } else if (Objects.requireNonNull(item.get(TaskHome.KEY_DATE)).contains(pattren)) {
                tomarrow.add(item);

            }

        }
        for (HashMap<String, String> item : upcomingList) {

            if (Objects.requireNonNull(item.get(TaskHome.KEY_CATEGORY)).toLowerCase().contains(pattren)) {
                upcoming.add(item);

            } else if (Objects.requireNonNull(item.get(TaskHome.KEY_DATE)).contains(pattren)) {
                upcoming.add(item);

            }

        }

        loadListView(taskListToday, todays);
        loadListView(taskListTomorrow, tomarrow);
        loadListView(taskListUpcoming, upcoming);


        if (todays.size() > 0) {
            todayText.setVisibility(View.VISIBLE);
        } else {
            todayText.setVisibility(View.GONE);
        }

        if (tomarrow.size() > 0) {
            tomorrowText.setVisibility(View.VISIBLE);
        } else {
            tomorrowText.setVisibility(View.GONE);
        }

        if (upcoming.size() > 0) {
            upcomingText.setVisibility(View.VISIBLE);
        } else {
            upcomingText.setVisibility(View.GONE);
        }


        loader.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add) {
            openAddTask();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openAddTask() {
        Intent i = new Intent(this, AddTask.class);
        startActivity(i);
    }


    public void populateData() {
        mydb = new TaskDBHelper(activity);
        scrollView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        LoadTask loadTask = new LoadTask();
        loadTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        populateData();

    }

    public void loadDataList(Cursor cursor, ArrayList<HashMap<String, String>> dataList) {
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                HashMap<String, String> mapToday = new HashMap<String, String>();
                mapToday.put(KEY_ID, cursor.getString(0));
                mapToday.put(KEY_CATEGORY, cursor.getString(1));
                mapToday.put(KEY_TASK, cursor.getString(2));
                mapToday.put(KEY_DATE, Function.Epoch2DateString(cursor.getString(3), "dd-MM-yyyy"));
                mapToday.put(KEY_DESCRIPTION, cursor.getString(4));
                mapToday.put(KEY_HOUR, cursor.getString(5));
                dataList.add(mapToday);
                cursor.moveToNext();
            }
        }
    }

    public void loadListView(ListView listView, final ArrayList<HashMap<String, String>> dataList) {
        ListTaskAdapter adapter = new ListTaskAdapter(activity, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(activity, AddTask.class);
                i.putExtra("isUpdate", true);
                i.putExtra("id", dataList.get(+position).get(KEY_ID));
                startActivity(i);
            }
        });
    }

    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            todayList.clear();
            tomorrowList.clear();
            upcomingList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            /* ===== TODAY ========*/
            Cursor today = mydb.getDataToday();
            loadDataList(today, todayList);
            /* ===== TODAY ========*/

            /* ===== TOMORROW ========*/
            Cursor tomorrow = mydb.getDataTomorrow();
            loadDataList(tomorrow, tomorrowList);
            /* ===== TOMORROW ========*/

            /* ===== UPCOMING ========*/
            Cursor upcoming = mydb.getDataUpcoming();
            loadDataList(upcoming, upcomingList);
            /* ===== UPCOMING ========*/

            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {


            loadListView(taskListToday, todayList);
            loadListView(taskListTomorrow, tomorrowList);
            loadListView(taskListUpcoming, upcomingList);


            if (todayList.size() > 0) {
                todayText.setVisibility(View.VISIBLE);
            } else {
                todayText.setVisibility(View.GONE);
            }

            if (tomorrowList.size() > 0) {
                tomorrowText.setVisibility(View.VISIBLE);
            } else {
                tomorrowText.setVisibility(View.GONE);
            }

            if (upcomingList.size() > 0) {
                upcomingText.setVisibility(View.VISIBLE);
            } else {
                upcomingText.setVisibility(View.GONE);
            }


            loader.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }
}
