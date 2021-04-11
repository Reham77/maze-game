package edu.maze.game.screen;

import edu.maze.game.builder.DFSMazeBuilder;
import edu.maze.game.builder.KruskalMazeBuilder;
import edu.maze.game.builder.MazeBuilder;
import edu.maze.game.entity.Board;
import edu.maze.game.entity.Cell;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;

public class MazeGameScene {

    public static int counter = 0;

    public static int getDistance(int rows) {
        if (rows == 8)
            return 90;
        else if (rows == 14)
            return 50;
        else
            return 35;
    }

    public static Scene create(int rows, int cols, Stage stage) throws FileNotFoundException {
        int distance = getDistance(rows);

        MazeBuilder builder = new KruskalMazeBuilder(rows, cols);
        Board board = builder.build();

        stage.setTitle("Maze Game");

        Group group = new Group();
        GridPane gridPane = new GridPane();

        for (int x = 0; x <= rows; x++) {
            for (int y = 0; y <= cols; y++) {
                Group stackPane = new Group();
                // horizontal
                Line horizontal = new Line(0, 0, distance, 0);
                if (board.isWallRemoved(new Cell(x, y), Board.UP) || y == rows) {
                    horizontal.setStyle("-fx-stroke: #ff0000 ; -fx-stroke-width: 5");
                    horizontal.setOpacity(0);
                } else {
                    horizontal.setStyle("-fx-stroke: #0d3b12 ; -fx-stroke-width: 5");
                }

                // vertical
                Line vertical = new Line(0, 0, 0, distance);
                if (board.isWallRemoved(new Cell(x, y), Board.LEFT) || x == cols) {
                    vertical.setStyle("-fx-stroke: #ff0000 ; -fx-stroke-width: 5");
                    vertical.setOpacity(0);
                } else {
                    vertical.setStyle("-fx-stroke: #022d26 ; -fx-stroke-width: 5");
                }
                //          rgb(240,248,255)
                stackPane.getChildren().addAll(vertical, horizontal);
                gridPane.add(stackPane, y, x);
            }
        }

        Group g = new Group();
        Button button = new Button("I Quit");
        button.setMaxWidth(150);
        button.setOnAction(event -> {
            stage.setScene(HomePageScene.createHomePageScene(stage));
        });
        button.setFocusTraversable(false);
        g.getChildren().add(button);
        gridPane.add(g, cols + 2, rows / 3, 1, 3);

        Image image = new Image("file:src/main/resources/mouse.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setX((distance / 10) + 5);
        imageView.setY((distance / 10) + 5);
        imageView.setFitHeight(distance + 5);
        imageView.setFitWidth(distance + 5);
        imageView.setPreserveRatio(true);
        imageView.setFocusTraversable(true);

        group.getChildren().addAll(imageView, gridPane);

        Scene scene = new Scene(group, 940, 780, Color.rgb(255, 250, 255));

        TranslateTransition translateTransition = new TranslateTransition();

        scene.setOnKeyPressed(key -> {
            if (translateTransition.getStatus().equals(Animation.Status.RUNNING)) {
                System.out.println("Wait!!!");
                return;
            }
            double y = imageView.getTranslateY();
            double x = imageView.getTranslateX();

            translateTransition.setDuration(Duration.millis(200));
            translateTransition.setNode(imageView);

            int indi = (int) y / (distance + 5);
            int indj = (int) x / (distance + 5);

            System.out.println(x + " " + y + " " + indi + " " + indj);
            if (key.getCode() == KeyCode.UP) {
                if (board.isWallRemoved(new Cell(indi, indj), Board.UP)) {
                    translateTransition.setToY(y - (distance + 5));
                }
            } else if (key.getCode() == KeyCode.DOWN) {
                if (board.isWallRemoved(new Cell(indi, indj), Board.DOWN)) {
                    translateTransition.setToY(y + (distance + 5));
                }
            } else if (key.getCode() == KeyCode.RIGHT) {
                if (board.isWallRemoved(new Cell(indi, indj), Board.RIGHT)) {
                    translateTransition.setToX(x + distance + 5);
                }
            } else if (key.getCode() == KeyCode.LEFT) {
                if (board.isWallRemoved(new Cell(indi, indj), Board.LEFT)) {
                    translateTransition.setToX(x - (distance + 5));
                }
            } else {
                return;
            }
            translateTransition.play();
            System.out.println("done");
        });
        //         stage.setResizable(false);
        scene.getStylesheets().add("style.css");
        return scene;
    }

}
