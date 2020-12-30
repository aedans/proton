package io.github.proton;

import io.github.proton.plugin.Plugins;
import io.github.proton.plugin.java.JavaLanguageServer;
import io.github.proton.ui.WorkspaceUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.concurrent.ExecutionException;

public final class Proton extends Application {
    private static File home = new File(".");

    public Proton() throws InterruptedException, ExecutionException, IOException {
        JavaLanguageServer.init();
    }

    @Override
    public void start(Stage primaryStage) {
        Plugins.start();
        WorkspaceUI ui = new WorkspaceUI(home);
        Scene scene = new Scene(ui, 720, 480);
        scene.getStylesheets().add(Proton.class.getResource("proton.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Proton");
        primaryStage.show();
    }

    @Override
    public void stop() {
        JavaLanguageServer.server.exit();
    }

    public static void main(String[] args) {
        if (args.length != 0)
            home = new File(args[0]);
        launch(args);
    }
}
