package com.example.sumit.srms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class AdminDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Declaration Part
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_drawer);

        //Screen Rotation Disable Code
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Drawer Code
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new Home()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        //Drawer Code End

        //Check Internet Connection
        checkInternet();
    }

    //Check If Internet Is Connected or Not!!
    public void checkInternet()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            new SweetAlertDialog(AdminDrawer.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("SRMS")
                    .setContentText("Welcome To Admin Panel :)")
                    .setConfirmText("Thank You!")
                    .show();
        }
        else
        {
            new SweetAlertDialog(AdminDrawer.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Internet (:")
                    .setContentText("Please Connect Your Device With Internet For Get Better Experience of This App :)")
                    .setConfirmText("Got It!")
                    .show();
        }
    }

    //Drawer Methods
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit from SRMS?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AdminDrawer.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new Home()).commit();
                break;

            case R.id.nav_course:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new CourseMgmt()).commit();
                break;

            case R.id.nav_semester:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new SemesterMgmt()).commit();
                break;

            case R.id.nav_batch:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new BatchMgmt()).commit();
                break;

            case R.id.nav_division:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new DivisionMgmt()).commit();
                break;

            case R.id.nav_subject:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new SubjectMgmt()).commit();
                break;

            case R.id.nav_exam:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new ExamMgmt()).commit();
                break;

            case R.id.nav_manual_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new RegistrationActivity()).commit();
                break;

            case R.id.admin_nav_update_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new UpdateProfile()).commit();
                break;

            case R.id.nav_semester_update:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new SemesterUpdate()).commit();
                break;

            case R.id.nav_manage_faculty:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new FacultyMgmt()).commit();
                break;

            case R.id.nav_manual_result:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new ManualResult()).commit();
                break;

            case R.id.nav_reports:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new Reports()).commit();
                break;

            case R.id.nav_about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container, new AboutUs()).commit();
                break;

            case R.id.nav_logout:
                SessionMgmt sessionMgmt = new SessionMgmt(AdminDrawer.this);
                sessionMgmt.remove_session();
                Intent intent = new Intent(AdminDrawer.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Drawer Methods End
}