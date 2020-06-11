package net.modificationstation.stationloader.common.api;

import net.modificationstation.stationloader.event.common.PreInit;
import org.apache.logging.log4j.Logger;

public interface StationMod extends PreInit {

    default void setLogger(Logger log) {
        logHandler.setLogger(log);
    }

    default Logger getLogger() {
        return logHandler.getLogger();
    }

    interface LoggerHandler {

        Logger getLogger();

        void setLogger(Logger log);
    }

    LoggerHandler logHandler = new LoggerHandler() {

        @Override
        public Logger getLogger() {
            return log;
        }

        @Override
        public void setLogger(Logger log) {
            this.log = log;
        }

        private Logger log;
    };
}
