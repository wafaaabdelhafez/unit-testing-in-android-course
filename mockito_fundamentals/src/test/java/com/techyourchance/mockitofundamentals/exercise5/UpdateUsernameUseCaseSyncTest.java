package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    public static final String USERID = "USERID";
    public static final String USERNAME = "USERNAME";

    @Mock public UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSyncMock;
    @Mock public UsersCache usersCacheMock;
    @Mock public EventBusPoster eventBusPosterMock;

    public UpdateUsernameUseCaseSync SUI;

    @Before
    public void setUp() throws Exception{
        SUI = new UpdateUsernameUseCaseSync(updateUsernameHttpEndpointSyncMock, usersCacheMock, eventBusPosterMock);
        success();
    }

    // userid and username passed to the endpoint
    @Test
    public void update_success_userIdAndUserNamePassedToEndpoint() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUI.updateUsernameSync(USERID, USERNAME);
        verify(updateUsernameHttpEndpointSyncMock, times(1))
                .updateUsername(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0), is(USERID));
        assertThat(captures.get(1), is(USERNAME));
    }

    // test new user is cached
    @Test
    public void update_success_userCached() throws Exception {
        // Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        // Act
        SUI.updateUsernameSync(USERID, USERNAME);
        // Assert
        verify(usersCacheMock).cacheUser(ac.capture());
        User cachedUser = ac.getValue();
        assertThat(cachedUser.getUserId(), is(USERID));
        assertThat(cachedUser.getUsername(), is(USERNAME));
    }

    // test general error and no user cached
    @Test
    public void update_generalError_userNotCached() throws Exception{
        generalError();
        SUI.updateUsernameSync(USERID, USERNAME);
        verifyNoMoreInteractions(usersCacheMock);
    }

    // test general error and no interaction with event bus
    @Test
    public void update_generalError_noInteractionWithBus() throws Exception{
        generalError();
        SUI.updateUsernameSync(USERID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    // test network error
    @Test
    public void update_networkError_returnNetwork() throws Exception{
        netWorkError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUI.updateUsernameSync(USERID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }


    private void success() throws Exception{
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(
                        UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS, USERID, USERNAME));
    }

    private void generalError() throws Exception{
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(
                        UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, "", ""));
    }

    private void netWorkError() throws Exception{
       doThrow(new NetworkErrorException())
               .when(updateUsernameHttpEndpointSyncMock)
               .updateUsername(USERID, USERNAME);
    }

}