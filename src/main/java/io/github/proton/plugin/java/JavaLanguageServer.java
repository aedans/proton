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

public final class JavaLanguageServer implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(JavaLanguageServer.class);
    private static final File file;
    private static final File launcherJar;

    static {
        file = Arrays.stream(Objects.requireNonNull(new File(".proton").listFiles()))
            .filter(x -> x.getName().startsWith("jdt-language-server") && x.isDirectory())
            .findFirst()
            .orElseThrow();

        launcherJar = Arrays.stream(Objects.requireNonNull(new File(file, "plugins").listFiles()))
            .filter(x -> x.getName().startsWith("org.eclipse.equinox.launcher_"))
            .findFirst()
            .orElseThrow()
            .getAbsoluteFile();

        logger.info("Launcher jar is " + launcherJar);
    }

    public static JavaLanguageServer INSTANCE;

    public final LanguageServer server;
    public final EventSource<List<Diagnostic>> diagnostics = new EventSource<>();
    public final InitializeResult initializeResult;
    private final Process process;

    public JavaLanguageServer(InitializeParams params) throws IOException, ExecutionException, InterruptedException {
        process = Runtime.getRuntime().exec("java " +
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
            }

            @Override
            public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
                return null;
            }

            @Override
            public void logMessage(MessageParams message) {
                if (message.getType() == MessageType.Info) {
                    logger.info(message.getMessage());
                } else if (message.getType() == MessageType.Warning) {
                    logger.warn(message.getMessage());
                } else if (message.getType() == MessageType.Error) {
                    logger.error(message.getMessage());
                } else {
                    logger.debug(message.getMessage());
                }
            }
        }, process.getInputStream(), process.getOutputStream());

        launcher.startListening();

        server = launcher.getRemoteProxy();
        initializeResult = server.initialize(params).get();
        logger.info(initializeResult.toString());
    }

    @Override
    public void close() {
        server.exit();
    }
}
