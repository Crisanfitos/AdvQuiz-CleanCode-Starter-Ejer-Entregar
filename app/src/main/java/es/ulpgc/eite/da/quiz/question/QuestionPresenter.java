package es.ulpgc.eite.da.quiz.question;

import android.util.Log;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.quiz.app.AppMediator;
import es.ulpgc.eite.da.quiz.app.CheatToQuestionState;
import es.ulpgc.eite.da.quiz.app.QuestionToCheatState;

public class QuestionPresenter implements QuestionContract.Presenter {

  public static String TAG = QuestionPresenter.class.getSimpleName();

  private AppMediator mediator;
  private WeakReference<QuestionContract.View> view;
  private QuestionState state;
  private QuestionContract.Model model;

  public QuestionPresenter(AppMediator mediator) {
    this.mediator = mediator;
    state = mediator.getQuestionState();
  }

  @Override
  public void onStart() {
    Log.e(TAG, "onStart()");

    // call the model
    state.question = model.getQuestion();
    state.option1 = model.getOption1();
    state.option2 = model.getOption2();
    state.option3 = model.getOption3();

    // reset state to tests
    state.answerCheated=false;
    state.optionClicked = false;
    state.option = 0;

    // update the view
    disableNextButton();
    view.get().resetReply();
  }


  @Override
  public void onRestart() {
    Log.e(TAG, "onRestart()");

    //TODO: falta implementacion
    view.get().updateReply(state.optionCorrect);
  }


  @Override
  public void onResume() {
    Log.e(TAG, "onResume()");

    //TODO: falta implementacion

    // use passed state if is necessary
    CheatToQuestionState savedState = getStateFromCheatScreen();
    if (savedState != null) {
      if (savedState.answerCheated) {
        model.updateQuizIndex();
      }
    }


    // update the view
    view.get().displayQuestion(state);
    if(state.optionClicked){
      if(state.optionCorrect) {
        view.get().updateReply(true);
      }else{
        view.get().updateReply(false);
      }
    }
  }


  @Override
  public void onDestroy() {
    Log.e(TAG, "onDestroy()");
  }

  @Override
  public void onOptionButtonClicked(int option) {
    Log.e(TAG, "onOptionButtonClicked()");

    boolean isCorrect = model.isCorrectOption(option);
    state.optionCorrect = isCorrect;
    if(isCorrect) {
      state.cheatEnabled=false;
      view.get().updateReply(true);
    } else {
      state.cheatEnabled=true;
      view.get().updateReply(false);
    }

    enableNextButton();
    state.optionCorrect = isCorrect;
    model.updateQuizIndex();
    state.quizIndex = model.getQuizIndex();
    view.get().displayQuestion(state);
  }

  @Override
  public void onNextButtonClicked() {
    Log.e(TAG, "onNextButtonClicked()");

    //TODO: falta implementacion
    disableNextButton();
    state.question = model.getQuestion();
    state.option1 = model.getOption1();
    state.option2 = model.getOption2();
    state.option3 = model.getOption3();
    view.get().displayQuestion(state);
    view.get().resetReply();

  }

  @Override
  public void onCheatButtonClicked() {
    Log.e(TAG, "onCheatButtonClicked()");

    //TODO: falta implementacion
  }

  private void passStateToCheatScreen(QuestionToCheatState state) {

    //TODO: falta implementacion
    mediator.setQuestionToCheatState(state);

  }

  private CheatToQuestionState getStateFromCheatScreen() {

    //TODO: falta implementacion
    mediator.getCheatToQuestionState();
    return null;
  }

  private void disableNextButton() {
    state.optionEnabled=true;
    state.cheatEnabled=true;
    state.nextEnabled=false;

  }

  private void enableNextButton() {
    state.optionEnabled=false;

    if(!model.hasQuizFinished()) {
      state.nextEnabled=true;
    }
  }

  @Override
  public void injectView(WeakReference<QuestionContract.View> view) {
    this.view = view;
  }

  @Override
  public void injectModel(QuestionContract.Model model) {
    this.model = model;
  }

}
