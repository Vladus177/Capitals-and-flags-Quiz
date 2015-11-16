package vladus177.ru.geotest_10;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;

/**
 * Created by Владислав on 06.11.2015.
 */
public class FragmentButtons extends Fragment {
    // buttons for main
    Button capitalsButton;
    Button flagsButton;


    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        View buttonsView = inflater.inflate(R.layout.buttons_fragment,container,false);
        //find them all
        capitalsButton = (Button)buttonsView.findViewById(R.id.capitalsButton);
        flagsButton = (Button)buttonsView.findViewById(R.id.flagsButon);

        capitalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addFragmentCapitals();
            }
        });
        flagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addFragmentFlags();
            }
        });

        return buttonsView;
    }

}
