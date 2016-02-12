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
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Владислав on 02.01.2016
 */
public class CapitalsHard extends android.support.v4.app.Fragment implements View.OnClickListener {
    //Buttons
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextView Question;
    TextView Counter;
    RelativeLayout layout;
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
    private String strtext;
    //private String answer;
    private String name;
    private final String LOG_TAG = "myLogs";
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    Context context;
    AlertDialog.Builder ad = null;
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private final long startTime = 11 * 1000;
    private final long interval = 1 * 1000;
    private Toast toast = null;
    private static final String Gamesettings = "mysettings";
    private static final String CH_COUNTER = "CHcounter";
    private SharedPreferences mSettings;
    private int maxScore=0;

    //counts
    int wrong = 0;
    int right = 0;
    int time = 0;
    int totalTime = QUESTIONS;
    int current_right = 0;
    int questionCounter = 0;
    int score = 0;
    boolean isFirst = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentCHView = inflater.inflate(R.layout.activity_capitals,
                container, false);
        mSettings = getActivity().getSharedPreferences(Gamesettings, Context.MODE_PRIVATE);
        button1 = (Button) fragmentCHView.findViewById(R.id.button1);
        button2 = (Button) fragmentCHView.findViewById(R.id.button2);
        button3 = (Button) fragmentCHView.findViewById(R.id.button3);
        button4 = (Button) fragmentCHView.findViewById(R.id.button4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        timerText = (TextView) fragmentCHView.findViewById(R.id.timerText);
        Question = (TextView) fragmentCHView.findViewById(R.id.Question);
        Counter = (TextView) fragmentCHView.findViewById(R.id.Counter);
        textViewLevel = (TextView) fragmentCHView.findViewById(R.id.textViewLevel);
        textViewLevel.setText(R.string.Hard);
        wrongRight = (TextView) fragmentCHView.findViewById(R.id.WrongRightTextView);
        rightCounter = (TextView) fragmentCHView.findViewById(R.id.textViewRightCounter);
        buttons[0] = button1;
        buttons[1] = button2;
        buttons[2] = button3;
        buttons[3] = button4;
        context = getActivity();
        countDownTimer = new CountDownTimerActivity(startTime, interval);
        strtext = getString(R.string.Hard);
        //action
        numGenerator(numbers);
        LoadQuestions();
        LoadQuestion(numbers.get(time));
        return fragmentCHView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (right > maxScore) {
            maxScore = right;
        }
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(CH_COUNTER, maxScore);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSettings.contains(CH_COUNTER)) {
            // Получаем число из настроек
            maxScore = mSettings.getInt(CH_COUNTER, 0);
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

    //Load All Questions from array
    private void LoadQuestions() {
        Resources res = getResources();
        base = res.obtainTypedArray(R.array.Questions);
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
        Question.setText(quest[time]);
        for (int i = 0; i < VARIANTS; i++) {
            buttons[i].setText(answerMatrix[i][time]);
        }
        current_right = rightAnswers[time] - 1;
        //answer = answerMatrix[current_right][time];
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
        } else {
            countDownTimer.cancel();
            timerHasStarted = false;
            countDownTimer.start();
            timerHasStarted = true;
        }

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
                    if (wrong > 3) {
                        score = right;
                        Stats();
                        //showToastGameOver(this.layout);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        if (right > 10 && ad == null) {
                            Dialog();
                        } else {
                            if (isAdded()) {
                                closeFragment();
                            }
                        }

                    } else if (time == totalTime) {
                        score = right;
                        Stats();
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        if (right > 10 && ad == null) {
                            Dialog();
                        } else {
                            if (isAdded()) {
                                closeFragment();
                            }
                        }
                    } else if (time < numbers.size() && isAdded()) {
                        LoadQuestion(numbers.get(time));
                    }
                } else {
                    buttons[i].setTextColor(getResources().getColor(R.color.RED));
                    wrongRight.setTextColor(getResources().getColor(R.color.RED));
                    wrongRight.setText(getResources().getString(R.string.wrong));
                    isFirst = false;
                    if (wrong == 3 && isFirst != true) {

                        score = right;
                        Stats();
                        //showToastGameOver(this.layout);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        if (right > 10 && ad == null) {
                            Dialog();
                        } else {
                            if (isAdded()) {
                                closeFragment();
                            }
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
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        ((MainActivity) getActivity()).addFirstPage();
    }

    public void SQLwrite(String name, String strtext, int score) {
        mDatabaseHelper = new DataBaseHelper(context, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(DataBaseHelper.PLAYER_NAME, name);
        newValues.put(DataBaseHelper.GAME_MODE, strtext);
        newValues.put(DataBaseHelper.PLAYER_SCORES, score);
        Log.d("myLogs", "Игрок записан " + name + " уровень " + strtext + " " + score);
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
        InputFilter[] lengthFilter = new InputFilter[1];
        lengthFilter[0] = new InputFilter.LengthFilter(14);
        String message = getString(R.string.dialogMessage);
        final EditText input = new EditText(this.getActivity());
        input.setFilters(lengthFilter);
        ad.setTitle(getString(R.string.gameOver));
        ad.setView(input);
        ad.setMessage(message);
        ad.setPositiveButton(getString(R.string.okButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable value = input.getText();
                name = value.toString();
                SQLwrite(name, strtext, score);
                if (isAdded()) {
                    closeFragment();
                }

            }
        });

        ad.setNegativeButton(getString(R.string.cancelButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (isAdded()) {
                    closeFragment();
                }
            }
        });

        ad.show();
    }


    public class CountDownTimerActivity extends CountDownTimer {
        public CountDownTimerActivity(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            timerText.setText("--");
            wrong++;
            wrongRight.setText(R.string.over);
            time++;
            if (wrong > 3) {
                score = right;
                Stats();
                //showToastGameOver(layout);
                if (right > 10) {
                    Dialog();
                } else {
                    if (isAdded()) {
                        closeFragment();
                    }
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
                    if (isAdded()) {
                        closeFragment();
                    }
                }
            } else if (time < numbers.size() && isAdded()) {
                LoadQuestion(numbers.get(time));
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerText.setText("" + millisUntilFinished / 1000);
        }
    }
}

