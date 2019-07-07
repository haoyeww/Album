package program.controller;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A Node that displays a image with some 2.5D perspective rotation around the Y
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
public class DisplayShelfController extends Parent {

    private static final double REFLECTION_SIZE = 0.25;

    public static final double WIDTH = 200;
    public static final double HEIGHT = WIDTH + (WIDTH * REFLECTION_SIZE);

    private static final double RADIUS_H = WIDTH / 2;
    private static final double BACK = WIDTH / 10;
    private PerspectiveTransform transform = new PerspectiveTransform();

    public DisplayShelfController(Image image) {
        ImageView imageView = new ImageView(image);
        Reflection reflection = new Reflection();
        reflection.setFraction(REFLECTION_SIZE);
        imageView.setEffect(reflection);
        setEffect(transform);
        getChildren().addAll(imageView);
    }

    /**
     * Angle Property
     */
    public final DoubleProperty angle = new SimpleDoubleProperty(45) {
        @Override
        protected void invalidated() {
            // when angle viewChangeBtn calculate new transform
            double lx = (RADIUS_H - Math.sin(Math.toRadians(angle.get())) * RADIUS_H - 1);
            double rx = (RADIUS_H + Math.sin(Math.toRadians(angle.get())) * RADIUS_H + 1);
            double uly = (-Math.cos(Math.toRadians(angle.get())) * BACK);
            double ury = -uly;
            transform.setUlx(lx);
            transform.setUly(uly);
            transform.setUrx(rx);
            transform.setUry(ury);
            transform.setLrx(rx);
            transform.setLry(HEIGHT + uly);
            transform.setLlx(lx);
            transform.setLly(HEIGHT + ury);
        }
    };

}
