package net.modificationstation.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.modificationstation.classloader.LogFormatter;

/**
 * A clean logger
 * 
 * @author mine_diver
 *
 */
public final class Log
{

    private static final class LoggingOutStream extends ByteArrayOutputStream
    {
        private final Logger log;
        private final StringBuilder currentMessage;

        private LoggingOutStream(Logger log)
        {
            this.log = log;
            this.currentMessage = new StringBuilder();
        }

        @Override
        public final void flush() throws IOException
        {
            final String record;
            synchronized(this)
            {
                super.flush();
                record = this.toString();
                super.reset();

                currentMessage.append(record);
                if (currentMessage.lastIndexOf(LogFormatter.LINE_SEPARATOR)>=0)
                {
                    if (currentMessage.length()>LogFormatter.LINE_SEPARATOR.length())
                    {
                        currentMessage.setLength(currentMessage.length()-LogFormatter.LINE_SEPARATOR.length());
                        log.log(Level.INFO, currentMessage.toString());
                    }
                    currentMessage.setLength(0);
                }
            }
        }
    }
    public static Log log = new Log();

    static File minecraftHome;
    private static boolean configured;
    private Logger myLog;

    private Log() {}
    
    private static final void configureLogging()
    {
        LogManager.getLogManager().reset();
        Logger globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        globalLogger.setLevel(Level.OFF);
        log.myLog = Logger.getLogger("ClassLoader");
        Logger stdOut = Logger.getLogger("STDOUT");
        stdOut.setParent(log.myLog);
        Logger stdErr = Logger.getLogger("STDERR");
        stdErr.setParent(log.myLog);
        LogFormatter formatter = new LogFormatter();
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.parse(System.getProperty("modificationstation.classloader.log.level","INFO")));
        log.myLog.setUseParentHandlers(false);
        log.myLog.addHandler(ch);
        ch.setFormatter(formatter);
        log.myLog.setLevel(Level.ALL);
        try
        {
            File logPath = new File(minecraftHome, ClassLoadingManager.logFile);
            FileHandler fileHandler = new FileHandler(logPath.getPath());
            fileHandler.setFormatter(formatter);
            fileHandler.setLevel(Level.ALL);
            log.myLog.addHandler(fileHandler);
        }
        catch (Exception e) {}
        System.setOut(new PrintStream(new LoggingOutStream(stdOut), true));
        System.setErr(new PrintStream(new LoggingOutStream(stdErr), true));
        configured = true;
    }

    public static final void log(Level level, String format, Object... data)
    {
        if (!configured)
        {
            configureLogging();
        }
        log.myLog.log(level, String.format(format, data));
    }

    public static final void log(Level level, Throwable ex, String format, Object... data)
    {
        if (!configured)
        {
            configureLogging();
        }
        log.myLog.log(level, String.format(format, data), ex);
    }

    public static final void severe(String format, Object... data)
    {
        log(Level.SEVERE, format, data);
    }

    public static final void warning(String format, Object... data)
    {
        log(Level.WARNING, format, data);
    }

    public static final void info(String format, Object... data)
    {
        log(Level.INFO, format, data);
    }

    public static final void fine(String format, Object... data)
    {
        log(Level.FINE, format, data);
    }

    public static final void finer(String format, Object... data)
    {
        log(Level.FINER, format, data);
    }

    public static final void finest(String format, Object... data)
    {
        log(Level.FINEST, format, data);
    }
    public final Logger getLogger()
    {
        return myLog;
    }
}
