package it.unibs.dii.pajc.pig.client.utility;

import java.io.File;

public class UtilityConstant {

    public static final String RESOURCES_CONNECTION_SYMBOL = "/images/connection_symbol.png";
    public static final String RESOURCES_FAVORITE_SYMBOL = "/images/favorite_symbol.png";
    public static final String RESOURCES_HELP_SYMBOL = "/images/help_symbol.png";
    public static final String RESOURCES_UNMARKED_FAVORITE_SYMBOL = "/images/unmarked_favorite_symbol.png";
    public static final String RESOURCES_TRASH_SYMBOL = "/images/trash_symbol.png";

    public static final String APPLICATION_DATAFOLDER_PATH = System.getProperty("user.home") + File.separator + ".PIG" + File.separator;
    public static final String SERVER_SELECTION_DATA_PATH = APPLICATION_DATAFOLDER_PATH + "srvcnct.dat";
}
