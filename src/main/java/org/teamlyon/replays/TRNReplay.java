package org.teamlyon.replays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.fortnitetracker.replays.DataResponse;
import com.fortnitetracker.replays.JobData;
import com.fortnitetracker.replays.ReplayData;
import com.fortnitetracker.replays.ReplayService;

import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javafx.scene.shape.Path;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Utility class
 */
public class TRNReplay {

    public static MultipartBody.Part fromFile(File file, Consumer<Integer> percentageConsumer) {
        //return MultipartBody.Part.createFormData("file", "temp.replay",
        //        RequestBody.create(MediaType.parse("multipart/form-data"), file));
        return MultipartBody.Part.createFormData("file", "temp.replay",
                new ProgressRequestBody(file, "multipart/form-data", percentageConsumer));
    }

    public static ReplayData processReplay(File replay, Consumer<Integer> percentageConsumer) throws Exception {
        OkHttpClient client =
                new OkHttpClient.Builder()
                        .callTimeout(100, TimeUnit.MINUTES)
                        .readTimeout(100, TimeUnit.MINUTES)
                        .writeTimeout(100, TimeUnit.MINUTES)
                        .connectTimeout(100, TimeUnit.MINUTES)
                        .build();
        Retrofit retrofit =
                new Retrofit.Builder()
                        .client(client)
                        .baseUrl("https://replays.fortnitetracker.com/api/")
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                        .build();
        final ReplayService replayService = retrofit.create(ReplayService.class);
        System.out.println("Creating job...");
        final long timeStart = System.currentTimeMillis();
        /*
        replayService.createJob(fromFile(replay)).enqueue(new Callback<DataResponse<JobData>>() {
                    public void onResponse(Call<DataResponse<JobData>> call,
                                           Response<DataResponse<JobData>> responseResponse) {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                            completed[0] = true;
                            data[0] = null;
                        }
                    }

                    public void onFailure(Call<DataResponse<JobData>> call, Throwable throwable) {
                        completed[0] = true;
                        data[0] = null;
                    }
                });*/
        Response<DataResponse<JobData>> responseResponse =
                replayService.createJob(fromFile(replay, percentageConsumer)).execute();
        String jobId =
                responseResponse.body().data.jobId;
        System.out.println("Uploaded in " + ((System.currentTimeMillis() - timeStart) / 1000) +
                " seconds");
        System.out.println("Job id: " + jobId);
        JobData result;
        while (true) {
            Response<DataResponse<JobData>> statusResp =
                    replayService.getJobStatus(jobId).execute();
            JobData data = statusResp.body().data;
            if (!data.status.equalsIgnoreCase("Processing")) {
                result = data;
                break;
            } else {
                System.out.println("Still processsing");
            }
            Thread.sleep(30000);
        }
        System.out.println("Job completed!");
        if (result.status.equalsIgnoreCase("Error")) {
            System.out.println("Job couldn't be completed.");
            System.out.println(result.result.error);
            return null;
        }
        DataResponse<ReplayData> response =
                replayService.getReplayData(result.result.replayId).execute().body();
        //System.out.println(new Gson().toJson(response));
        return response.data;
    }

    public static void main(String... args) throws Exception {
        processReplay(new File("game 2.replay"), integer -> {

        });
    }

}
