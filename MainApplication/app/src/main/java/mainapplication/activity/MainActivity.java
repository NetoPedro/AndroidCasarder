package mainapplication.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import mainapplication.R;

public class MainActivity extends FragmentActivity {

    private static int page;
    HomeActivity home = HomeActivity.newInstance(this);
    MyBookingsActivity myBookings = new MyBookingsActivity();
    HistoryActivity history = new HistoryActivity();
    Button login;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    page = 1;

                    manager.beginTransaction().replace(R.id.fragments_container, home)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                    return true;
                case R.id.navigation_my_bookings:
                    page = 2;

                    if(getSharedPreferences("userPreferences", MODE_PRIVATE).contains("token")) {
                        manager.beginTransaction().replace(R.id.fragments_container, myBookings)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                    }else{
                        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                        navigation.setSelectedItemId(R.id.navigation_home);
                        Toast.makeText(MainActivity.this, "Please Log In", Toast.LENGTH_LONG).show();

                    }

                    return true;
                case R.id.navigation_history:
                    page = 3;

                    if(getSharedPreferences("userPreferences", MODE_PRIVATE).contains("token")) {
                    manager.beginTransaction().replace(R.id.fragments_container, history)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                    }else {
                        BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.navigation);
                        navigation.setSelectedItemId(R.id.navigation_home);
                        Toast.makeText(MainActivity.this, "Please Log In", Toast.LENGTH_LONG).show();
                    }
                    return true;
                default:
                    page = 1;
                    manager.beginTransaction().replace(R.id.fragments_container, home)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                    return true;
            }

        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        home.setmParent(this);
        myBookings.setParent(this);
        history.setParent(this);
        BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        login = (Button) findViewById(R.id.login_button);
        setLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLogin();
        home.setmParent(this);
        myBookings.setParent(this);
        history.setParent(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        FragmentManager manager = getFragmentManager();
        switch(page){
            case 1:
                navigation.setSelectedItemId(R.id.navigation_home);

                manager.beginTransaction().replace(R.id.fragments_container, home)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;
            case 2:
                navigation.setSelectedItemId(R.id.navigation_my_bookings);

                manager.beginTransaction().replace(R.id.fragments_container, myBookings)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;
            case 3:

                navigation.setSelectedItemId(R.id.navigation_history);
                manager.beginTransaction().replace(R.id.fragments_container, history)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;
            default:
                page = 1;
                navigation.setSelectedItemId(R.id.navigation_home);
                manager.beginTransaction().replace(R.id.fragments_container, home)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setLogin();
    }




    public void setLogin(){
        if(getSharedPreferences("userPreferences", MODE_PRIVATE).contains("token")){
            login.setText("Logout");
            login.setTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.black, null)));
            login.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, null)));
            login.setElevation(0);

        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login.getText() == "Logout"){
                    getSharedPreferences("userPreferences", MODE_PRIVATE).edit().remove("username")
                            .remove("password")
                            .remove("token")
                            .remove("expiration").commit();
                    login.setText("Login");
                    LoginManager.getInstance().logOut();
                    login.setTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.white, null)));
                    login.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent, null)));
                    login.setElevation(1);
                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
    }
}
