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
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


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
    RadioGroup radioGroup;
    RadioButton radioButtonEasy;
    RadioButton radioButtonMedium;
    RadioButton radioButtonHard;
    public String game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fragF = new FragmentFlags();
        //fragC = new FragmentCapitals();
        //fragButtons = new FragmentButtons();
        //ratesFragment = new RatesFragment();
        container = (FrameLayout) findViewById(R.id.container);
        container2 = (FrameLayout) findViewById(R.id.container2);
        containerButtons = (FrameLayout) findViewById(R.id.containerButtons);
        containerRating = (FrameLayout) findViewById(R.id.containerRating);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonEasy = (RadioButton) findViewById(R.id.radioButtonEasy);
        radioButtonMedium = (RadioButton) findViewById(R.id.radioButtonMedium);
        radioButtonHard = (RadioButton) findViewById(R.id.radioButtonHard);
        myFragmentManager = getFragmentManager();
        game = "medium";
        SQLstart();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radioButtonEasy:
                        game = "easy";
                        restartFragment();
                        break;
                    case R.id.radioButtonMedium:
                        game = "medium";
                        restartFragment();
                        break;
                    case R.id.radioButtonHard:
                        game = "hard";
                        restartFragment();
                        break;
                    default:
                        game = "medium";
                        break;
                }


            }
        });
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
        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragF = new FragmentFlags();
            fTrans.replace(R.id.container, fragF);
            fTrans.commit();
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragF = new FragmentFlags();
            fTrans.replace(R.id.container2, new FragmentFlags());
            fTrans.commit();
        }
    }

    public void addFragmentCapitals() {
        fTrans = myFragmentManager.beginTransaction();
        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragC = new FragmentCapitals();
            fTrans.replace(R.id.container, fragC);
            fTrans.commit();
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragC = new FragmentCapitals();
            fTrans.replace(R.id.container, new FragmentCapitals());
            fTrans.commit();
        }

    }

    public void addFragmentButtons() {
        fTrans = myFragmentManager.beginTransaction();
        fragButtons = new FragmentButtons();
        fTrans.replace(R.id.containerButtons, fragButtons);
        fTrans.commit();
    }

    public void addFragmentRates() {
        fTrans = myFragmentManager.beginTransaction();
        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ratesFragment = new RatesFragment();
            fTrans.replace(R.id.container, new RatesFragment());
            fTrans.commit();
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ratesFragment = new RatesFragment();
            fTrans.replace(R.id.containerRating, new RatesFragment());
            fTrans.commit();
        }
    }

    public String getLevel() {
        return game;
    }

    public void restartFragment() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof FragmentCapitals) {
            fragment.getFragmentManager().beginTransaction().remove(this.fragC).commit();
        } else if (fragment instanceof FragmentFlags) {
            fragment.getFragmentManager().beginTransaction().remove(this.fragF).commit();
        }
    }

    public void SQLstart() {
        mDatabaseHelper = new DataBaseHelper(this, "mydatabase.db", null, 1);
        SQLiteDatabase sdb;
        sdb = mDatabaseHelper.getReadableDatabase();

    }


}
