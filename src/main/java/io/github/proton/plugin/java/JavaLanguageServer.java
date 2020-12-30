package io.github.proton.plugin.java;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.*;
import org.reactfx.EventSource;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public final class JavaLanguageServer {
    private static final Logger logger = LoggerFactory.getLogger(JavaLanguageServer.class);
    public static LanguageServer server;
    public static InitializeResult initializeResult;
    public static final EventSource<List<Diagnostic>> diagnostics = new EventSource<>();

    public static void init() throws IOException, ExecutionException, InterruptedException {
        File file = Arrays.stream(Objects.requireNonNull(new File(".proton").listFiles()))
            .filter(x -> x.getName().startsWith("jdt-language-server") && x.isDirectory())
            .findFirst()
            .orElseThrow();

        File launcherJar = Arrays.stream(Objects.requireNonNull(new File(file, "plugins").listFiles()))
            .filter(x -> x.getName().startsWith("org.eclipse.equinox.launcher_"))
            .findFirst()
            .orElseThrow()
            .getAbsoluteFile();

        logger.info("Launcher jar is " + launcherJar);

        Process process = Runtime.getRuntime().exec("java " +
            "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n " +
            "-Declipse.application=org.eclipse.jdt.ls.core.id1 " +
            "-Dosgi.bundles.defaultStartLevel=4 " +
            "-Declipse.product=org.eclipse.jdt.ls.core.product " +
            "-Dlog.level=ALL " +
            "-noverify " +
            "-Xmx1G " +
            "-jar " + launcherJar + " " +
            "-configuration ./config_win " +
            "--add-modules=ALL-SYSTEM " +
            "--add-opens java.base/java.util=ALL-UNNAMED " +
            "--add-opens java.base/java.lang=ALL-UNNAMED", null, file);

        process.onExit().whenComplete((p, throwable) -> {
            Scanner scanner = new Scanner(process.getErrorStream());
            while (scanner.hasNextLine()) {
                logger.error(scanner.nextLine());
            }
        });

        Launcher<LanguageServer> launcher = LSPLauncher.createClientLauncher(new LanguageClient() {
            @Override
            public void telemetryEvent(Object object) {
            }

            @Override
            public void publishDiagnostics(PublishDiagnosticsParams d) {
                diagnostics.push(d.getDiagnostics());
            }

            @Override
            public void showMessage(MessageParams messageParams) {
                logMessage(messageParams);
            }

            @Override
            public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
                return null;
            }

            @Override
            public void logMessage(MessageParams messageParams) {
                if (messageParams.getType() == MessageType.Info) {
                    logger.info(messageParams.getMessage());
                } else if (messageParams.getType() == MessageType.Warning) {
                    logger.warn(messageParams.getMessage());
                } else if (messageParams.getType() == MessageType.Error) {
                    logger.error(messageParams.getMessage());
                } else {
                    logger.debug(messageParams.getMessage());
                }
            }
        }, process.getInputStream(), process.getOutputStream());

        launcher.startListening();

        server = launcher.getRemoteProxy();
        initializeResult = server.initialize(new InitializeParams()).get();
        logger.info(initializeResult.toString());
    }
}
