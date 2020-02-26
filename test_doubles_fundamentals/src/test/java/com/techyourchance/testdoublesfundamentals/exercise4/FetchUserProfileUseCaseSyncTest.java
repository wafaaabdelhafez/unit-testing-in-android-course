package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.LoginHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FetchUserProfileUseCaseSyncTest {

    public static final String userId = "id";
    public static final String username = "wafaa";
    public static final String imageUrl = "imageUrl";

    public UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    public UsersCacheTd usersCacheTd;

    public FetchUserProfileUseCaseSync SUT;


    @Before
    public void setUp() {
        userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd);
    }

    // test get user profile
    @Test
    public void fetchUser_success_returnUser(){
        SUT.fetchUserProfileSync(userId);
        assertThat(userProfileHttpEndpointSyncTd.userId, is(userId));
    }

    // test no user with a specific id
    @Test
    public void fetchUser_fail_returnFailure(){
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync("11");
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    //test user is cached
    @Test
    public void fetchUser_success_userCached() {
        SUT.fetchUserProfileSync(userId);
        User user = usersCacheTd.getUser(userId);
        assertThat(user.getFullName(), is(userProfileHttpEndpointSyncTd.username));
    }

    // test server error
    @Test
    public void fetchUser_failure_failureError() {
        userProfileHttpEndpointSyncTd.mIsServerError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(userId);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    //------------------------------------------------------------------------------------------
    // Help classes

    public static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        public String userId = "id";
        public String username = "wafaa";
        public String imageUrl = "imageUrl";
        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsNetworkError;


        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            if (mIsGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if (mIsAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            }  else if (mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else if(userId.equals("11")) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, null, null, null);
            }
            return new EndpointResult(EndpointResultStatus.SUCCESS, userId,
                    username, imageUrl);
        }
    }

    public static class UsersCacheTd implements UsersCache {

        public String userId = "";
        public String username = "";
        public String imageUrl = "";

        @Override
        public void cacheUser(User user) {
            this.userId = user.getUserId();
            this.username = user.getFullName();
            this.imageUrl = user.getImageUrl();
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            return new User("id", "wafaa", "imageUrl");
        }
    }


}