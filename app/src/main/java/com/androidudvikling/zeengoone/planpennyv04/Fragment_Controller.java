package com.androidudvikling.zeengoone.planpennyv04;

import android.content.Context;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidudvikling.zeengoone.planpennyv04.entities.Project;
import com.androidudvikling.zeengoone.planpennyv04.logic.DataLogic;
import com.androidudvikling.zeengoone.planpennyv04.logic.FileHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Fragment_Controller extends AppCompatActivity {
    private ArrayList<String> projekt_liste = new ArrayList<>();
    private ArrayList<String> tabMaaneder = new ArrayList<String>();
    private ListView projekt_liste_view;
    private DrawerLayout pennydrawerLayout;
    private ActionBarDrawerToggle penny_Projekt_Drawer_Toggle;
    private DataLogic dc = new DataLogic();
    private Calendar cal = new GregorianCalendar();
    private int currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_controller);

        // Lav default projekter at teste på
        dc.addDefaultProjects();
        // Gør listen klar og smid den i projekt listen
        for(Project p:dc.getProjects()){
            projekt_liste.add(p.getTitle());
        }
        projekt_liste_view = (ListView) findViewById(R.id.penny_projekt_drawer_list);
        projekt_liste_view.setAdapter(new ArrayAdapter<String>(this,
                R.layout.penny_drawer_listview_item, projekt_liste));

        // Opsæt actionbar
        getSupportActionBar().setLogo(R.drawable.penny_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Sæt Drawer op
        pennydrawerLayout = (DrawerLayout) findViewById(R.id.penny_projekt_drawer_layout);
        penny_Projekt_Drawer_Toggle = new ActionBarDrawerToggle(
                this,
                pennydrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);
        pennydrawerLayout.setDrawerListener(penny_Projekt_Drawer_Toggle);

        // Onclick listener til projektlistemenuen
        projekt_liste_view.setOnItemClickListener(new DrawerItemClickListener());


        // Læg to års måneder ind i tab-listen
        populateTabList(24);
        // Toast.makeText(Fragment_Controller.this, ((TextView) view).getText(), Toast.LENGTH_LONG).show();
        pennydrawerLayout.closeDrawer(projekt_liste_view);
        // Få fat i ViewPager og set dens pageradapter så den kan vise items

        // Sender tabLayoutet videre til viewpageren
        Fragment_Gantt.beregnMaanedOgAar(0);

    }

    /*private void startKategorier(){
        for(int i = 0;i < 30;i++){
            projekter_test.add("kategori" + i);
        }
    }*/
    private Calendar addMonth(){
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, currentMonth++);
        return cal;
    }
    private void populateTabList(int years){
        int month;
        int year;
        for(int i = 0;i < years;i++){
            month = addMonth().get(Calendar.MONTH);
            month = month+1;
            year = cal.get(Calendar.YEAR);
            tabMaaneder.add(new SimpleDateFormat("MMM").format(cal.getTime()));
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.penny_projekt_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        penny_Projekt_Drawer_Toggle.syncState();
    }*/

    @Override
    public void onPause(){
        super.onPause();
        //fh.saveAllData(dc.getProjects());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //fh.saveAllData(dc.getProjects());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        penny_Projekt_Drawer_Toggle.onConfigurationChanged(newConfig);
        penny_Projekt_Drawer_Toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (penny_Projekt_Drawer_Toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Fragment_Gantt.setProject(dc.getProjects().get(position).getTitle());
            Fragment_Gantt.setProjectNumber(position);
            final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            final PennyFragmentPagerAdapter adapter = new PennyFragmentPagerAdapter(getSupportFragmentManager(),
                    Fragment_Controller.this, dc);
            adapter.setTabFields(tabMaaneder);
            viewPager.setAdapter(adapter);
            final TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Fragment_Gantt.beregnMaanedOgAar(tab.getPosition());
                    viewPager.setCurrentItem(tab.getPosition());
                    System.out.println("fragment controller tab status: " + tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

}
