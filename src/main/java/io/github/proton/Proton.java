package io.github.proton;

import io.github.proton.api.Plugins;
import io.github.proton.plugin.java.JavaLanguageServer;
import io.github.proton.ui.WorkspaceUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.eclipse.lsp4j.InitializeParams;

import java.io.*;
import java.util.concurrent.ExecutionException;

public final class Proton extends Application {
    private static File home = new File(".");

    public Proton() throws InterruptedException, ExecutionException, IOException {
        JavaLanguageServer.instance = new JavaLanguageServer(new InitializeParams());
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
    public void stop() throws Exception {
        JavaLanguageServer.instance.close();
        super.stop();
    }

    public static void main(String[] args) {
        if (args.length != 0)
            home = new File(args[0]);
        launch(args);
    }
}
