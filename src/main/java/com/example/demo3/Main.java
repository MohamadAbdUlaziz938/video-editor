package com.example.demo3;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Main extends Application {
    private List<BufferedImage> upBar = new LinkedList<BufferedImage>();
    HBox hBox = new HBox();
    File selectedFile;
    File watermark;
    HBox mediaHbox = new HBox();
    File firstVideo;
    Stack<File> history = new Stack<File>();

    @Override
    public void start(Stage primaryStage) {
        try {

            Button refreshVideoImages = new Button("refresh images bar");
            refreshVideoImages.setOnAction(actionEvent -> {
                upBar.clear();
                hBox.getChildren().removeAll();
                try {
                    VideoImages videoImages = new VideoImages(selectedFile);
                    upBar.addAll(videoImages.perform());
                    System.out.println(upBar.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                List<ImageView> imageViews = new LinkedList<>();
                for (int i = 0; i < upBar.size() - 2; i += 2) {
                    imageViews.add(new ImageView(SwingFXUtils.toFXImage(upBar.get(i), null)));
                    imageViews.get(i / 2).setFitHeight(50);
                    imageViews.get(i / 2).setFitWidth(50);
                    HBox.setHgrow(imageViews.get(i / 2), Priority.ALWAYS);
                }
                hBox.getChildren().addAll(imageViews);
            });
            hBox.setSpacing(5);
            hBox.setAlignment(Pos.BOTTOM_CENTER);
            hBox.setPrefHeight(30);
            StackPane root = new StackPane();
            root.getChildren().addAll(hBox);
            StackPane.setAlignment(hBox, Pos.BOTTOM_CENTER);
            StackPane.setMargin(hBox, new Insets(20));
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            scrollPane.hvalueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    double hmin = scrollPane.getHmin();
                    double hmax = scrollPane.getHmax();
                    double hvalue = scrollPane.getHvalue();
                    double contentWidth = scrollPane.getContent().getLayoutBounds().getWidth();
                    double viewportWidth = scrollPane.getViewportBounds().getWidth();
                    double hoffset = Math.max(0, contentWidth - viewportWidth) * (hvalue - hmin) / (hmax - hmin);
                    hBox.setLayoutX(hoffset);
                    hBox.requestLayout();
                }
            });

            scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    double vmin = scrollPane.getVmin();
                    double vmax = scrollPane.getVmax();
                    double vvalue = scrollPane.getVvalue();
                    double contentHeight = scrollPane.getContent().getLayoutBounds().getHeight();
                    double viewportHeight = scrollPane.getViewportBounds().getHeight();
                    double voffset = Math.max(0, contentHeight - viewportHeight) * (vvalue - vmin) / (vmax - vmin);
                    hBox.setLayoutY(voffset);
                    hBox.requestLayout();
                }
            });
            VBox vbox = new VBox();
            vbox.setPrefHeight(400.0);
            vbox.setPrefWidth(600.0);
            FileChooser fileChooser = new FileChooser();

            Button button = new Button("Select File");
            button.setOnAction(e -> {
                mediaHbox.getChildren().clear();
                selectedFile = fileChooser.showOpenDialog(primaryStage);
                history.push(selectedFile);
                Media media = new Media(history.peek().toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setFitHeight(600);
                mediaView.setFitWidth(600);
                mediaHbox.getChildren().add(mediaView);

            });
            HBox cutHBox = new HBox();
            Button cutButton = new Button("cut");
            cutHBox.getChildren().add(cutButton);
            cutButton.setOnAction(e -> {
                TextField from = new TextField();
                TextField to = new TextField();
                Button submit = new Button("submit");
                cutHBox.getChildren().addAll(from, to, submit);
                submit.setOnAction(event -> {
                    int fromCut = Integer.parseInt(from.getText());
                    int toCut = Integer.parseInt(to.getText());
                    try {
                        mediaHbox.getChildren().clear();
                        VideoCutter videoCutter = new VideoCutter(selectedFile);
                        history.push(videoCutter.perform(fromCut, toCut));
                        Media media = new Media(history.peek().toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setAutoPlay(true);
                        MediaView mediaView = new MediaView(mediaPlayer);
                        mediaView.setFitHeight(600);
                        mediaView.setFitWidth(600);
                        mediaHbox.getChildren().add(mediaView);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            });
            HBox moveHbox = new HBox();
            Button moveButton = new Button("move");
            moveHbox.getChildren().add(moveButton);
            moveButton.setOnAction(ev -> {
                TextField from = new TextField();
                TextField to = new TextField();
                TextField destination = new TextField();
                Button submitMove = new Button("submit");
                moveHbox.getChildren().addAll(from, to, destination, submitMove);
                submitMove.setOnAction(ee -> {
                    try {
                        mediaHbox.getChildren().clear();
                        VideoMover videoMover = new VideoMover(selectedFile);
                        history.push(videoMover.perform(Integer.parseInt(from.getText()), Integer.parseInt(to.getText()), Integer.parseInt(destination.getText())));
                        Media media = new Media(history.peek().toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setAutoPlay(true);
                        MediaView mediaView = new MediaView(mediaPlayer);
                        mediaView.setFitHeight(600);
                        mediaView.setFitWidth(600);
                        mediaHbox.getChildren().add(mediaView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
            Button waterMark = new Button("add watermark");
            waterMark.setOnAction(e -> {
                watermark = fileChooser.showOpenDialog(primaryStage);
                try {
                    mediaHbox.getChildren().clear();
                    File f2 = history.peek();
                    VideoWaterMarker videoWaterMarker = new VideoWaterMarker(selectedFile);
                    File f = videoWaterMarker.perform(watermark);
                    if(f.equals(f2)) System.out.println("o");
                    history.push(f);
                    Media media = new Media(history.peek().toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setAutoPlay(true);
                    MediaView mediaView = new MediaView(mediaPlayer);
                    mediaView.setFitHeight(600);
                    mediaView.setFitWidth(600);
                    mediaHbox.getChildren().add(mediaView);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            HBox frameRateHbox = new HBox();
            Button frameRateButton = new Button("frame rate");
            frameRateHbox.getChildren().add(frameRateButton);
            frameRateButton.setOnAction(cc -> {
                TextField frameRate = new TextField();
                Button submit = new Button("submit");
                frameRateHbox.getChildren().addAll(frameRate, submit);
                submit.setOnAction(kk -> {
                    VideoFrameRater videoFrameRater = null;
                    try {
                        mediaHbox.getChildren().clear();
                        videoFrameRater = new VideoFrameRater(selectedFile);
                        history.push(videoFrameRater.perform(Integer.parseInt(frameRate.getText())));
                        Media media = new Media(history.peek().toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setAutoPlay(true);
                        MediaView mediaView = new MediaView(mediaPlayer);
                        mediaView.setFitHeight(600);
                        mediaView.setFitWidth(600);
                        mediaHbox.getChildren().add(mediaView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
            HBox combineHbox = new HBox();
            Button combineButton = new Button("combine Two Videos");
            combineHbox.getChildren().add(combineButton);
            combineButton.setOnAction(event -> {
                Button first = new Button("first video");
                Button submit = new Button("submit");
                combineHbox.getChildren().addAll(first, submit);
                first.setOnAction(event1 -> {
                    firstVideo = fileChooser.showOpenDialog(primaryStage);
                });
                submit.setOnAction(eventSubmit -> {
                    try {
                        mediaHbox.getChildren().clear();
                        VideoMerger videoMerger = new VideoMerger(selectedFile);
                        history.push(videoMerger.perform(firstVideo));
                        Media media = new Media(history.peek().toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setAutoPlay(true);
                        MediaView mediaView = new MediaView(mediaPlayer);
                        mediaView.setFitHeight(600);
                        mediaView.setFitWidth(600);
                        mediaHbox.getChildren().add(mediaView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
            HBox imagesToVideoHbox = new HBox();
            Button selectImages = new Button("select images");
            imagesToVideoHbox.getChildren().add(selectImages);
            selectImages.setOnAction(actionEvent -> {
                List<File> images = fileChooser.showOpenMultipleDialog(primaryStage);
                Button submit = new Button("submit");
                imagesToVideoHbox.getChildren().add(submit);
                submit.setOnAction(actionEvent1 -> {
                    mediaHbox.getChildren().clear();
                    ImageToVideo imageToVideo = new ImageToVideo();
                    try {
                        history.push(imageToVideo.perform(images));
                        Media media = new Media(history.peek().toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setAutoPlay(true);
                        MediaView mediaView = new MediaView(mediaPlayer);
                        mediaView.setFitHeight(600);
                        mediaView.setFitWidth(600);
                        mediaHbox.getChildren().add(mediaView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
            });
            Button undo = new Button("undo");
            undo.setOnAction(actionEvent -> {
                if (!history.isEmpty()) {
                    history.pop();
                    if (!history.isEmpty()) {
                        Media media = new Media(history.peek().toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setAutoPlay(true);
                        MediaView mediaView = new MediaView(mediaPlayer);
                        mediaView.setFitHeight(600);
                        mediaView.setFitWidth(600);
                        mediaHbox.getChildren().clear();
                        mediaHbox.getChildren().add(mediaView);
                    }
                }

            });
            vbox.getChildren().addAll(undo, refreshVideoImages, scrollPane, button, cutHBox, moveHbox, waterMark, frameRateHbox, mediaHbox, combineHbox, imagesToVideoHbox);

            Scene scene = new Scene(vbox, 600, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}