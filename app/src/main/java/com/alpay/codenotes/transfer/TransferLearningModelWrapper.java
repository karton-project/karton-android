/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.alpay.codenotes.transfer;

import android.os.ConditionVariable;

import androidx.appcompat.app.AppCompatActivity;

import com.alpay.codenotes.transfer.api.AssetModelLoader;
import com.alpay.codenotes.transfer.api.TransferLearningModel;

import java.io.Closeable;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * App-layer wrapper for {@link TransferLearningModel}.
 *
 * <p>This wrapper allows to run training continuously, using start/stop API, in contrast to
 * run-once API of {@link TransferLearningModel}.
 */
public class TransferLearningModelWrapper implements Closeable {

    public static final int IMAGE_SIZE = 224;
    private static TransferLearningModel model;
    private final ConditionVariable shouldTrain = new ConditionVariable();
    private volatile TransferLearningModel.LossConsumer lossConsumer;

    public TransferLearningModelWrapper(AppCompatActivity appCompatActivity) {
        model = new TransferLearningModel(
                new AssetModelLoader(appCompatActivity, "model"), Arrays.asList("0", "1", "2", "3"));

        new Thread(() -> {
            while (!Thread.interrupted()) {
                shouldTrain.block();
                try {
                    model.train(1, lossConsumer).get();
                } catch (ExecutionException e) {
                    throw new RuntimeException("Exception occurred during model training", e.getCause());
                } catch (InterruptedException e) {
                    // no-op
                }
            }
        }).start();
    }

    // This method is thread-safe.
    public Future<Void> addSample(float[] image, String className) {
        return model.addSample(image, className);
    }

    // This method is thread-safe, but blocking.
    public TransferLearningModel.Prediction[] predict(float[] image) {
        return model.predict(image);
    }

    public int getTrainBatchSize() {
        return model.getTrainBatchSize();
    }

    /**
     * Start training the model continuously until {@link #disableTraining() disableTraining} is
     * called.
     *
     * @param lossConsumer callback that the loss values will be passed to.
     */
    public void enableTraining(TransferLearningModel.LossConsumer lossConsumer) {
        this.lossConsumer = lossConsumer;
        shouldTrain.open();
    }

    /**
     * Stops training the model.
     */
    public void disableTraining() {
        shouldTrain.close();
    }

    /**
     * Frees all model resources and shuts down all background threads.
     */
    public void close() {
        model.close();
    }
}
