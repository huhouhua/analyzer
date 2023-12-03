package com.ruijie.core.docker;

import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;

public  class ProgressBuildImageResultCallback extends BuildImageResultCallback {
    private static final int PROGRESS_BAR_WIDTH = 50;

    @Override
    public void onNext(BuildResponseItem item) {
        if (item.isBuildSuccessIndicated()) {
            System.out.println("Image build completed successfully.");
        } else if (item.isErrorIndicated()) {
            System.err.println("Error occurred during image build: " + item.getErrorDetail());
        } else {
            String stream = item.getStream();
            if (stream != null) {
                updateProgressBar(stream);
            }
        }
    }

    private void updateProgressBar(String stream) {
        // 在这里更新进度条
        // 根据stream的内容，解析构建进度并更新进度条
        // 例如，你可以解析stream中的信息，获取构建进度百分比，并根据百分比更新进度条的显示
        int progress = parseProgressFromStream(stream);
        int completed = (int) (PROGRESS_BAR_WIDTH * progress / 100.0);

        StringBuilder progressBar = new StringBuilder("[");
        for (int i = 0; i < completed; i++) {
            progressBar.append("=");
        }
        for (int i = completed; i < PROGRESS_BAR_WIDTH; i++) {
            progressBar.append(" ");
        }
        progressBar.append("] ").append(progress).append("%");

        System.out.print("\r" + progressBar.toString());
    }

    private int parseProgressFromStream(String stream) {
        // 根据stream的内容，解析构建进度，并返回百分比值
        // 你需要根据构建日志的格式和内容进行适当的解析
        // 这里只是示例，假设stream中包含"progress:"和百分比值
        String progressPrefix = "progress:";
        int progressIndex = stream.indexOf(progressPrefix);
        if (progressIndex >= 0) {
            String progressValue = stream.substring(progressIndex + progressPrefix.length()).trim();
            return Integer.parseInt(progressValue);
        }
        return 0;
    }
}
