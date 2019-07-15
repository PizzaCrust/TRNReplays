package org.teamlyon.replays;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Based on
 * @author https://stackoverflow.com/questions/33338181/is-it-possible-to-show-progress-bar-when-upload-image-via-retrofit-2/33384551#33384551
 */
public class ProgressRequestBody extends RequestBody {
    private File mFile;
    private String mPath;
    private String content_type;

    private final Consumer<Integer> percentageConsumer;

    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public ProgressRequestBody(final File file, String content_type,
                               Consumer<Integer> percentageConsumer) {
        this.content_type = content_type;
        this.percentageConsumer = percentageConsumer;
        mFile = file;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(content_type+"/*");
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;
        int lastPercentage = 0;
        try {
            int read;
            while ((read = in.read(buffer)) != -1) {

                // update progress on UI thread
                //handler.post(new ProgressUpdater(uploaded, fileLength));
                int percentage = (int) (100 * uploaded/fileLength);
                if (lastPercentage != percentage) {
                    System.out.println(percentage + "%");
                    percentageConsumer.accept(percentage);
                    lastPercentage = percentage;
                }

                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }
}
