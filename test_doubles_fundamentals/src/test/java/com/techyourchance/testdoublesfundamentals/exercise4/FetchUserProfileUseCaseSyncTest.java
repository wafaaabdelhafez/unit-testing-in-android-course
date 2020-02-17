package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;

public class FetchUserProfileUseCaseSyncTest {

    UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    UsersCacheTd usersCacheTd;

    FetchUserProfileUseCaseSync SUT;

    @Before
    public void setUp() {
        userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd);
    }



    //------------------------------------------------------------------------------------------
    // Help classes

    public static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            return null;
        }
    }

    public static class UsersCacheTd implements UsersCache {

        @Override
        public void cacheUser(User user) {

        }

        @Nullable
        @Override
        public User getUser(String userId) {
            return null;
        }
    }


}