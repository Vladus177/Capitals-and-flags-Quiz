package vladus177.ru.geotest_10;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    final String LOG_TAG = "myLogs";
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    FragmentFlags fragF;
    FragmentCapitals fragC;
    FragmentButtons fragButtons;
    RatesFragment ratesFragment;
    FragmentTransaction fTrans;
    FrameLayout container;
    FrameLayout container2;
    FrameLayout containerRating;
    FrameLayout containerButtons;
    FragmentManager myFragmentManager;
    public String game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (FrameLayout) findViewById(R.id.container);
        container2 = (FrameLayout) findViewById(R.id.container2);
        containerButtons = (FrameLayout) findViewById(R.id.containerButtons);
        myFragmentManager = getFragmentManager();
        game = "medium";
        SQLstart();
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
        fTrans = myFragmentManager.beginTransaction();
        fragF = new FragmentFlags();
        fTrans.replace(R.id.container, fragF).commit();

    }

    public void addFragmentCapitals() {
        fTrans = myFragmentManager.beginTransaction();
        fragC = new FragmentCapitals();
        fTrans.replace(R.id.container, fragC).commit();

    }

    public void addFragmentButtons() {
        fTrans = myFragmentManager.beginTransaction();
        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragButtons = new FragmentButtons();
            fTrans.replace(R.id.containerButtons, fragButtons).commit();
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragButtons = new FragmentButtons();
            fTrans.replace(R.id.container2, fragButtons).commit();
        }
    }

    public void addFragmentRates() {
        fTrans = myFragmentManager.beginTransaction();
        ratesFragment = new RatesFragment();
        fTrans.replace(R.id.container, ratesFragment);
        fTrans.commit();

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

    public void SQLstart() {
        mDatabaseHelper = new DataBaseHelper(this, "mydatabase.db", null, 1);
        SQLiteDatabase sdb;
        sdb = mDatabaseHelper.getReadableDatabase();

    }

}
