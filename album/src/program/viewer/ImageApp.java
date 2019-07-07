package program.viewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import program.model.ImageFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A display shelf of images using the PerspectiveTransform effect.
 * This was adapted from Java SE Development Kit 8u152 Demos and Samples in DisplayShelf under Ensemble8 section.
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

public class ImageApp extends Application {
    private static final double WIDTH = 200, HEIGHT = 250;
    public static ArrayList<Image> images = new ArrayList<>();
    public static ArrayList<ImageFile> imageFiles = new ArrayList<>();
    public static String path;
    public static BorderPane root = new BorderPane();

    /**
     * Java main for when running without JavaFX launcher
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Display image in images in DisplayShelf
     */
    public static Parent createContent() {
        // load images
        if (images.size() > 0) {
            DisplayShelf displayShelf = new DisplayShelf(images);
            displayShelf.setPrefSize(WIDTH, HEIGHT);

            String displayShelfCss = ImageApp.class.getResource("DisplayShelf.css").toExternalForm();
            displayShelf.getStylesheets().add(displayShelfCss);
            return displayShelf;
        }
        return null;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader GUIBuilderLoader = new FXMLLoader(getClass().getResource("/program/viewer/GUIBuilder.fxml"));
            root.setTop(createContent());
            root.setCenter(GUIBuilderLoader.load());
            primaryStage.sizeToScene();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("GUIBuilder not found!");
        }
    }
}
