package vladus177.ru.geotest_10;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Владислав on 03.01.2016.
 */
public class FlagsMedium extends android.support.v4.app.Fragment implements View.OnClickListener {
    //Buttons
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextView Question;
    TextView Counter;
    RelativeLayout layout2;
    ImageView imageView;
    TextView timerText;
    TextView textViewLevel;
    TextView wrongRight;
    TextView rightCounter;

    //answer arrays and constants
    private static final int VARIANTS = 4;
    private static final int QUESTIONS = 120;
    private static final char DELIMITTER = '/';
    private String[][] answerMatrix = new String[VARIANTS][QUESTIONS];
    private int[] rightAnswers = new int[QUESTIONS];
    private String[] quest = new String[QUESTIONS];
    private TypedArray base;
    Button[] buttons = new Button[VARIANTS];
    ArrayList<Integer> numbers = new ArrayList<>(QUESTIONS);
    private String strtext = "Easy";
    //private String answer;
    public String name;
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    Context context;
    AlertDialog.Builder ad;
    private Toast toast = null;
    public static final String Gamesettings = "mysettings";
    public static final String FM_COUNTER = "FMcounter";
    private SharedPreferences mSettings;
    private int maxScore=0;

    // Image Array
    int[] drawables = {
            R.drawable.russia,
            R.drawable.chile,
            R.drawable.usa,
            R.drawable.canada,
            R.drawable.argentina,
            R.drawable.china,
            R.drawable.japan,
            R.drawable.germany,
            R.drawable.italy,
            R.drawable.france,
            R.drawable.unitedkingdom,
            R.drawable.portugal,
            R.drawable.brazil,
            R.drawable.greece,
            R.drawable.turkey,
            R.drawable.bulgaria,
            R.drawable.romania,
            R.drawable.mongolia,
            R.drawable.iran,
            R.drawable.iraq,
            R.drawable.poland,
            R.drawable.czechrepublic,
            R.drawable.slovakia,
            R.drawable.egypt,
            R.drawable.sweden,
            R.drawable.spain,
            R.drawable.peru,
            R.drawable.bolivia,
            R.drawable.australia,
            R.drawable.newzealand,
            R.drawable.cyprus,
            R.drawable.southkorea,
            R.drawable.malaysia,
            R.drawable.india,
            R.drawable.syria,
            R.drawable.finland,
            R.drawable.ukraine,
            R.drawable.belarus,
            R.drawable.georgia,
            R.drawable.netherlands,
            R.drawable.switzerland,
            R.drawable.indonesia,
            R.drawable.latvia,
            R.drawable.lithuania,
            R.drawable.estonia,
            R.drawable.belgium,
            R.drawable.libya,
            R.drawable.lebanon,
            R.drawable.morocco,
            R.drawable.arabemirates,
            R.drawable.southafrica,
            R.drawable.colombia,
            R.drawable.mexico,
            R.drawable.panama,
            R.drawable.venezuela,
            R.drawable.cuba,
            R.drawable.nicaragua,
            R.drawable.uruguay,
            R.drawable.paraguay,
            R.drawable.thailand,
            R.drawable.vietnam,
            R.drawable.cambodia,
            R.drawable.pakistan,
            R.drawable.hungary,
            R.drawable.slovenia,
            R.drawable.serbia,
            R.drawable.kazakhstan,
            R.drawable.armenia,
            R.drawable.croatia,
            R.drawable.ecuador,
            R.drawable.austria,
            R.drawable.azerbaijan,
            R.drawable.albania,
            R.drawable.algeria,
            R.drawable.angola,
            R.drawable.afghanistan,
            R.drawable.bangladesh,
            R.drawable.burkinafaso,
            R.drawable.haiti,
            R.drawable.guatemala,
            R.drawable.honduras,
            R.drawable.denmark,
            R.drawable.zimbabwe,
            R.drawable.israel,
            R.drawable.jordan,
            R.drawable.ireland,
            R.drawable.iceland,
            R.drawable.cameroon,
            R.drawable.qatar,
            R.drawable.kenya,
            R.drawable.costarica,
            R.drawable.madagascar,
            R.drawable.maldives,
            R.drawable.malta,
            R.drawable.nepal,
            R.drawable.salvador,
            R.drawable.saudiarabia,
            R.drawable.tunisia,
            R.drawable.montenegro,
            R.drawable.srilanka,
            R.drawable.ethiopia,
            R.drawable.jamaica,
            R.drawable.kyrgyzstan,
            R.drawable.kuwait,
            R.drawable.moldova,
            R.drawable.norway,
            R.drawable.singapore,
            R.drawable.tajikistan,
            R.drawable.turkmenistan,
            R.drawable.philippines,
            R.drawable.myanmar,
            R.drawable.yemen,
            R.drawable.sudan,
            R.drawable.uganda,
            R.drawable.uzbekistan,
            R.drawable.ghana,
            R.drawable.mozambique,
            R.drawable.tanzania,
            R.drawable.nigeria,
            R.drawable.northkorea,

    };
    //counts
    int wrong = 0;
    int right = 0;
    int time = 0;
    int totalTime = 50;
    int current_right = 0;
    int questionCounter = 0;
    int score = 0;
    boolean isFirst = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentFMView = inflater.inflate(R.layout.activity_flags,
                container, false);
        mSettings = getActivity().getSharedPreferences(Gamesettings, Context.MODE_PRIVATE);
        //find them all
        button1 = (Button) fragmentFMView.findViewById(R.id.button1);
        button2 = (Button) fragmentFMView.findViewById(R.id.button2);
        button3 = (Button) fragmentFMView.findViewById(R.id.button3);
        button4 = (Button) fragmentFMView.findViewById(R.id.button4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        timerText = (TextView) fragmentFMView.findViewById(R.id.timerText);
        imageView = (ImageView) fragmentFMView.findViewById(R.id.imageView);
        Question = (TextView) fragmentFMView.findViewById(R.id.Question);
        Counter = (TextView) fragmentFMView.findViewById(R.id.Counter);
        textViewLevel = (TextView) fragmentFMView.findViewById(R.id.textViewLevel);
        textViewLevel.setText(R.string.Easy);
        wrongRight = (TextView) fragmentFMView.findViewById(R.id.WrongRightTextView);
        rightCounter = (TextView) fragmentFMView.findViewById(R.id.textViewRightCounter);
        buttons[0] = button1;
        buttons[1] = button2;
        buttons[2] = button3;
        buttons[3] = button4;
        context = getActivity();
        //action
        numGenerator(numbers);
        LoadQuestions();
        LoadQuestion(numbers.get(time));
        return fragmentFMView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (right > maxScore) {
            maxScore = right;
        }
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(FM_COUNTER, maxScore);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSettings.contains(FM_COUNTER)) {
            // Получаем число из настроек
            maxScore = mSettings.getInt(FM_COUNTER, 0);
        }
    }

    //string parser
    private String getSubstringBetweenDelimiters(int k, int m, String str) {
        int index1 = 0;
        int index2 = 0;
        int len = str.length();
        int dels = 0;
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) == DELIMITTER) {
                dels++;
            }
            if (dels == k) {
                index1 = i;
            }
            if (dels == m) {
                index2 = i;
            }
        }
        return str.substring(index1 + 2, index2 + 1);
    }

    //Load All Questions
    private void LoadQuestions() {
        Resources res = getResources();
        base = res.obtainTypedArray(R.array.Flags);
        for (int i = 0; i < QUESTIONS; i++) {
            quest[i] = getSubstringBetweenDelimiters(0, 1, base.getString(i));
            for (int j = 0; j < VARIANTS; j++) {
                answerMatrix[j][i] = getSubstringBetweenDelimiters(j + 1, j + 2, base.getString(i));
            }
            rightAnswers[i] = Integer.parseInt
                    (getSubstringBetweenDelimiters(VARIANTS + 1, VARIANTS + 2, base.getString(i)));
        }
    }

    //Load Next Question
    private void LoadQuestion(int time) {
        wrong++;
        isFirst = true;
        button1.setTextColor(getResources().getColor(R.color.White));
        button2.setTextColor(getResources().getColor(R.color.White));
        button3.setTextColor(getResources().getColor(R.color.White));
        button4.setTextColor(getResources().getColor(R.color.White));
        Counter.setText(getString(R.string.note5) + " " + questionCounter
                + " " + getString(R.string.note4) + " " + totalTime
                + " " + getString(R.string.note6));
        questionCounter++;
        for (int i = 0; i < VARIANTS; i++) {
            buttons[i].setText(answerMatrix[i][time]);
            imageView.setImageResource(drawables[time]);
        }
        current_right = rightAnswers[time] - 1;
        //answer = answerMatrix[current_right][time];
    }


    //stats
    private void Stats() {
        double rating = right;
        String stat = "";
        stat += getString(R.string.note1);
        stat += " " + right + " ";
        stat += getString(R.string.note2);
        stat += " " + totalTime + ". ";
        stat += getString(R.string.note3);
        stat += " " + (rating + "").substring(0, (rating + "").length() - 2);
        toast = Toast.makeText(context, stat, Toast.LENGTH_LONG);
        toast.show();


    }


    //Toast game over
    public void showToastGameOver(View view) {
        if (toast == null) {
            toast = Toast.makeText(context, getString(R.string.gameOver), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            toast.cancel();
            toast = null;
            toast = Toast.makeText(context, getString(R.string.gameOver), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override //Click
    public void onClick(View v) {
        for (int i = 0; i < VARIANTS; i++) {
            if (v == buttons[i]) {
                if (current_right == i) {
                    if (isFirst != false) {
                        wrong--;
                        right++;
                    }
                    wrongRight.setTextColor(getResources().getColor(R.color.Teal));
                    wrongRight.setText(getResources().getString(R.string.right));
                    rightCounter.setText(getResources().getString(R.string.result) + " " + right);
                    time++;
                    if (wrong > 5) {
                        score = right;
                        Stats();
                        //showToastGameOver(this.layout);
                        if (right > 10 && ad == null) {
                            Dialog();
                        } else {
                            closeFragment();
                        }

                    } else if (time == totalTime) {
                        score = right;
                        Stats();
                        if (toast != null) {
                            toast.cancel();
                            toast = null;
                        }
                        if (right > 10 && ad == null) {
                            Dialog();
                        } else {
                            closeFragment();
                        }
                    } else if (time < numbers.size() && isAdded()) {
                        LoadQuestion(numbers.get(time));
                    }
                } else {
                    buttons[i].setTextColor(getResources().getColor(R.color.RED));
                    wrongRight.setTextColor(getResources().getColor(R.color.RED));
                    wrongRight.setText(getResources().getString(R.string.wrong));
                    isFirst = false;
                    if (wrong == 5 && isFirst != true) {
                        score = right;
                        Stats();
                        //showToastGameOver(this.layout);
                        if (right > 10 && ad == null) {
                            Dialog();
                        } else {
                            closeFragment();
                        }
                    }
                    return;
                }
            }
        }

    }

    //creating Array of no repeating randomly generated numbers
    private ArrayList numGenerator(ArrayList<Integer> numbersForQuestions) {
        Random rand = new Random();
        int number;

        while (numbersForQuestions.size() < QUESTIONS) {
            int random = rand.nextInt(QUESTIONS);
            if (!numbersForQuestions.contains(random)) {
                number = random;
                numbersForQuestions.add(number);
            }
        }
        return numbersForQuestions;

    }

    //closing the fragment
    private void closeFragment() {
        //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        ((MainActivity) getActivity()).addFirstPage();
    }

    public void SQLwrite(String name, String strtext, int score) {
        mDatabaseHelper = new DataBaseHelper(context, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(DataBaseHelper.PLAYER_NAME, name);
        newValues.put(DataBaseHelper.GAME_MODE, strtext);
        newValues.put(DataBaseHelper.PLAYER_SCORES, score);
        Log.d("myLogs", "Игрок записан" + name + " уровень " + strtext + " " + score);
        mSqLiteDatabase.insert("scores", null, newValues);
        mSqLiteDatabase.close();
        mDatabaseHelper.close();
        newValues.clear();
    }

    public void Dialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ad = new AlertDialog.Builder(context);
        } else {
            ad = new AlertDialog.Builder(context);
        }
        //String title = getString(R.string.dialogTitle);
        String message = getString(R.string.dialogMessage);
        final EditText input = new EditText(this.getActivity());
        ad.setView(input);
        ad.setTitle(getString(R.string.gameOver));
        ad.setMessage(message);
        ad.setPositiveButton(getString(R.string.okButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable value = input.getText();
                name = value.toString();
                SQLwrite(name, strtext, score);
                closeFragment();

            }
        });

        ad.setNegativeButton(getString(R.string.cancelButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                closeFragment();
            }
        });

        ad.show();
    }

}
