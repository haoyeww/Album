package program.viewer;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import program.controller.DisplayShelfController;

import java.util.ArrayList;

/*
  Simple 7 segment LED style digit. It supports the numbers 0 through 9.
 */
/**
 * A ui control which displays a browse-able display shelf of images
 * This was copied from Java SE Development Kit 8u152 Demos and Samples in DisplayShelf under Ensemble8 section.
 * The download link is here:
  * http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
 * (retrieved in November 2017)
 */
//        Copyright (c) 2016, Oracle America, Inc. All rights reserved.
//
//        Use of this source code is governed by a BSD-style license that can be 
//        found in the LICENSE file.
//
//        Redistribution and use in source and binary forms, with or without modification,
//        are permitted provided that the following conditions are met:
//
//        1. Redistributions of source code must retain the above copyright notice,
//        this list of conditions and the following disclaimer.
//
//        2. Redistributions in binary form must reproduce the above copyright notice,
//        this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
//
//        3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
//        derived from this software without specific prior written permission.
//
//        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
//        BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
//        IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
//        OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
//        OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
//        OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
//        OF SUCH DAMAGE.
public class DisplayShelf extends Region {

    private final Duration DURATION = Duration.millis(500);
    private final Interpolator INTERPOLATOR = Interpolator.EASE_BOTH;
    private DisplayShelfController[] items;
    private Group centered = new Group();
    private Group left = new Group();
    private Group center = new Group();
    private Group right = new Group();
    private static int centerIndex = 0;
    private Timeline timeline;
    private ScrollBar scrollBar = new ScrollBar();
    private boolean localChange = false;
    private Rectangle clip = new Rectangle();

    public DisplayShelf(ArrayList<Image> images) {
        centerIndex = 0;
        this.setPrefHeight(this.getHeight() / 2);
        // set clip
        setClip(clip);
        // set ids for styling via CSS
        setId("displayshelf");
        scrollBar.setId("display-scrollbar");
        // create items
        items = new DisplayShelfController[images.size()];
        for (int i = 0; i < images.size(); i++) {
            final DisplayShelfController item =
                    items[i] = new DisplayShelfController(images.get(i));
            final double index = i;
            item.setOnMouseClicked((MouseEvent me) -> {
                localChange = true;
                scrollBar.setValue(index);
                localChange = false;
                shiftToCenter(item);
            });
        }
        // setup scroll bar
        scrollBar.setMax(items.length - 1);
        scrollBar.setVisibleAmount(1);
        scrollBar.setUnitIncrement(1);
        scrollBar.setBlockIncrement(1);
        scrollBar.valueProperty().addListener((Observable ov) -> {
            if (!localChange) {
                shiftToCenter(items[(int) Math.round(scrollBar.getValue())]);
            }
        });
        // create content
        centered.getChildren().addAll(left, right, center);
        getChildren().addAll(centered, scrollBar);
        // listen for keyboard events
        setFocusTraversable(true);
        setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.LEFT) {
                shift(1);
                localChange = true;
                scrollBar.setValue(centerIndex);
                localChange = false;
            } else if (ke.getCode() == KeyCode.RIGHT) {
                shift(-1);
                localChange = true;
                scrollBar.setValue(centerIndex);
                localChange = false;
            }
        });
        // update
        update();
    }

    public static int getcenter() {
        return centerIndex;
    }

    @Override
    protected void layoutChildren() {
        // update clip to our size
        clip.setWidth(getWidth());
        clip.setHeight(getPrefHeight());
        // keep centered centered
        centered.setLayoutY((getPrefHeight() - DisplayShelfController.HEIGHT) / 2);
        centered.setLayoutX((getWidth() - DisplayShelfController.WIDTH) / 2);
        // position scroll bar at bottom
        scrollBar.setLayoutX(10);
        scrollBar.setLayoutY(getPrefHeight() - 25);
        scrollBar.resize(getWidth() - 20, 15);
    }

    private void update() {
        // move items to new homes in groups
        left.getChildren().clear();
        center.getChildren().clear();
        right.getChildren().clear();
        for (int i = 0; i < centerIndex; i++) {
            left.getChildren().add(items[i]);
        }
        center.getChildren().add(items[centerIndex]);
        for (int i = items.length - 1; i > centerIndex; i--) {
            right.getChildren().add(items[i]);
        }
        // stop old timeline if there is one running
        if (timeline != null) {
            timeline.stop();
        }
        // create timeline to animate to new positions
        timeline = new Timeline();
        // addTagBtn keyframes for left items
        final ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
        double SCALE_SMALL = 1.0;
        double SPACING = 200;
        for (int i = 0; i < left.getChildren().size(); i++) {
            final DisplayShelfController it = items[i];
            double LEFT_OFFSET = -100;
            double newX = -left.getChildren().size()
                    * SPACING + SPACING * i + LEFT_OFFSET;
            keyFrames.add(new KeyFrame(DURATION,
                    new KeyValue(it.translateXProperty(), newX, INTERPOLATOR),
                    new KeyValue(it.scaleXProperty(), SCALE_SMALL, INTERPOLATOR),
                    new KeyValue(it.scaleYProperty(), SCALE_SMALL, INTERPOLATOR),
                    new KeyValue(it.angle, 90.0, INTERPOLATOR)));
        }
        // addTagBtn keyframe for center item
        final DisplayShelfController centerItem = items[centerIndex];
        keyFrames.add(new KeyFrame(DURATION,
                new KeyValue(centerItem.translateXProperty(), 0, INTERPOLATOR),
                new KeyValue(centerItem.scaleXProperty(), 1.0, INTERPOLATOR),
                new KeyValue(centerItem.scaleYProperty(), 1.0, INTERPOLATOR),
                new KeyValue(centerItem.angle, 90.0, INTERPOLATOR)));
        // addTagBtn keyframes for right items
        for (int i = 0; i < right.getChildren().size(); i++) {
            final DisplayShelfController it = items[items.length - i - 1];
            double RIGHT_OFFSET = 100;
            final double newX = right.getChildren().size()
                    * SPACING - SPACING * i + RIGHT_OFFSET;
            keyFrames.add(new KeyFrame(DURATION,
                    new KeyValue(it.translateXProperty(), newX, INTERPOLATOR),
                    new KeyValue(it.scaleXProperty(), SCALE_SMALL, INTERPOLATOR),
                    new KeyValue(it.scaleYProperty(), SCALE_SMALL, INTERPOLATOR),
                    new KeyValue(it.angle, 90.0, INTERPOLATOR)));
        }
        // play animation
        timeline.play();
    }

    private void shiftToCenter(DisplayShelfController item) {
        for (int i = 0; i < left.getChildren().size(); i++) {
            if (left.getChildren().get(i) == item) {
                int shiftAmount = left.getChildren().size() - i;
                shift(shiftAmount);
                return;
            }
        }
        if (center.getChildren().get(0) == item) {
            return;
        }
        for (int i = 0; i < right.getChildren().size(); i++) {
            if (right.getChildren().get(i) == item) {
                int shiftAmount = -(right.getChildren().size() - i);
                shift(shiftAmount);
                return;
            }
        }
    }

    private void shift(int shiftAmount) {
        if (centerIndex <= 0 && shiftAmount > 0) {
            return;
        }
        if (centerIndex >= items.length - 1 && shiftAmount < 0) {
            return;
        }
        centerIndex -= shiftAmount;
        update();
    }
}
