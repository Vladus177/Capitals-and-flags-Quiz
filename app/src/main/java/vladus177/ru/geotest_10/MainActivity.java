package vladus177.ru.geotest_10;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;




public class MainActivity extends FragmentActivity {
    final String LOG_TAG = "myLogs";
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    FragmentFlags fragF;
    FragmentCapitals fragC;
    FragmentButtons fragButtons;
    RatesFragment ratesFragment;
    FrameLayout container;
    FrameLayout container2;
    FrameLayout containerButtons;
    public String game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        mDatabaseHelper = new DataBaseHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        container = (FrameLayout) findViewById(R.id.container);
        container2 = (FrameLayout) findViewById(R.id.container2);
        containerButtons = (FrameLayout) findViewById(R.id.containerButtons);
        game = "medium";
        mDatabaseHelper.onCreate(mSqLiteDatabase);
        addFragmentButtons();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void addFragmentFlags() {
        fragF = new FragmentFlags();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragF).commit();

    }

    public void addFragmentCapitals() {
        fragC = new FragmentCapitals();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragC).commit();

    }

    public void addFragmentButtons() {

        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragButtons = new FragmentButtons();
            getSupportFragmentManager().beginTransaction().replace
                    (R.id.containerButtons, fragButtons).commit();
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragButtons = new FragmentButtons();
            getSupportFragmentManager().beginTransaction().replace
                    (R.id.container2, fragButtons).commit();
        }
    }

    public void addFragmentRates() {
        ratesFragment = new RatesFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.container, ratesFragment).commit();

    }

    public void closeFragmentCapitals() {
        if (fragC != null) {
            fragC.getFragmentManager().beginTransaction().remove(fragC).commit();
        }
    }

    public void closeFragmentFlags() {
        if (fragF != null) {
            fragF.getFragmentManager().beginTransaction().remove(fragC).commit();
        }
    }

}
