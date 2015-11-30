package vladus177.ru.geotest_10;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Владислав on 05.11.2015.
 */
public class FragmentCapitals extends Fragment implements View.OnClickListener {
    //Buttons
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextView Question;
    TextView Counter;
    RelativeLayout layout;

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
    private String strtext;
    private String answer;
    public String name;
    final String LOG_TAG = "myLogs";
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase SQLwriter;
    Context context;
    AlertDialog.Builder ad;
    private CountDownTimer countDownTimer;
    private boolean timerStarted = false;
    private final long startTime = 12 * 1000;
    private final long interval = 1 * 1000;


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

        //find them all
        button1 = (Button) fragment2View.findViewById(R.id.button1);
        button2 = (Button) fragment2View.findViewById(R.id.button2);
        button3 = (Button) fragment2View.findViewById(R.id.button3);
        button4 = (Button) fragment2View.findViewById(R.id.button4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        Question = (TextView) fragment2View.findViewById(R.id.Question);
        Counter = (TextView) fragment2View.findViewById(R.id.Counter);
        buttons[0] = button1;
        buttons[1] = button2;
        buttons[2] = button3;
        buttons[3] = button4;
        strtext = ((MainActivity) getActivity()).getLevel();
        context = getActivity();
        dataBaseHelper = new DataBaseHelper(context, "mydatabase.db", null, 1);
        //countDownTimer = new CountDownTimerActivity(startTime, interval);
        //timerText.setText(timerText.getText() + String.valueOf(startTime / 1000));
        //countDownTimer.start();
        //timerStarted = true;
        //action
        numGenerator(numbers);
        LoadQuestions();
        LoadQuestion(numbers.get(time));
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
        if (strtext.equals("easy")) {
            double rating = Math.round(((double) right / ((double) right + (double) wrong)) * 100);
            String stat = "";
            stat += getString(R.string.note1);
            stat += " " + right + " ";
            stat += getString(R.string.note2);
            stat += " " + totalTime + ". ";
            stat += getString(R.string.note3);
            stat += " " + (rating + "").substring(0, (rating + "").length() - 2);
            Toast.makeText(getActivity(), stat, Toast.LENGTH_LONG).show();
        } else if (strtext.equals("medium")) {
            double rating = right;
            String stat = "";
            stat += getString(R.string.note1);
            stat += " " + right + " ";
            stat += getString(R.string.note2);
            stat += " " + totalTime + ". ";
            stat += getString(R.string.note3);
            stat += " " + (rating + "").substring(0, (rating + "").length() - 2);
            Toast.makeText(getActivity(), stat, Toast.LENGTH_LONG).show();
        } else {
            double rating = right;
            String stat = "";
            stat += getString(R.string.note1);
            stat += " " + (right * 2) + " ";
            stat += getString(R.string.note2);
            stat += " " + totalTime + ". ";
            stat += getString(R.string.note3);
            stat += " " + (rating + "").substring(0, (rating + "").length() - 2);
            Toast.makeText(getActivity(), stat, Toast.LENGTH_LONG).show();
        }
    }

    //Toast for correct answer
    public void showToastRight(View view) {
        Toast toast = Toast.makeText(getActivity(), getString(R.string.right), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        LinearLayout toastContainer = (LinearLayout) toast.getView();
        ImageView rightImageView = new ImageView(getActivity());
        rightImageView.setImageResource(R.drawable.right);
        toastContainer.addView(rightImageView, 0);
        toast.show();
    }

    //Toast for wrong answer
    public void showToastWrong(View view) {
        Toast toast = Toast.makeText(getActivity(), answer, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        LinearLayout toastContainer = (LinearLayout) toast.getView();
        ImageView rightImageView = new ImageView(getActivity());
        rightImageView.setImageResource(R.drawable.wrong);
        toastContainer.addView(rightImageView, 0);
        toast.show();
    }

    //Toast game over
    public void showToastGameOver(View view) {
        Toast toast = Toast.makeText(getActivity(), "Игра Закончена", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        //LinearLayout toastContainer = (LinearLayout) toast.getView();
        //ImageView rightImageView = new ImageView(getActivity());
        //rightImageView.setImageResource(R.drawable.wrong);
        //toastContainer.addView(rightImageView, 0);
        toast.show();
    }

    @Override //Click
    public void onClick(View v) {
        switch (strtext) {
            case "easy":
                totalTime = QUESTIONS;
                Counter.setText(getString(R.string.note5) + " " + questionCounter
                        + " " + getString(R.string.note4) + " " + totalTime
                        + " " + getString(R.string.note6));
                wrong++;
                for (int i = 0; i < VARIANTS; i++) {
                    if (v == buttons[i]) {
                        if (current_right == i) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }

                    }
                }

                time++;
                if (time < numbers.size()) {
                    LoadQuestion(numbers.get(time));
                }
                if (time == totalTime) {
                    Stats();
                    time = 0;
                    right = 0;
                    wrong = 0;
                    questionCounter = 1;
                    numGenerator(numbers);
                }
                break;
            case "medium":
                totalTime = QUESTIONS;
                Counter.setText(getString(R.string.note5) + " " + questionCounter
                        + " " + getString(R.string.note4) + " " + totalTime
                        + " " + getString(R.string.note6));
                wrong++;
                for (int i = 0; i < VARIANTS; i++) {
                    if (v == buttons[i]) {
                        if (current_right == i) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }

                    }
                }

                time++;
                if (time < numbers.size()) {
                    LoadQuestion(numbers.get(time));
                }
                if (time == totalTime) {
                    score = right;
                    Stats();
                    if (right > 10) {
                        Dialog();
                    }
                    time = 0;
                    right = 0;
                    wrong = 0;
                    numGenerator(numbers);
                }
                if (wrong > 5) {
                    showToastGameOver(this.layout);
                    score = right;
                    Stats();
                    if (right > 10) {
                        Dialog();
                    }
                    time = 0;
                    right = 0;
                    wrong = 0;
                    closeFragment();

                }
                break;
            case "hard":
                totalTime = QUESTIONS;
                Counter.setText(getString(R.string.note5) + " " + questionCounter
                        + " " + getString(R.string.note4) + " " + totalTime
                        + " " + getString(R.string.note6));
                wrong++;
                for (int i = 0; i < VARIANTS; i++) {
                    if (v == buttons[i]) {
                        if (current_right == i) {
                            wrong--;
                            right++;
                            showToastRight(this.layout);
                        } else {
                            showToastWrong(this.layout);
                        }

                    }
                }

                time++;
                if (time < numbers.size()) {
                    LoadQuestion(numbers.get(time));
                }
                if (time == totalTime) {
                    score = right * 2;
                    Stats();
                    if (right > 10) {
                        Dialog();
                    }
                    time = 0;
                    right = 0;
                    wrong = 0;
                    numGenerator(numbers);
                }
                if (wrong > 3) {
                    showToastGameOver(this.layout);
                    score = right * 2;
                    Stats();
                    if (right > 10) {
                        Dialog();
                    }
                    time = 0;
                    right = 0;
                    wrong = 0;
                    closeFragment();

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
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    public void SQLwrite(String name, String strtext, int score) {
        SQLwriter = dataBaseHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(DataBaseHelper.PLAYER_NAME, name);
        newValues.put(DataBaseHelper.GAME_MODE, strtext);
        newValues.put(DataBaseHelper.PLAYER_SCORES, score);
        Log.d("myLogs", "Игрок записан" + name + " уровень " + strtext + " " + score);
        SQLwriter.insert("scores", null, newValues);
        SQLwriter.close();
    }

    public void Dialog() {
        ad = new AlertDialog.Builder(context);
        String title = getString(R.string.dialogTitle);
        String message = getString(R.string.dialogMessage);
        final EditText input = new EditText(this.getActivity());
        ad.setView(input);
        ad.setTitle(title);
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
    public class CountDownTimerActivity extends CountDownTimer {
        public CountDownTimerActivity(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            timerText.setText("--");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerText.setText("" + millisUntilFinished/1000);
            (MainActivity)getActivity().
        }
    }
}
