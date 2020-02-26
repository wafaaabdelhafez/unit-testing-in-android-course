package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

public class FetchReputationUseCaseSync {

    private GetReputationHttpEndpointSync getReputationHttpEndpointSync;

    public FetchReputationUseCaseSync(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    public UseCaseResult getReputationSync() {
        GetReputationHttpEndpointSync.EndpointResult result = getReputationHttpEndpointSync.getReputationSync();
        switch (result.getStatus()){
            case SUCCESS:
                return new UseCaseResult(Status.SUCCESS, 1);
            case GENERAL_ERROR:
                return new UseCaseResult(Status.FAILURE, 0);
            case NETWORK_ERROR:
                return new UseCaseResult(Status.NETWORK, 0);
        }
        throw new RuntimeException();
    }

    public class UseCaseResult {

        private final int reputation;
        private final Status status;

        public UseCaseResult(Status status, int reputation) {
            this.status = status;
            this.reputation = reputation;
        }

        public int getReputation() {
            return reputation;
        }

        public Status getStatus() {
            return status;
        }
    }

    public enum Status {
        SUCCESS,
        FAILURE,
        NETWORK
    }

}
