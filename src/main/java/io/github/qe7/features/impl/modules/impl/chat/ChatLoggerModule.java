package io.github.qe7.features.impl.modules.impl.chat;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.IncomingPacketEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet3Chat;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ChatLoggerModule extends Module {
	private static final Logger chatLogger;

	static {
		ConsoleLogManager.init();
		chatLogger = Logger.getLogger("OsirisLogger");
	}

    public ChatLoggerModule() {
        super("Chat Logger", "Logs chat messages", ModuleCategory.CHAT);
    }

    @EventLink
    public final Listener<IncomingPacketEvent> incomingPacketEventListener = event -> {
        if (event.getPacket() instanceof Packet3Chat) {
            final Packet3Chat packet = (Packet3Chat) event.getPacket();
            chatLogger.info(packet.message);
        }
    };

    static class ConsoleLogManager {
        public static Logger logger = Logger.getLogger("OsirisLogger");

        public static void init() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

            ConsoleLogFormatter consoleLogFormatter0 = new ConsoleLogFormatter();

            logger.setUseParentHandlers(false);

            ConsoleHandler consoleHandler1 = new ConsoleHandler();

            consoleHandler1.setFormatter(consoleLogFormatter0);

            logger.addHandler(consoleHandler1);

            String logName = dateFormat.format(System.currentTimeMillis()) + "_bot.log";

            try {
                FileHandler fileHandler = new FileHandler("OsirisChatLogs" + File.separator + logName, true);
                fileHandler.setFormatter(consoleLogFormatter0);
                logger.addHandler(fileHandler);
            } catch (Exception exception3) {
                logger.log(Level.WARNING, "Failed to log to " + "OsirisChatLogs" + File.separator + logName, exception3);
            }

        }
    }

    static class ConsoleLogFormatter extends Formatter {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

        public String format(LogRecord logRecord1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Minecraft.getMinecraft().gameSettings.lastServer).append(" ");
            stringBuilder.append(this.dateFormat.format(logRecord1.getMillis()));
            stringBuilder.append(" [Osiris] ");
            stringBuilder.append(logRecord1.getMessage());
            stringBuilder.append('\n');
            Throwable throwable4 = logRecord1.getThrown();

            if(throwable4 != null) {
                StringWriter stringWriter5 = new StringWriter();
                throwable4.printStackTrace(new PrintWriter(stringWriter5));
                stringBuilder.append(stringWriter5);
            }

            return stringBuilder.toString();
        }
    }
}