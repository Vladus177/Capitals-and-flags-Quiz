package vladus177.ru.geotest_10;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Владислав on 31.12.2015.
 */
public class FirstPage extends android.support.v4.app.Fragment  {
    Button btnFM;
    Button btnFH;
    Button btnCM;
    Button btnCH;
    Button btnTrain;
    Button btnRate;
    TextView CMBest;
    TextView CHBest;
    TextView FHBest;
    TextView FMBest;
    private int CMMaxScore=0;
    private int CHMaxScore=0;
    private int FHMaxScore=0;
    private int FMMaxScore=0;
    private SharedPreferences mSettings;
    private static final String Gamesettings = "mysettings";
    private static final String CM_COUNTER = "CMcounter";
    private static final String CH_COUNTER = "CHcounter";
    private static final String FH_COUNTER = "FHcounter";
    private static final String FM_COUNTER = "FMcounter";
    Toast toast = null;
    Context context;

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        View firstPage = inflater.inflate(R.layout.first_page,container,false);
        //find them all
        btnFM=(Button) firstPage.findViewById(R.id.buttonFlagsMedium);
        btnFH=(Button) firstPage.findViewById(R.id.buttonFlagsHard);
        btnCM=(Button) firstPage.findViewById(R.id.buttonCapMedium);
        btnCH=(Button) firstPage.findViewById(R.id.buttonCapHard);
        btnTrain=(Button) firstPage.findViewById(R.id.buttonPractice);
        btnRate=(Button) firstPage.findViewById(R.id.buttonBestPlayers);
        CMBest=(TextView) firstPage.findViewById(R.id.CMBest);
        CHBest=(TextView) firstPage.findViewById(R.id.CHBest);
        FHBest=(TextView) firstPage.findViewById(R.id.FHBest);
        FMBest=(TextView) firstPage.findViewById(R.id.FMBest);
        mSettings = getActivity().getSharedPreferences(Gamesettings, Context.MODE_PRIVATE);
        context = getActivity();
        //onClick
        btnCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addFragmentCM();
            }
        });
        btnCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CMMaxScore>=40){
                ((MainActivity)getActivity()).addFragmentCH();}
                else {
                    toast = Toast.makeText(context,getString(R.string.stats),
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addFragmentRates();
            }
        });
        btnFM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addFragmentFM();
            }
        });
        btnFH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FMMaxScore>=40){
                ((MainActivity)getActivity()).addFragmentFH();
                }
                else
                {
                    toast = Toast.makeText(context,getString(R.string.stats),
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        btnTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addFragmentPR();
            }
        });


        return firstPage;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mSettings.contains(CM_COUNTER)) {
            // Получаем число из настроек
            CMMaxScore = mSettings.getInt(CM_COUNTER,0);
            CMBest.setText(getString(R.string.best) + "\n" + CMMaxScore);
        }
        if (mSettings.contains(CH_COUNTER)) {
            // Получаем число из настроек
            CHMaxScore = mSettings.getInt(CH_COUNTER, 0);
            CHBest.setText(getString(R.string.best) + "\n" + CHMaxScore);
        }
        if (mSettings.contains(FM_COUNTER)) {
            // Получаем число из настроек
            FMMaxScore = mSettings.getInt(FM_COUNTER, 0);
            FMBest.setText(getString(R.string.best) + "\n" + FMMaxScore);
        }
        if (mSettings.contains(FH_COUNTER)) {
            // Получаем число из настроек
            FHMaxScore = mSettings.getInt(FH_COUNTER, 0);
            FHBest.setText(getString(R.string.best) + "\n" + FHMaxScore);
        }
    }


}
