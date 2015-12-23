package vladus177.ru.geotest_10;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Владислав on 05.11.2015.
 */
public class FragmentCapitals extends android.support.v4.app.Fragment implements View.OnClickListener {
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


    //answer arrays and constants
    private static final int VARIANTS = 4;
    private static final int QUESTIONS = 110;
    private static final char DELIMITTER = '/';
    private String[][] answerMatrix = new String[VARIANTS][QUESTIONS];
    private int[] rightAnswers = new int[QUESTIONS];
    private String[] quest = new String[QUESTIONS];
    private TypedArray base;
    Button[] buttons = new Button[VARIANTS];
    ArrayList<Integer> numbers = new ArrayList<>(QUESTIONS);
    private String strtext = null;
    private String answer;
    public String name;
    final String LOG_TAG = "myLogs";
    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    Context context;
    AlertDialog.Builder ad;
    AlertDialog.Builder fad;
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private final long startTime = 11 * 1000;
    private final long interval = 1 * 1000;

    //Toasts
    private Toast toast = null;

    //counts
    int wrong = 0;
    int right = 0;
    int time = 0;
    int totalTime = QUESTIONS;
    int current_right = 0;
    int questionCounter = 0;
    int score = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment2View = inflater.inflate(R.layout.activity_capitals,
                container, false);
        button1 = (Button) fragment2View.findViewById(R.id.button1);
        button2 = (Button) fragment2View.findViewById(R.id.button2);
        button3 = (Button) fragment2View.findViewById(R.id.button3);
        button4 = (Button) fragment2View.findViewById(R.id.button4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        timerText = (TextView) fragment2View.findViewById(R.id.timerText);
        Question = (TextView) fragment2View.findViewById(R.id.Question);
        Counter = (TextView) fragment2View.findViewById(R.id.Counter);
        textViewLevel = (TextView) fragment2View.findViewById(R.id.textViewLevel);
        buttons[0] = button1;
        buttons[1] = button2;
        buttons[2] = button3;
        buttons[3] = button4;
        context = getActivity();
        countDownTimer = new CountDownTimerActivity(startTime, interval);
        //action
        numGenerator(numbers);
        LoadQuestions();
        firstDialog();
        return fragment2View;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        Counter.setText(getString(R.string.note5) + " " + questionCounter
                + " " + getString(R.string.note4) + " " + totalTime
                + " " + getString(R.string.note6));
        questionCounter++;
        Question.setText(quest[time]);
        for (int i = 0; i < VARIANTS; i++) {
            buttons[i].setText(answerMatrix[i][time]);
        }
        current_right = rightAnswers[time] - 1;
        answer = answerMatrix[current_right][time];

    }


    //stats
    private void Stats() {
        if (isAdded()) {
            if (strtext.equals("Easy")) {
                double rating = Math.round(((double) right / ((double) right + (double) wrong)) * 100);
                String stat = "";
                stat += getString(R.string.note1);
                stat += " " + right + " ";
                stat += getString(R.string.note2);
                stat += " " + totalTime + ". ";
                stat += getString(R.string.note3);
                stat += " " + (rating + "").substring(0, (rating + "").length() - 2);
                toast = Toast.makeText(context, stat, Toast.LENGTH_LONG);
                toast.show();
            } else if (strtext.equals("Medium")) {
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
            } else {
                double rating = right*2;
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
        } else return;
    }

    //Toast for correct answer
    public void showToastRight(View view) {
        if (toast == null) {
            toast = Toast.makeText(context, getString(R.string.right), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            LinearLayout toastContainer = (LinearLayout) toast.getView();
            ImageView toastView = new ImageView(getActivity());
            toastView.setImageResource(R.drawable.right);
            toastContainer.addView(toastView, 0);
            toast.show();
        } else {
            toast.cancel();
            toast = null;
            toast = Toast.makeText(context, getString(R.string.right), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            LinearLayout toastContainer = (LinearLayout) toast.getView();
            ImageView toastView = new ImageView(getActivity());
            toastView.setImageResource(R.drawable.right);
            toastContainer.addView(toastView, 0);
            toast.show();
        }
    }

    //Toast for wrong answer
    public void showToastWrong(View view) {
        if (toast == null) {
            toast = Toast.makeText(context, answer, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
        LinearLayout toastContainer = (LinearLayout) toast.getView();
        ImageView toastView = new ImageView(getActivity());
        toastView.setImageResource(R.drawable.wrong);
        toastContainer.addView(toastView, 0);
            toast.show();
        } else {
            toast.cancel();
            toast = null;
            toast = Toast.makeText(context, answer, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            LinearLayout toastContainer = (LinearLayout) toast.getView();
            ImageView toastView = new ImageView(getActivity());
            toastView.setImageResource(R.drawable.wrong);
            toastContainer.addView(toastView, 0);
            toast.show();
        }
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
        int a;
        switch (strtext) {
            case "Easy":
                totalTime = QUESTIONS;
                wrong++;
                switch (v.getId()) {
                    case R.id.button1:
                        a = 0;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }

                        break;
                    case R.id.button2:
                        a = 1;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }
                        break;
                    case R.id.button3:
                        a = 2;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }
                        break;
                    case R.id.button4:
                        a = 3;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }
                        break;
                }

                time++;
                if (time == totalTime) {
                    Stats();
                    time = 0;
                    right = 0;
                    wrong = 0;
                    questionCounter = 1;
                    numGenerator(numbers);
                } else if (time < numbers.size() && isAdded()) {
                    LoadQuestion(numbers.get(time));
                }
                break;
            case "Medium":
                totalTime = QUESTIONS;
                wrong++;
                switch (v.getId()) {
                    case R.id.button1:
                        a = 0;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }

                        break;
                    case R.id.button2:
                        a = 1;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }
                        break;
                    case R.id.button3:
                        a = 2;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }
                        break;
                    case R.id.button4:
                        a = 3;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }
                        break;
                }
                time++;
                if (wrong > 5) {
                    score = right;
                    Stats();
                    showToastGameOver(this.layout);
                    if (right > 10) {
                        Dialog();
                    }
                    time = 0;
                    right = 0;
                    wrong = 0;
                    if (toast != null) {
                        toast.cancel();
                        toast = null;
                    }
                    closeFragment();

                } else if (time == totalTime) {
                    score = right;
                    Stats();
                    if (right > 10) {
                        Dialog();
                    }
                    time = 0;
                    right = 0;
                    wrong = 0;
                    if (toast != null) {
                        toast.cancel();
                        toast = null;
                    }
                    closeFragment();
                } else if (time < numbers.size() && isAdded()) {
                    LoadQuestion(numbers.get(time));
                }

                break;
            case "Hard":
                totalTime = QUESTIONS;
                wrong++;
                if (!timerHasStarted) {
                    countDownTimer.start();
                    timerHasStarted = true;
                } else {
                    countDownTimer.cancel();
                    timerHasStarted = false;
                    countDownTimer.start();
                    timerHasStarted = true;
                }
                switch (v.getId()) {
                    case R.id.button1:
                        a = 0;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }

                        break;
                    case R.id.button2:
                        a = 1;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }
                        break;
                    case R.id.button3:
                        a = 2;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }
                        break;
                    case R.id.button4:
                        a = 3;
                        if (a == current_right) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }
                        break;
                }
                time++;
                if (wrong > 3) {
                    score = right * 2;
                    Stats();
                    showToastGameOver(this.layout);
                    if (right > 10) {
                        Dialog();
                    }
                    time = 0;
                    right = 0;
                    wrong = 0;
                    if (toast != null) {
                        toast.cancel();
                        toast = null;
                    }
                    closeFragment();
                } else if (time == totalTime) {
                    score = right * 2;
                    Stats();
                    if (right > 10) {
                        Dialog();
                    }
                    time = 0;
                    right = 0;
                    wrong = 0;
                    if (toast != null) {
                        toast.cancel();
                        toast = null;
                    }
                    closeFragment();
                } else if (time < numbers.size() && isAdded()) {
                    LoadQuestion(numbers.get(time));
                    //timerText.setText(timerText.getText() + String.valueOf(startTime / 1000));
                }
                break;
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
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ad = new AlertDialog.Builder(context,R.style.myBackgroundStyle);
        }
        else
        {
            ad = new AlertDialog.Builder(context);
        }
        //String title = getString(R.string.dialogTitle);
        String message = getString(R.string.dialogMessage);
        final EditText input = new EditText(this.getActivity());
        ad.setView(input);
        //ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton(getString(R.string.okButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable value = input.getText();
                name = value.toString();
                SQLwrite(name, strtext, score);


            }
        });

        ad.setNegativeButton(getString(R.string.cancelButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                return;
            }
        });

        ad.show();
    }


    public void firstDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            fad = new AlertDialog.Builder(context,R.style.myBackgroundStyle);
        }
        else {
            fad = new AlertDialog.Builder(context);
        }
        final String[] gameLevels = {"Easy", "Medium", "Hard"};
        String title = getString(R.string.levelSelect);
        fad.setTitle(title);
        fad.setItems(gameLevels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                strtext = gameLevels[item];
                textViewLevel.setText(strtext);
                if (isAdded()) {
                    LoadQuestion(numbers.get(time));
                    if (strtext.equals("Hard")) {
                        if (!timerHasStarted) {
                            countDownTimer.start();
                            timerHasStarted = true;
                        } else {
                            countDownTimer.cancel();
                            timerHasStarted = false;
                        }
                        timerText.setText(timerText.getText() + String.valueOf(startTime / 1000));
                    }
                }

            }
        });
        fad.show();

    }

    public class CountDownTimerActivity extends CountDownTimer {
        public CountDownTimerActivity(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            timerText.setText("--");
            wrong++;
            showToastWrong(layout);
            time++;
            if (wrong > 3) {
                score = right * 2;
                Stats();
                showToastGameOver(layout);
                if (right > 10) {
                    Dialog();
                }
                time = 0;
                right = 0;
                wrong = 0;
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }

                closeFragment();
            } else if (time == totalTime) {
                score = right * 2;
                Stats();
                if (right > 10) {
                    Dialog();
                }
                time = 0;
                right = 0;
                wrong = 0;
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
                closeFragment();
            } else if (time < numbers.size() && isAdded()) {
                LoadQuestion(numbers.get(time));
                countDownTimer.start();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerText.setText("" + millisUntilFinished / 1000);
        }
    }
}
