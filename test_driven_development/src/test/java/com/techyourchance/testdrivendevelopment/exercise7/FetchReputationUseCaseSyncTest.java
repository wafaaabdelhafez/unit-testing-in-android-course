package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseSyncTest {

    @Mock
    GetReputationHttpEndpointSync getReputationHttpEndpointSyncMcok;

    FetchReputationUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FetchReputationUseCaseSync(getReputationHttpEndpointSyncMcok);
        success();
    }

    @Test
    public void fetchRepu_success_successReturned() {
        // Arrange
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        // Assert
        assertThat(result.getStatus(), is(FetchReputationUseCaseSync.Status.SUCCESS));
    }

    @Test
    public void fetchRepu_success_repuNumberReturned() {
        // Arrange
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        // Assert
        assertThat(result.getReputation(), is(1));
    }

    @Test
    public void fetchRepu_fail_failReturned() {
        // Arrange
        generalError();
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        // Assert
        assertThat(result.getStatus(), is(FetchReputationUseCaseSync.Status.FAILURE));
    }

    @Test
    public void fetchRepu_fail_zeroREpuREturned() {
        // Arrange
        generalError();
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        // Assert
        assertThat(result.getReputation(), is(0));
    }

    @Test
    public void fetchRepu_networkError_networkReturned() {
        // Arrange
        netWorkError();
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        // Assert
        assertThat(result.getStatus(), is(FetchReputationUseCaseSync.Status.NETWORK));
    }

    private void success(){
        when(getReputationHttpEndpointSyncMcok.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS, 1));
    }

    private void generalError(){
        when(getReputationHttpEndpointSyncMcok.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR, 0));
    }


    private void netWorkError(){
        when(getReputationHttpEndpointSyncMcok.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR, 0));
    }

}