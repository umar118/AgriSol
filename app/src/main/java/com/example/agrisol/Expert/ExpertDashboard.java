package com.example.agrisol.Expert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.agrisol.Login.AboutUs;
import com.example.agrisol.Login.DeleteAccountDailogFragment;
import com.example.agrisol.Login.Login;
import com.example.agrisol.Market.MarketPriceList;
import com.example.agrisol.R;
import com.example.agrisol.User.Fragments.UserHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpertDashboard extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView  navigationView;
    private DatabaseReference UsersRef;
    private CircleImageView ProfileImage;
    private TextView ProfileName;
    private StorageReference UserProfileImageRef;
    FirebaseAuth mAuth;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expert_dashboard);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("Val", 2);//2 for tasker
        editor.apply();

        loadFragment(new UserHome());
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("AgriSolutions");
        BottomNavigationView navigation1 = findViewById(R.id.navigation1);
        navigation1.setOnNavigationItemSelectedListener((bottom));

        drawerLayout =findViewById(R.id.drawerlayout6);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView =(NavigationView) findViewById(R.id.navigation_view6);
        navigationView.setNavigationItemSelectedListener(this);
        ProfileImage =(CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.header_Profile);
        ProfileName=navigationView.getHeaderView(0).findViewById(R.id.header_name);

        DrawableProfile();
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container1, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        switch (menuItem.getItemId()){
            case R.id.nav_update:
                startActivity(new Intent(getApplicationContext(), UpdateExpertProfile.class));
                finish();
                break;
            case  R.id.nav_signout:
                mAuth.signOut();
                Toast.makeText(getApplicationContext(),"Sign out Successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                SharedPreferences spreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor spreferencesEditor = spreferences.edit();
                spreferencesEditor.remove("Expert");
                spreferencesEditor.commit();
                break;
            case R.id.nav_delete:
                DeleteAccount();
                break;
            case R.id.menu_about_us:
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
                finish();
                break;

        }

        drawerLayout.closeDrawer( GravityCompat.START);
        return true;


    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottom =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;

            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    fragment = new UserHome();
                    break;

                case R.id.navigation_expert:
                    fragment = new ExpertDetials();
                    break;

                case R.id.navigation_market:
                    fragment = new MarketPriceList();
                    break;

                case R.id.navigation_profile:
                    fragment = new ExpertProfile();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
            }

            return loadFragment(fragment);

        }
    };
    private void  DrawableProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Expert").child(user.getUid());
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Expert Profile Images");
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("expert_Profile")) {
                        String userprofileimage = dataSnapshot.child("expert_Profile").getValue().toString();
                        Picasso.get().load(userprofileimage).placeholder(R.drawable.profile).into(ProfileImage);
                    }
                    if (dataSnapshot.hasChild("expert_FullName")) {
                        String fullname = dataSnapshot.child("expert_FullName").getValue().toString();
                        ProfileName.setText(fullname);
                    }
                    else {
                        Toast.makeText( getApplicationContext(), "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private  void  DeleteAccount(){
        final   FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential("user@example.com", "password1234");
        FragmentManager manager =getSupportFragmentManager();
        DeleteAccountDailogFragment myDialogFragment =new DeleteAccountDailogFragment();
        myDialogFragment.show(manager,"DeleteAccountDialogFragment");
        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText( getApplicationContext(), "User account deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplication(),Login.class));
                        }
                    }
                });
            }
        });
    }


}
