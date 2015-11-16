package vladus177.ru.geotest_10;

import android.app.Fragment;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class FragmentFlags extends Fragment implements View.OnClickListener {

    //Buttons
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextView Question;
    RelativeLayout layout;
    ImageView imageView;
    TextView Counter;


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
    String strtext;
    String answer;

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
            R.drawable.unitedarabemirates,
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
            R.drawable.elsalvador,
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

    };
    //counts
    int wrong = 0;
    int right = 0;
    int time = 0;
    int totalTime = QUESTIONS;
    int current_right = 0;
    int questionCounter = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment1View = inflater.inflate(R.layout.activity_flags,
                container, false);

        //find them all
        button1 = (Button) fragment1View.findViewById(R.id.button1);
        button2 = (Button) fragment1View.findViewById(R.id.button2);
        button3 = (Button) fragment1View.findViewById(R.id.button3);
        button4 = (Button) fragment1View.findViewById(R.id.button4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        imageView = (ImageView) fragment1View.findViewById(R.id.imageView);
        Question = (TextView) fragment1View.findViewById(R.id.Question);
        Counter = (TextView) fragment1View.findViewById(R.id.Counter);
        buttons[0] = button1;
        buttons[1] = button2;
        buttons[2] = button3;
        buttons[3] = button4;
        strtext = ((MainActivity) getActivity()).getLevel();

        numGenerator(numbers);
        LoadQuestions();
        LoadQuestion(numbers.get(time));
        return fragment1View;
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

        //Question.setText(quest[time]);
        questionCounter++;
        for (int i = 0; i < VARIANTS; i++) {
            buttons[i].setText(answerMatrix[i][time]);
            imageView.setImageResource(drawables[time]);
        }
        current_right = rightAnswers[time] - 1;
        answer = answerMatrix[current_right][time];
    }


    //stats
    private void Stats() {
        if (strtext.equals("easy"))
        {
            double rating = Math.round(((double) right / ((double) right + (double) wrong)) * 100);
            String stat = "";
            stat += getString(R.string.note1);
            stat += " " + right + " ";
            stat += getString(R.string.note2);
            stat += " " + totalTime + ". ";
            stat += getString(R.string.note3);
            stat += " " + (rating + "").substring(0, (rating + "").length() - 2);
            Toast.makeText(getActivity(), stat, Toast.LENGTH_LONG).show();
        }
        else
        {
            double rating = right;
            String stat = "";
            stat += getString(R.string.note1);
            stat += " " + right + " ";
            stat += getString(R.string.note2);
            stat += " " + totalTime + ". ";
            stat += getString(R.string.note3);
            stat += " " + (rating + "").substring(0, (rating + "").length() - 2);
            Toast.makeText(getActivity(), stat, Toast.LENGTH_LONG).show();
        }
    }
    public void showToastRight(View view) {
        Toast toast = Toast.makeText(getActivity(), "TRUE", Toast.LENGTH_SHORT);
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
                Counter.setText(getString(R.string.note5)+ " "+ questionCounter
                        + " " +getString(R.string.note4) + " " + totalTime
                        + " " + getString(R.string.note6) );
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
                    numGenerator(numbers);
                }
                break;
            case "medium":
                totalTime = 70;
                Counter.setText(getString(R.string.note5)+ " "+ questionCounter
                        + " " +getString(R.string.note4) + " " + totalTime
                        + " " + getString(R.string.note6) );
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
                    numGenerator(numbers);
                }
                if (wrong > 3) {
                    showToastGameOver(this.layout);
                    Stats();
                    time = 0;
                    right = 0;
                    wrong = 0;
                    closeFragment();

                }
                break;
            case "hard":
                totalTime = QUESTIONS;
                Counter.setText(getString(R.string.note5)+ " "+ questionCounter
                        + " " +getString(R.string.note4) + " " + totalTime
                        + " " + getString(R.string.note6) );
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
                    numGenerator(numbers);
                }
                if (wrong > 5) {
                    showToastGameOver(this.layout);
                    Stats();
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
    private void closeFragment()
    {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }
}
