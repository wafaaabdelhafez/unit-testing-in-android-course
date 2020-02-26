package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.FetchUserUseCaseSync.Status;
import com.techyourchance.testdrivendevelopment.exercise6.FetchUserUseCaseSync.UseCaseResult;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.omg.CORBA.Any;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncImpTest {

    public static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";

    @Mock
    FetchUserHttpEndpointSync fetchUserHttpEndpointSyncMock;
    @Mock
    UsersCache usersCacheMock;

    FetchUserUseCaseSyncImp SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FetchUserUseCaseSyncImp(fetchUserHttpEndpointSyncMock, usersCacheMock);
        success();
    }


    // correct parameters passed to the endpoint

    @Test
    public void fetchUser_correctParametersPassed() throws Exception{
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(fetchUserHttpEndpointSyncMock).fetchUserSync(ac.capture());
        assertThat(ac.getValue(), is(USER_ID));
    }

    // endpoint success - success returned

    @Test
    public void fetchUser_success_successReturned() {
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(Status.SUCCESS));
    }

    // endpoint autherror - failure returned

    @Test
    public void fetchUser_authError_failureReturned() throws Exception{
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(Status.FAILURE));
    }

    //test user is cached


    @Test
    public void fetchUser_success_userCached() throws Exception {
        // Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(usersCacheMock).cacheUser(ac.capture());
        User cachedUser = ac.getValue();
        assertThat(cachedUser.getUserId(), is(USER_ID));
        assertThat(cachedUser.getUsername(), is(USER_NAME));
    }

    private void success() throws Exception {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(any(String.class)))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(
                        FetchUserHttpEndpointSync.EndpointStatus.SUCCESS, USER_ID, USER_NAME));
    }

    private void authError() throws Exception {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(any(String.class)))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(
                        FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR, "", ""));
    }


}