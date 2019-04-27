package com.tattyseal.compactstorage.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by tattyseal on 07/09/15.
 */
public class LogHelper {
	public static final Logger logger = LogManager.getLogger("CompactStorage");

	public static void dump(String string) {
		logger.info("COMPACTSTORAGE DATA DUMP: " + string);
	}
}
