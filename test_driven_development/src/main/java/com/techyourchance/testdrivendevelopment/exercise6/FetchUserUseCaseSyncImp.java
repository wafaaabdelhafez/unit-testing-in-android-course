package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

public class FetchUserUseCaseSyncImp implements FetchUserUseCaseSync {

    private FetchUserHttpEndpointSync fetchUserHttpEndpointSync;
    private UsersCache usersCache;

    public FetchUserUseCaseSyncImp(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCacheMock) {
        this.fetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        this.usersCache = usersCacheMock;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {
        FetchUserHttpEndpointSync.EndpointResult result;
        try {
            result = fetchUserHttpEndpointSync.fetchUserSync(userId);
        } catch (NetworkErrorException e) {
            return new UseCaseResult(Status.NETWORK_ERROR, null);
        }
        if(result.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS) {
            User user = new User(result.getUserId(), result.getUsername());
            usersCache.cacheUser(user);
            return new UseCaseResult(Status.SUCCESS, user);
        } else if(result.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR ||
        result.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR) {
            return new UseCaseResult(Status.FAILURE, null);
        }
        throw new RuntimeException();
    }

}
