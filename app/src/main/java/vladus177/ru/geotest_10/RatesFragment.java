package vladus177.ru.geotest_10;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Владислав on 21.11.2015.
 */
public class RatesFragment extends Fragment {
    final String LOG_TAG = "myLogs";
    public DataBaseHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> games = new ArrayList<>();
    ArrayList<Integer> scores = new ArrayList<>();
    Cursor cursor;
    String orderBy;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment3Rates = inflater.inflate(R.layout.activity_rates,
                container, false);
        orderBy = "playerScores DESC";
        mDatabaseHelper = new DataBaseHelper(getActivity(), "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        SQLread();
        LinearLayout linLayout = (LinearLayout) fragment3Rates.findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getActivity().getLayoutInflater();
        for (int i = 0; i < names.size(); i++) {
            Log.d("myLogs", "i = " + i);
            View item = ltInflater.inflate(R.layout.list_item, linLayout, false);
            TextView tvName = (TextView) item.findViewById(R.id.tvName);
            tvName.setText(names.get(i));
            TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
            tvPosition.setText(getString(R.string.gameLevel) + " "+ games.get(i));
            TextView tvSalary = (TextView) item.findViewById(R.id.tvSalary);
            tvSalary.setText(getString(R.string.result) + " "+String.valueOf(scores.get(i)));
            item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            linLayout.addView(item);
        }


        return fragment3Rates;
    }

    public void SQLread() {
        cursor = mSqLiteDatabase.query("scores", null, null, null, null, null, orderBy);

        cursor.moveToFirst();


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
    }

}
