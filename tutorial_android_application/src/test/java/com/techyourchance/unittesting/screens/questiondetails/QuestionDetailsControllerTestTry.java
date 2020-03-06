package com.techyourchance.unittesting.screens.questiondetails;

import com.techyourchance.unittesting.networking.questions.FetchLastActiveQuestionsEndpoint;
import com.techyourchance.unittesting.questions.FetchLastActiveQuestionsUseCase;
import com.techyourchance.unittesting.questions.FetchQuestionDetailsUseCase;
import com.techyourchance.unittesting.questions.QuestionDetails;
import com.techyourchance.unittesting.screens.common.screensnavigator.ScreensNavigator;
import com.techyourchance.unittesting.screens.common.toastshelper.ToastsHelper;
import com.techyourchance.unittesting.screens.questionslist.QuestionsListController;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class QuestionDetailsControllerTestTry {

    static final QuestionDetails QUESTION_DETAILS = new QuestionDetails("id", "title", "body");
    EndPointTd endPointTd;

    @Mock
    ScreensNavigator screensNavigatorMock;
    @Mock
    ToastsHelper toastsHelperMock;
    @Mock QuestionDetailsViewMvc questionDetailsViewMvcMock;

    QuestionDetailsController SUI;

    @Before
    public void setUp() throws Exception {
        endPointTd = new EndPointTd();
        SUI = new QuestionDetailsController(endPointTd, screensNavigatorMock ,toastsHelperMock);
        SUI.bindView(questionDetailsViewMvcMock);
    }

    @Test
    public void onStart_success_showProgress() {
        // Arrange
        success();
        // Act
        SUI.onStart();
        // Assert
        verify(questionDetailsViewMvcMock).showProgressIndication();
    }

    @Test
    public void onStart_listenersRegistered() {
        // Arrange
        success();
        // Act
        SUI.onStart();
        // Assert
        verify(questionDetailsViewMvcMock).registerListener(SUI);
        endPointTd.verifyListenerRegistered(SUI);
    }

    @Test
    public void onStart_success_questionBoundToViews() {
        // Arrange
        success();
        // Act
        SUI.onStart();
        // Assert
        verify(questionDetailsViewMvcMock).bindQuestion(QUESTION_DETAILS);
    }

    @Test
    public void onStart_success_progressHidden() {
        // Arrange
        success();
        // Act
        SUI.onStart();
        // Assert
        verify(questionDetailsViewMvcMock).hideProgressIndication();
    }

    @Test
    public void onStart_success_onBackClicked_navigationCalled() {
        // Arrange
        success();
        // Act
        SUI.onNavigateUpClicked();
        // Assert
        verify(screensNavigatorMock).navigateUp();
    }


    @Test
    public void fail_progressHidden() {
        // Arrange
        failure();
        // Act
        SUI.onStart();
        // Assert
        verify(questionDetailsViewMvcMock).hideProgressIndication();
    }

    @Test
    public void fail_errorToastShown() {
        // Arrange
        failure();
        // Act
        SUI.onStart();
        // Assert
        verify(toastsHelperMock).showUseCaseError();
    }

    @Test
    public void fail_detailsNotBoundToViews() {
        // Arrange
        failure();
        // Act
        SUI.onStart();
        // Assert
        verify(questionDetailsViewMvcMock, never()).bindQuestion(any(QuestionDetails.class));
    }


    @Test
    public void onStop_listenerUnregistered() {
        // Arrange
        // Act
        SUI.onStop();
        // Assert
        verify(questionDetailsViewMvcMock).unregisterListener(SUI);
        endPointTd.verifyListenerUnregistered(SUI);
    }



    // Helper functions
    private void success() {

    }

    private void failure() {
        endPointTd.failure = true;
    }

    // Helper classes
    private static class EndPointTd extends FetchQuestionDetailsUseCase {

        boolean failure;

        public EndPointTd() {
            super(null);
        }

        @Override
        public void fetchQuestionDetailsAndNotify(String questionId) {
            if(failure) {
                for (FetchQuestionDetailsUseCase.Listener listener : getListeners()) {
                    listener.onQuestionDetailsFetchFailed();
                }
            } else {
                for (FetchQuestionDetailsUseCase.Listener listener : getListeners()) {
                    listener.onQuestionDetailsFetched(QUESTION_DETAILS);
                }
            }
        }

        public void verifyListenerRegistered(QuestionDetailsController controller) {
            for(FetchQuestionDetailsUseCase.Listener listener: getListeners()) {
                if(listener == controller) {
                    return;
                }
            }
            throw new RuntimeException("Error");
        }

        public void verifyListenerUnregistered(QuestionDetailsController controller) {
            for(FetchQuestionDetailsUseCase.Listener listener: getListeners()) {
                if (listener == controller) {
                    throw new RuntimeException("Error");
                }
            }
        }
    }

}