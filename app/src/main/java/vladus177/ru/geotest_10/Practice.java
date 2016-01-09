package vladus177.ru.geotest_10;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * Created by Владислав on 04.01.2016.
 */
public class Practice extends android.support.v4.app.Fragment implements View.OnClickListener {
    //Buttons
    Button next;
    Button previous;
    Button back2;
    ImageSwitcher imageSwitcher;
    TextSwitcher textSwitcher;
    private int mPosition = 0;
    Context context;
    // Image Array
    private static final int[] drawables = {
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
    private String TEXTS[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentPracticeView = inflater.inflate(R.layout.activity_practice,
                container, false);
        TEXTS = getResources().getStringArray(R.array.info);
        context = getActivity();
        next = (Button) fragmentPracticeView.findViewById(R.id.buttonNext);
        back2 = (Button) fragmentPracticeView.findViewById(R.id.buttonBack2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).addFirstPage();
            }
        });
        previous = (Button) fragmentPracticeView.findViewById(R.id.buttonPrevious);
        imageSwitcher = (ImageSwitcher) fragmentPracticeView.findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(context);
                return imageView;
            }
        });
        imageSwitcher.setInAnimation(context, android.R.anim.slide_in_left);
        imageSwitcher.setOutAnimation(context, android.R.anim.slide_out_right);
        textSwitcher = (TextSwitcher) fragmentPracticeView.findViewById(R.id.textSwitcher);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(context);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                textView.setTextColor(getResources().getColor(R.color.White));
                return textView;
            }
        });
        textSwitcher.setInAnimation(context, android.R.anim.fade_in);
        textSwitcher.setOutAnimation(context, android.R.anim.fade_out);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        textSwitcher.setText(TEXTS[mPosition]);
        imageSwitcher.setBackgroundResource(drawables[mPosition]);

        return fragmentPracticeView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                if (mPosition==TEXTS.length-1)
                {
                    mPosition=0;
                }
                else {
                    mPosition++;
                }
            textSwitcher.setText(TEXTS[mPosition]);
            imageSwitcher.setBackgroundResource(drawables[mPosition]);

            break;
            case R.id.buttonPrevious:
                if (mPosition == 0) {
                    mPosition = TEXTS.length - 1;
                } else {
                    mPosition--;
                }
                textSwitcher.setText(TEXTS[mPosition]);
                imageSwitcher.setBackgroundResource(drawables[mPosition]);
                break;

        }

    }
}
