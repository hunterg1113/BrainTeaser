package com.example.huntergreer.brainteaser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Hunter on 3/16/2018.
 */

public class StartGameActivityFragment extends Fragment {
    public static final String CUR_EQUATION = "CurEquation";
    public static final String CUR_SCORE = "CurScore";

    public static final String CUR_CHOICE_1 = "Choice1";
    public static final String CUR_CHOICE_2 = "Choice2";
    public static final String CUR_CHOICE_3 = "Choice3";
    public static final String CUR_CHOICE_4 = "Choice4";

    public static final String CUR_CHOICE_1_TAG = "Choice1Tag";
    public static final String CUR_CHOICE_2_TAG = "Choice2Tag";
    public static final String CUR_CHOICE_3_TAG = "Choice3Tag";
    public static final String CUR_CHOICE_4_TAG = "Choice4Tag";

    public static final int INIT_SCORE = 0;

    public static final long TIMER_INTERVAL = 1000;

    List<TextView> choices;

    private TextView timer;
    private TextView equation;
    private TextView score;
    private TextView response;

    private long timerMillisLeft = 30000;
    private int answersCorrect = INIT_SCORE;
    private int answersWrong = INIT_SCORE;

    private DialogEvents mListener;
    private CountDownTimer mTimer;


    interface DialogEvents {
        void onPositiveDialogResult();

        void onNegativeDialogResult();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (!(activity instanceof StartGameActivityFragment.DialogEvents)) {
            throw new ClassCastException(activity.getClass().getSimpleName() + "must implement StartGameActivityFragment.DialogEvents interface");
        }
        mListener = (StartGameActivityFragment.DialogEvents) activity;
        mTimer = new CountDownTimer(timerMillisLeft, TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerMillisLeft = millisUntilFinished;
                String timeLeft = String.valueOf(timerMillisLeft / TIMER_INTERVAL);
                timer.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                String finalScore = score.getText().toString();

                builder.setTitle(R.string.dialog_title).
                        setMessage(getString(R.string.dialog_message, finalScore)).
                        setCancelable(false).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mListener != null) mListener.onPositiveDialogResult();
                            }
                        }).
                        setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mListener != null) mListener.onNegativeDialogResult();
                            }
                        }).create().show();
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_start, container, false);

        timer = view.findViewById(R.id.timer);
        equation = view.findViewById(R.id.equation);
        score = view.findViewById(R.id.score);
        response = view.findViewById(R.id.response);

        choices = new ArrayList<>();
        choices.add((TextView) view.findViewById(R.id.choice1));
        choices.add((TextView) view.findViewById(R.id.choice2));
        choices.add((TextView) view.findViewById(R.id.choice3));
        choices.add((TextView) view.findViewById(R.id.choice4));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        playGame(view, savedInstanceState);
    }

    private void playGame(final View v, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            TextView choice1 = v.findViewById(R.id.choice1);
            choice1.setText(savedInstanceState.getString(CUR_CHOICE_1));
            choice1.setTag(savedInstanceState.getBoolean(CUR_CHOICE_1_TAG));

            TextView choice2 = v.findViewById(R.id.choice2);
            choice2.setText(savedInstanceState.getString(CUR_CHOICE_2));
            choice2.setTag(savedInstanceState.getBoolean(CUR_CHOICE_2_TAG));

            TextView choice3 = v.findViewById(R.id.choice3);
            choice3.setText(savedInstanceState.getString(CUR_CHOICE_3));
            choice3.setTag(savedInstanceState.getBoolean(CUR_CHOICE_3_TAG));

            TextView choice4 = v.findViewById(R.id.choice4);
            choice4.setText(savedInstanceState.getString(CUR_CHOICE_4));
            choice4.setTag(savedInstanceState.getBoolean(CUR_CHOICE_4_TAG));

            TextView equation = v.findViewById(R.id.equation);
            equation.setText(savedInstanceState.getString(CUR_EQUATION));

            TextView score = v.findViewById(R.id.score);
            score.setText(savedInstanceState.getString(CUR_SCORE));
        } else {
            setEquationAndChoices();
        }

        mTimer.start();

        View.OnClickListener gameListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) v.getTag()) {
                    answersCorrect++;
                    score.setText(String.valueOf(answersCorrect + "/" + (answersCorrect + answersWrong)));
                    setEquationAndChoices();
                    response.setText(R.string.correct_response_text);
                    response.setAlpha(1f);
                    response.animate().alpha(0f).setDuration(1000);
                } else {
                    answersWrong++;
                    score.setText(String.valueOf(answersCorrect + "/" + (answersCorrect + answersWrong)));
                    setEquationAndChoices();
                    response.setText(R.string.wrong_response_text);
                    response.setAlpha(1f);
                    response.animate().alpha(0f).setDuration(1000);
                }
            }
        };

        for (View view : choices) {
            view.setOnClickListener(gameListener);
        }
    }


    private void setEquationAndChoices() {
        List<Integer> usedNumbers = new ArrayList<>();
        Random random = new Random();

        int x = random.nextInt(30);
        int y = random.nextInt(30);
        int sum = x + y;
        usedNumbers.add(sum);

        String equation = x + " + " + y;
        this.equation.setText(equation);

        if (getView() != null) {
            choices = new ArrayList<>();
            choices.add((TextView) getView().findViewById(R.id.choice1));
            choices.add((TextView) getView().findViewById(R.id.choice2));
            choices.add((TextView) getView().findViewById(R.id.choice3));
            choices.add((TextView) getView().findViewById(R.id.choice4));
        }
        int correctChoice = random.nextInt(4);
        for (int i = 0; i < choices.size(); i++) {
            if (i == correctChoice) {
                choices.get(i).setText(String.valueOf(sum));
                choices.get(i).setTag(true);
            } else {
                int wrongAns = random.nextInt(60);
                while (usedNumbers.contains(wrongAns)) {
                    wrongAns = random.nextInt(60);
                }
                usedNumbers.add(wrongAns);
                choices.get(i).setText(String.valueOf(wrongAns));
                choices.get(i).setTag(false);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CUR_CHOICE_1, choices.get(0).getText().toString());
        outState.putString(CUR_CHOICE_2, choices.get(1).getText().toString());
        outState.putString(CUR_CHOICE_3, choices.get(2).getText().toString());
        outState.putString(CUR_CHOICE_4, choices.get(3).getText().toString());

        outState.putBoolean(CUR_CHOICE_1_TAG, (boolean) choices.get(0).getTag());
        outState.putBoolean(CUR_CHOICE_2_TAG, (boolean) choices.get(1).getTag());
        outState.putBoolean(CUR_CHOICE_3_TAG, (boolean) choices.get(2).getTag());
        outState.putBoolean(CUR_CHOICE_4_TAG, (boolean) choices.get(3).getTag());

        outState.putString(CUR_SCORE, score.getText().toString());
        outState.putString(CUR_EQUATION, equation.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}