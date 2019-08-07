package it.unibs.dii.pajc.pig.client.utility;

import java.io.File;

public class UtilityConstant {

    public static final String RESOURCES_LOGO = "/images/logo.png";
    public static final String RESOURCES_CONNECTION_SYMBOL = "/images/connection_symbol.png";
    public static final String RESOURCES_FAVORITE_SYMBOL = "/images/favorite_symbol.png";
    public static final String RESOURCES_HELP_SYMBOL = "/images/help_symbol.png";
    public static final String RESOURCES_UNMARKED_FAVORITE_SYMBOL = "/images/unmarked_favorite_symbol.png";
    public static final String RESOURCES_TRASH_SYMBOL = "/images/trash_symbol.png";

    public static final String APPLICATION_DATAFOLDER_PATH = System.getProperty("user.home") + File.separator + ".PIG" + File.separator;
    public static final String SERVER_SELECTION_DATA_PATH = APPLICATION_DATAFOLDER_PATH + "srvcnct.dat";

    public static final int ICON_DIMENSION_16 = 16;
    public static final int ICON_DIMENSION_32 = 32;
    public static final int ICON_DIMENSION_48 = 48;
    public static final int ICON_DIMENSION_64 = 64;
    public static final int ICON_DIMENSION_128 = 128;
    public static final int ICON_DIMENSION_256 = 256;

    public static final String SINGLE_BYTE_IP_REGEX = "([1-9]?[0-9]|(1[0-9]|2[0-4])[0-9]|25[0-5])";
    public static final String IPV4_REGEX_VERIFIER = SINGLE_BYTE_IP_REGEX+"\\."+SINGLE_BYTE_IP_REGEX+"\\."+SINGLE_BYTE_IP_REGEX+"\\."+SINGLE_BYTE_IP_REGEX;
}
