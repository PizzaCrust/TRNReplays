package com.fortnitetracker.replays;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ReplayService {


    /**
     * Creates a job, ONLY retrieve JOB ID to get the status of the job and the result of it.
     * @param replay
     * @return
     */
    @POST("replays/upload")
    @Multipart
    Call<DataResponse<JobData>> createJob(@Part("replay") RequestBody replay);

    /**
     * Looks up status of a job, returns result of the job; if finished.
     * @param jobId
     * @return
     */
    @GET("replays/upload/status/{id}")
    Call<DataResponse<JobData>> getJobStatus(@Path("id") String jobId);

    /**
     * Attempts to get replay data from TRN replay ID.
     * @param replayId
     * @return
     */
    @GET("replays/{id}")
    Call<DataResponse<ReplayData>> getReplayData(@Path("id") String replayId);

}
