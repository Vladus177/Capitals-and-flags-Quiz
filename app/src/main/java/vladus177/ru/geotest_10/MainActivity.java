package vladus177.ru.geotest_10;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends FragmentActivity {
    final String LOG_TAG = "myLogs";
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    CapitalsMedium CM;
    CapitalsHard CH;
    FlagsMedium FM;
    FlagsHard FH;
    Practice PR;
    FirstPage fragmentFirst;
    RatesFragment ratesFragment;
    FrameLayout container;
    FrameLayout container2;
    public String game;
    final int MENU_EN = 1;
    final int MENU_ES = 2;
    final int MENU_RU = 3;
    Locale myLocale;
    AlertDialog.Builder AD = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        mDatabaseHelper = new DataBaseHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        container = (FrameLayout) findViewById(R.id.container);
        container2 = (FrameLayout) findViewById(R.id.container2);
        game = "medium";
        mDatabaseHelper.onCreate(mSqLiteDatabase);
        addFirstPage();
        mDatabaseHelper.close();
        mSqLiteDatabase.close();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, MENU_EN, 1, "English");
        menu.add(0, MENU_ES, 2, "Español");
        menu.add(0, MENU_RU, 3, "Русский");

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabaseHelper != null) {
            mDatabaseHelper.close();
        }
        if (mSqLiteDatabase != null) {
            mSqLiteDatabase.close();
        }

    }

    @Override
    public void onBackPressed() {
        if (CM != null || CH != null || FM != null || FH != null || PR != null || ratesFragment != null) {
            CM = null;
            CH = null;
            FM = null;
            FH = null;
            PR = null;
            ratesFragment = null;
            addFirstPage();
        } else {
            Dialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        switch (item.getItemId()) {
            case MENU_EN:
                setLocale("en");
                Toast.makeText(this, "Locale in English !", Toast.LENGTH_LONG).show();
                break;

            case MENU_ES:
                setLocale("es");
                Toast.makeText(this, "Locale in Spanish !", Toast.LENGTH_LONG).show();
                break;

            case MENU_RU:
                setLocale("ru");
                Toast.makeText(this, "Locale in Russian !", Toast.LENGTH_LONG).show();
                break;


        }

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/


        return super.onOptionsItemSelected(item);
    }


    public void addFragmentCM() {
        CM = new CapitalsMedium();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, CM).commit();

    }

    public void addFragmentCH() {
        CH = new CapitalsHard();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, CH).commit();

    }

    public void addFragmentFM() {
        FM = new FlagsMedium();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, FM).commit();

    }

    public void addFragmentFH() {
        FH = new FlagsHard();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, FH).commit();

    }

    public void addFragmentPR() {
        PR = new Practice();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, PR).commit();

    }

    public void addFirstPage() {
        fragmentFirst = new FirstPage();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.container, fragmentFirst).commit();

    }

    public void addFragmentRates() {
        ratesFragment = new RatesFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.container, ratesFragment).commit();

    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    public void Dialog() {
        AD = new AlertDialog.Builder(this);
        String title = getString(R.string.exit2);
        String message = getString(R.string.exit);
        AD.setTitle(title);
        AD.setMessage(message);
        AD.setPositiveButton(getString(R.string.okButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();


            }
        });

        AD.setNegativeButton(getString(R.string.cancelButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                addFirstPage();

            }
        });

        AD.show();
    }

}
