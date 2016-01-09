package vladus177.ru.geotest_10;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Владислав on 21.11.2015.
 */
public class RatesFragment extends android.support.v4.app.Fragment {
    final String LOG_TAG = "myLogs";
    public DataBaseHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> games = new ArrayList<>();
    ArrayList<Integer> scores = new ArrayList<>();
    Cursor cursor;
    String orderBy;
    Context context;
    AlertDialog.Builder AD = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment3Rates = inflater.inflate(R.layout.activity_rates,
                container, false);
        orderBy = "playerScores DESC";
        Button deleteAll = (Button) fragment3Rates.findViewById(R.id.buttonDeleteAll);
        Button buttonBack = (Button) fragment3Rates.findViewById(R.id.buttonBack);
        //Button addBase = (Button) fragment3Rates.findViewById(R.id.addBase);
        //Button deleteBase = (Button) fragment3Rates.findViewById(R.id.deleteBase);
        mDatabaseHelper = new DataBaseHelper(getActivity(), "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        context = getActivity();
        SQLread();
        LinearLayout linLayout = (LinearLayout) fragment3Rates.findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getActivity().getLayoutInflater();
        for (int i = 0; i < names.size(); i++) {
            Log.d("myLogs", "i = " + i);
            View item = ltInflater.inflate(R.layout.list_item, linLayout, false);
            TextView tvName = (TextView) item.findViewById(R.id.tvName);
            tvName.setText(names.get(i));
            TextView tvPosition = (TextView) item.findViewById(R.id.tvLevel);
            tvPosition.setText(getString(R.string.gameLevel) + " " + games.get(i));
            TextView tvSalary = (TextView) item.findViewById(R.id.tvScore);
            tvSalary.setText(getString(R.string.result) + " " + String.valueOf(scores.get(i)));
            item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            linLayout.addView(item);
        }
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
        });
        /*deleteBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSqLiteDatabase.execSQL("DROP TABLE IF EXISTS scores");
                Log.d(LOG_TAG, "--- Delete database: ---");
            }
        });
        addBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabaseHelper.onCreate(mSqLiteDatabase);
                Log.d(LOG_TAG, "--- onCreate database ---");

            }
        });*/
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).addFirstPage();
            }
        });

        return fragment3Rates;
    }

    public void SQLread() {
        cursor = mSqLiteDatabase.query("scores", null, null, null, null, null, orderBy);
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            //int id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper._ID));
            String mname = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PLAYER_NAME));
            String mgame = cursor.getString(cursor.getColumnIndex(DataBaseHelper.GAME_MODE));
            Integer mscore = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.PLAYER_SCORES));
            Log.i("LOG_TAG", " игрок " + mname + " в режиме "
                    + mgame + " набрал " + mscore);
            names.add(mname);
            games.add(mgame);
            scores.add(mscore);
        }
        for (int i = 0; i < names.size(); i++) {
            Log.d("myLogs", "i = " + i + names.get(i) + " " + games.get(i) + " " + scores.get(i));


        }
        cursor.close();
        mSqLiteDatabase.close();
        mDatabaseHelper.close();

    }

    private void closeFragment() {
        ((MainActivity) getActivity()).addFirstPage();
    }

    public void Dialog() {
        AD = new AlertDialog.Builder(context);
        String title = getString(R.string.delete2);
        String message = getString(R.string.delete);
        AD.setTitle(title);
        AD.setMessage(message);
        AD.setPositiveButton(getString(R.string.okButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                mDatabaseHelper = new DataBaseHelper(getActivity(), "mydatabase.db", null, 1);
                mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
                Log.d(LOG_TAG, "--- Delete from mytabe: ---");
                // удаляем по id
                int delCount = mSqLiteDatabase.delete("scores", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + delCount);
                mSqLiteDatabase.close();
                mDatabaseHelper.close();
                ((MainActivity) getActivity()).addFragmentRates();

            }
        });

        AD.setNegativeButton(getString(R.string.cancelButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                return;

            }
        });

        AD.show();
    }
}
