package com.techyourchance.unittesting.questions;

import com.techyourchance.unittesting.networking.questions.FetchLastActiveQuestionsEndpoint;
import com.techyourchance.unittesting.networking.questions.QuestionSchema;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FetchLastActiveQuestionsUseCaseTest {

    private EndPointTd endPointTd;

    @Captor ArgumentCaptor<List<Question>> ac;

    @Mock FetchLastActiveQuestionsUseCase.Listener listenerMock1;
    @Mock FetchLastActiveQuestionsUseCase.Listener listenerMock2;

    FetchLastActiveQuestionsUseCase SUI;

    @Before
    public void setUp() throws Exception {
        endPointTd = new EndPointTd();
        SUI = new FetchLastActiveQuestionsUseCase(endPointTd);
    }

    // success - listeners notified with data

    @Test
    public void fetchQuestions_success_listenersNotifiedWithData() {
        // Arrange
        success();
        SUI.registerListener(listenerMock1);
        SUI.registerListener(listenerMock2);
        // Act
        SUI.fetchLastActiveQuestionsAndNotify();
        // Assert
        verify(listenerMock1).onLastActiveQuestionsFetched(ac.capture());
        verify(listenerMock2).onLastActiveQuestionsFetched(ac.capture());
        List<List<Question>> acValues = ac.getAllValues();
        assertThat(acValues.get(0), is(getQuestionList()));
        assertThat(acValues.get(1), is(getQuestionList()));
    }

    // fail - listeners notified with failure


    @Test
    public void fetchQuestions_fail_listenersNotifiedWithFailure() {
        // Arrange
        fail();
        SUI.registerListener(listenerMock1);
        SUI.registerListener(listenerMock2);
        // Act
        SUI.fetchLastActiveQuestionsAndNotify();
        // Assert
        verify(listenerMock1).onLastActiveQuestionsFetchFailed();
        verify(listenerMock2).onLastActiveQuestionsFetchFailed();
    }

    private void success() {

    }

    private void fail() {
        endPointTd.failure = true;
    }

    private List<Question> getQuestionList() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("id1", "title1"));
        questions.add(new Question("id2", "title2"));
        return questions;
    }

    private static class EndPointTd extends FetchLastActiveQuestionsEndpoint {

        boolean failure;

        public EndPointTd() {
            super(null);
        }

        @Override
        public void fetchLastActiveQuestions(Listener listener) {
            if(failure){
                listener.onQuestionsFetchFailed();
            } else {
                List<QuestionSchema> schemas = new ArrayList<>();
                schemas.add(new QuestionSchema("title1", "id1", "body1"));
                schemas.add(new QuestionSchema("title2", "id2", "body2"));
                listener.onQuestionsFetched(schemas);
            }
        }
    }
}