package com.pisano.tennispadelstore.services.config;

import java.util.Calendar;
import java.util.logging.Level;

import com.pisano.tennispadelstore.model.dao.DAOFactory;
public class Configuration {

    /* Configurazione database */
    public static final String DAO_IMPL=DAOFactory.MYSQLJDBCIMPL;
    public static final String DATABASE_DRIVER="com.mysql.cj.jdbc.Driver";
    public static final String SERVER_TIMEZONE=Calendar.getInstance().getTimeZone().getID();
    public static final String
            DATABASE_URL="jdbc:mysql://localhost/tennispadelstore?user=root&password=28112002&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone="+SERVER_TIMEZONE;

    /* Session Configuration */
    public static final String COOKIE_IMPL=DAOFactory.COOKIEIMPL;

    /* Logger Configuration */
    public static final String GLOBAL_LOGGER_NAME="tennispadelstore";
    public static final String GLOBAL_LOGGER_FILE="/Users/user/TennisPadelStore/logs/tpstorelog.txt";
    public static final Level GLOBAL_LOGGER_LEVEL=Level.ALL;

}
