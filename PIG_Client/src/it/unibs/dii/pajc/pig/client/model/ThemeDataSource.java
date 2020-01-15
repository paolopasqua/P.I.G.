package it.unibs.dii.pajc.pig.client.model;

import it.unibs.dii.pajc.pig.client.utility.UtilityConstant;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ThemeDataSource {

    private static ThemeDataSource _instance = null;

    private Properties _defTheme;
    private Properties _theme;

    public static ThemeDataSource getInstance() {
        if (_instance == null)
            _instance = new ThemeDataSource();
        return _instance;
    }

    private ThemeDataSource() {
        _defTheme = new Properties();
        try {
            //_defTheme.load(new FileInputStream(getClass().getResource(UtilityConstant.RESOURCES_DEFAULT_THEME_SETTING).getPath()));
            _defTheme.load(getClass().getResourceAsStream(UtilityConstant.RESOURCES_DEFAULT_THEME_SETTING));
        } catch (IOException e) {
            e.printStackTrace();
        }

        _theme = new Properties(_defTheme); //set the default properties as default to cover the updates of the default file

        File settings = new File(UtilityConstant.THEME_SETTINGS_DATA_PATH);
        try {
            //Checks if the settings file exists. If not, then creates it with the default settings.
            if (!settings.exists()) {
                settings.createNewFile();
                _defTheme.store(new FileOutputStream(UtilityConstant.THEME_SETTINGS_DATA_PATH), "PIG Theme Settings");
            }

            _theme.load(new FileInputStream(UtilityConstant.THEME_SETTINGS_DATA_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Border getBorderFromProperty(String text, String type, String width, String color, String margin, String padding, String typeDef, String widthDef, String colorDef, String marginDef, String paddingDef) {
        String typeProp = checkProperty(type) ? type : typeDef;
        String widthProp = checkProperty(width) ? width : widthDef;
        String colorProp = checkProperty(color) ? color : colorDef;
        String marginProp = checkProperty(margin) ? margin : marginDef;
        String paddingProp = checkProperty(padding) ? padding : paddingDef;

        return getBorderFromProperty(text, typeProp, widthProp, colorProp, marginProp, paddingProp);
    }

    private Border getBorderFromProperty(String text, String typeProp, String widthProp, String colorProp, String marginProp, String paddingProp) {
        String borderType = _theme.getProperty(typeProp);
        String borderWidth = _theme.getProperty(widthProp);
        String marginWidth = _theme.getProperty(marginProp);
        String paddingWidth = _theme.getProperty(paddingProp);
        Color borderColor = getColorFromProperty(colorProp);
        boolean withText = text != null && !text.trim().isEmpty();

        if (borderType == null)
            borderType = "none";
        if (borderWidth == null)
            borderWidth = "0";
        if (marginWidth == null)
            marginWidth = "0";
        if (paddingWidth == null)
            paddingWidth = "0";

        int borderWidthInt = Integer.parseInt(borderWidth);
        int marginWidthInt = Integer.parseInt(marginWidth);
        int paddingWidthInt = Integer.parseInt(paddingWidth);

        Border border = null;
        switch (borderType) {
            case "none":
                border = BorderFactory.createEmptyBorder(borderWidthInt,borderWidthInt,borderWidthInt,borderWidthInt);
                break;

            case "solid":
                border = BorderFactory.createLineBorder(borderColor, borderWidthInt);
                break;
        }

        if (withText)
            border = BorderFactory.createTitledBorder(border, text, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, borderColor);

        if (paddingWidthInt > 0) {
            border = BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(paddingWidthInt,paddingWidthInt,paddingWidthInt,paddingWidthInt));
        }

        if (marginWidthInt > 0) {
            border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(marginWidthInt,marginWidthInt,marginWidthInt,marginWidthInt), border);
        }

        return border;
    }

    private Color getColorFromProperty(String prop, String def) {
        return checkProperty(prop) ? getColorFromProperty(prop) : getColorFromProperty(def);
    }

    private Color getColorFromProperty(String prop) {
        String colorStr = _theme.getProperty(prop);

        if (colorStr != null) {
            if (colorStr.startsWith("#")) {
                int  r=  Integer.valueOf( colorStr.substring( 1, 3 ), 16 );
                int  g=  Integer.valueOf( colorStr.substring( 3, 5 ), 16 );
                int  b=  Integer.valueOf( colorStr.substring( 5, 7 ), 16 );
                return new Color(r,g,b);
            }
            else {
                try {
                    return (Color)Color.class.getField(colorStr.toUpperCase()).get(null);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                    //no a color name
                    //do nothing and return null
                }
            }
        }

        return null;
    }

    private boolean checkProperty(String prop) {
        String value = _theme.getProperty(prop);
        return (value != null && !value.trim().isEmpty());
    }

    public Color getBaseBackground() {
        return getColorFromProperty("base.background");
    }

    public Color getBaseForeground() {
        return getColorFromProperty("base.foreground");
    }

    public Color getBaseToolbarBackground() {
        return getColorFromProperty("base.toolbar.background");
    }

    public Color getBaseToolbarForeground() {
        return getColorFromProperty("base.toolbar.foreground");
    }

    public Border getBaseBorder() {
        return getBorderFromProperty(null, "base.border.type", "base.border.width", "base.border.color", "base.margin", "base.padding");
    }

    public Border getBaseToolbarBorder() {
        return getBorderFromProperty(null, "base.toolbar.border.type", "base.toolbar.border.width", "base.toolbar.border.color", "base.toolbar.margin", "base.toolbar.padding");
    }

    public Color getInputServerDataPanelBackground() {
        return getColorFromProperty("inputserverdatapanel.background");
    }

    public Color getInputServerDataPanelForeground() {
        return getColorFromProperty("inputserverdatapanel.foreground");
    }

    public Border getInputServerDataPanelBorder(String title) {
        return getBorderFromProperty(title, "inputserverdatapanel.border.type", "inputserverdatapanel.border.width", "inputserverdatapanel.border.color", "inputserverdatapanel.margin", "inputserverdatapanel.padding");
    }

    public Color getInputServerDataPanelButtonBackground() {
        return getColorFromProperty("inputserverdatapanel.button.background");
    }

    public Color getInputServerDataPanelButtonForeground() {
        return getColorFromProperty("inputserverdatapanel.button.foreground");
    }

    public Border getInputServerDataPanelButtonBorder() {
        return getBorderFromProperty(null, "inputserverdatapanel.button.border.type", "inputserverdatapanel.button.border.width", "inputserverdatapanel.button.border.color", "inputserverdatapanel.button.margin", "inputserverdatapanel.button.padding");
    }

    public Color getListManagerPanelBackground() {
        return getColorFromProperty("listmanagerpanel.background");
    }

    public Color getListManagerPanelForeground() {
        return getColorFromProperty("listmanagerpanel.foreground");
    }

    public Border getListManagerPanelBorder() {
        return getBorderFromProperty(null, "listmanagerpanel.border.type", "listmanagerpanel.border.width", "listmanagerpanel.border.color", "listmanagerpanel.margin", "listmanagerpanel.padding");
    }

    public Color getListManagerPanelToolbarBackground() {
        return getColorFromProperty("listmanagerpanel.toolbar.background");
    }

    public Color getListManagerPanelToolbarForeground() {
        return getColorFromProperty("listmanagerpanel.toolbar.foreground");
    }

    public Border getListManagerPanelToolbarBorder() {
        return getBorderFromProperty(null, "listmanagerpanel.toolbar.border.type", "listmanagerpanel.toolbar.border.width", "listmanagerpanel.toolbar.border.color", "listmanagerpanel.toolbar.margin", "listmanagerpanel.toolbar.padding");
    }

    public Color getListManagerPanelListBackground() {
        return getColorFromProperty("listmanagerpanel.list.background");
    }

    public Color getListManagerPanelListForeground() {
        return getColorFromProperty("listmanagerpanel.list.foreground");
    }

    public Border getListManagerPanelListBorder() {
        return getBorderFromProperty(null, "listmanagerpanel.list.border.type", "listmanagerpanel.list.border.width", "listmanagerpanel.list.border.color", "listmanagerpanel.list.margin", "listmanagerpanel.list.padding");
    }

    public Color getListManagerPanelListScrollBackground() {
        return getColorFromProperty("listmanagerpanel.list.scroll.background");
    }

    public Color getListManagerPanelListScrollForeground() {
        return getColorFromProperty("listmanagerpanel.list.scroll.foreground");
    }

    public Color getDialogBackground() {
        return getColorFromProperty("dialog.background");
    }

    public Color getDialogForeground() {
        return getColorFromProperty("dialog.foreground");
    }

    public Border getDialogBorder() {
        return getBorderFromProperty(null, "dialog.border.type", "dialog.border.width", "dialog.border.color", "dialog.margin", "dialog.padding");
    }

    public Color getDialogButtonPanelBackground() {
        return getColorFromProperty("dialog.buttonpanel.background");
    }

    public Color getDialogButtonPanelForeground() {
        return getColorFromProperty("dialog.buttonpanel.foreground");
    }

    public Border getDialogButtonPanelBorder() {
        return getBorderFromProperty(null, "dialog.buttonpanel.border.type", "dialog.buttonpanel.border.width", "dialog.buttonpanel.border.color", "dialog.buttonpanel.margin", "dialog.buttonpanel.padding");
    }

    public Color getDialogButtonBackground() {
        return getColorFromProperty("dialog.button.background");
    }

    public Color getDialogButtonForeground() {
        return getColorFromProperty("dialog.button.foreground");
    }

    public Border getDialogButtonBorder() {
        return getBorderFromProperty(null, "dialog.button.border.type", "dialog.button.border.width", "dialog.button.border.color", "dialog.button.margin", "dialog.button.padding");
    }

    public Color getDialogActivityBackground() {
        return getColorFromProperty("dialog.activity.background");
    }

    public Color getDialogActivityForeground() {
        return getColorFromProperty("dialog.activity.foreground");
    }

    public Border getDialogActivityBorder() {
        return getBorderFromProperty(null, "dialog.activity.border.type", "dialog.activity.border.width", "dialog.activity.border.color", "dialog.activity.margin", "dialog.activity.padding");
    }

    public Color getDialogActivityTimingDataBackground() {
        return getColorFromProperty("dialog.activity.timingdata.background");
    }

    public Color getDialogActivityTimingDataForeground() {
        return getColorFromProperty("dialog.activity.timingdata.foreground");
    }

    public Border getDialogActivityTimingDataBorder(String title) {
        return getBorderFromProperty(title, "dialog.activity.timingdata.border.type", "dialog.activity.timingdata.border.width", "dialog.activity.timingdata.border.color", "dialog.activity.timingdata.margin", "dialog.activity.timingdata.padding");
    }

    public Color getDialogRuleBackground() {
        return getColorFromProperty("dialog.rule.background");
    }

    public Color getDialogRuleForeground() {
        return getColorFromProperty("dialog.rule.foreground");
    }

    public Border getDialogRuleBorder() {
        return getBorderFromProperty(null, "dialog.rule.border.type", "dialog.rule.border.width", "dialog.rule.border.color", "dialog.rule.margin", "dialog.rule.padding");
    }

    public Color getConnectionFormBackground() {
        return getColorFromProperty("connectionform.background");
    }

    public Color getConnectionFormForeground() {
        return getColorFromProperty("connectionform.foreground");
    }

    public Border getConnectionFormBorder() {
        return getBorderFromProperty(null, "connectionform.border.type", "connectionform.border.width", "connectionform.border.color", "connectionform.margin", "connectionform.padding");
    }

    public Color getConnectionFormNewServerBackground() {
        return getColorFromProperty("connectionform.newserver.background", "inputserverdatapanel.background");
    }

    public Color getConnectionFormNewServerForeground() {
        return getColorFromProperty("connectionform.newserver.foreground", "inputserverdatapanel.foreground");
    }

    public Border getConnectionFormNewServerBorder() {
        return getBorderFromProperty(null, "connectionform.newserver.border.type", "connectionform.newserver.border.width", "connectionform.newserver.border.color",
                              "connectionform.newserver.margin", "connectionform.newserver.padding", "inputserverdatapanel.border.type",
                            "inputserverdatapanel.border.width", "inputserverdatapanel.border.color", "inputserverdatapanel.margin",
                          "inputserverdatapanel.padding");
    }

    public Color getConnectionFormNewServerButtonBackground() {
        return getColorFromProperty("connectionform.newserver.button.background", "inputserverdatapanel.button.background");
    }

    public Color getConnectionFormNewServerButtonForeground() {
        return getColorFromProperty("connectionform.newserver.button.foreground", "inputserverdatapanel.button.foreground");
    }

    public Border getConnectionFormNewServerButtonBorder() {
        return getBorderFromProperty(null, "connectionform.newserver.button.border.type", "connectionform.newserver.button.border.width", "connectionform.newserver.button.border.color",
                "connectionform.newserver.button.margin", "connectionform.newserver.button.padding", "inputserverdatapanel.button.border.type",
                "inputserverdatapanel.button.border.width", "inputserverdatapanel.button.border.color", "inputserverdatapanel.button.margin",
                "inputserverdatapanel.button.padding");
    }

    public Color getConnectionFormListBackground() {
        return getColorFromProperty("connectionform.list.background", "listmanagerpanel.list.background");
    }

    public Color getConnectionFormListForeground() {
        return getColorFromProperty("connectionform.list.foreground", "listmanagerpanel.list.foreground");
    }

    public Border getConnectionFormListBorder() {
        return getBorderFromProperty(null, "connectionform.list.border.type", "connectionform.list.border.width", "connectionform.list.border.color",
                "connectionform.list.margin", "connectionform.list.padding", "listmanagerpanel.list.border.type",
                "listmanagerpanel.list.border.width", "listmanagerpanel.list.border.color", "listmanagerpanel.list.margin",
                "listmanagerpanel.list.padding");
    }

    public Color getConnectionFormListToolbarBackground() {
        return getColorFromProperty("connectionform.list.toolbar.background", "listmanagerpanel.toolbar.background");
    }

    public Color getConnectionFormListToolbarForeground() {
        return getColorFromProperty("connectionform.list.toolbar.foreground", "listmanagerpanel.toolbar.foreground");
    }

    public Border getConnectionFormListToolbarBorder() {
        return getBorderFromProperty(null, "connectionform.list.toolbar.border.type", "connectionform.list.toolbar.border.width", "connectionform.list.toolbar.border.color",
                "connectionform.list.toolbar.margin", "connectionform.list.toolbar.padding", "listmanagerpanel.toolbar.border.type",
                "listmanagerpanel.toolbar.border.width", "listmanagerpanel.toolbar.border.color", "listmanagerpanel.toolbar.margin",
                "listmanagerpanel.toolbar.padding");
    }

    public Color getConnectionFormListScrollBackground() {
        return getColorFromProperty("connectionform.list.scroll.background", "listmanagerpanel.list.scroll.background");
    }

    public Color getConnectionFormListScrollForeground() {
        return getColorFromProperty("connectionform.list.scroll.foreground", "listmanagerpanel.list.scroll.foreground");
    }


    public Color getConnectionFormSearchServerBackground() {
        return getColorFromProperty("connectionform.searchserver.background", "inputserverdatapanel.background");
    }

    public Color getConnectionFormSearchServerForeground() {
        return getColorFromProperty("connectionform.searchserver.foreground", "inputserverdatapanel.foreground");
    }

    public Border getConnectionFormSearchServerBorder() {
        return getBorderFromProperty(null, "connectionform.searchserver.border.type", "connectionform.searchserver.border.width", "connectionform.searchserver.border.color",
                "connectionform.searchserver.margin", "connectionform.searchserver.padding", "inputserverdatapanel.border.type",
                "inputserverdatapanel.border.width", "inputserverdatapanel.border.color", "inputserverdatapanel.margin",
                "inputserverdatapanel.padding");
    }

    public Color getConnectionFormSearchServerButtonBackground() {
        return getColorFromProperty("connectionform.searchserver.button.background", "inputserverdatapanel.button.background");
    }

    public Color getConnectionFormSearchServerButtonForeground() {
        return getColorFromProperty("connectionform.searchserver.button.foreground", "inputserverdatapanel.button.foreground");
    }

    public Border getConnectionFormSearchServerButtonBorder() {
        return getBorderFromProperty(null, "connectionform.searchserver.button.border.type", "connectionform.searchserver.button.border.width", "connectionform.searchserver.button.border.color",
                "connectionform.searchserver.button.margin", "connectionform.searchserver.button.padding", "inputserverdatapanel.button.border.type",
                "inputserverdatapanel.button.border.width", "inputserverdatapanel.button.border.color", "inputserverdatapanel.button.margin",
                "inputserverdatapanel.button.padding");
    }


    public Color getStateFormBackground() {
        return getColorFromProperty("stateform.background");
    }

    public Color getStateFormForeground() {
        return getColorFromProperty("stateform.foreground");
    }

    public Border getStateFormBorder() {
        return getBorderFromProperty(null, "stateform.border.type", "stateform.border.width", "stateform.border.color", "stateform.margin", "stateform.padding");
    }

    public Color getStateFormWaitForDataBackground() {
        return getColorFromProperty("stateform.waitfordata.background");
    }

    public Color getStateFormWaitForDataForeground() {
        return getColorFromProperty("stateform.waitfordata.foreground");
    }

    public Border getStateFormWaitForDataBorder(String text) {
        return getBorderFromProperty(text, "stateform.waitfordata.border.type", "stateform.waitfordata.border.width", "stateform.waitfordata.border.color", "stateform.waitfordata.margin", "stateform.waitfordata.padding");
    }

    public Color getStateFormRuleListBackground() {
        return getColorFromProperty("stateform.rulelist.background", "listmanagerpanel.list.background");
    }

    public Color getStateFormRuleListForeground() {
        return getColorFromProperty("stateform.rulelist.foreground", "listmanagerpanel.list.foreground");
    }

    public Border getStateFormRuleListBorder() {
        return getBorderFromProperty(null, "stateform.rulelist.border.type", "stateform.rulelist.border.width", "stateform.rulelist.border.color",
                "stateform.rulelist.margin", "stateform.rulelist.padding", "listmanagerpanel.list.border.type",
                "listmanagerpanel.list.border.width", "listmanagerpanel.list.border.color", "listmanagerpanel.list.margin",
                "listmanagerpanel.list.padding");
    }

    public Color getStateFormRuleListToolbarBackground() {
        return getColorFromProperty("stateform.rulelist.toolbar.background", "listmanagerpanel.toolbar.background");
    }

    public Color getStateFormRuleListToolbarForeground() {
        return getColorFromProperty("stateform.rulelist.toolbar.foreground", "listmanagerpanel.toolbar.foreground");
    }

    public Border getStateFormRuleListToolbarBorder() {
        return getBorderFromProperty(null, "stateform.rulelist.toolbar.border.type", "stateform.rulelist.toolbar.border.width", "stateform.rulelist.toolbar.border.color",
                "stateform.rulelist.toolbar.margin", "stateform.rulelist.toolbar.padding", "listmanagerpanel.toolbar.border.type",
                "listmanagerpanel.toolbar.border.width", "listmanagerpanel.toolbar.border.color", "listmanagerpanel.toolbar.margin",
                "listmanagerpanel.toolbar.padding");
    }

    public Color getStateFormRuleListScrollBackground() {
        return getColorFromProperty("stateform.rulelist.scroll.background", "listmanagerpanel.list.scroll.background");
    }

    public Color getStateFormRuleListScrollForeground() {
        return getColorFromProperty("stateform.rulelist.scroll.foreground", "listmanagerpanel.list.scroll.foreground");
    }


    public Color getStateFormGreenhouseRendererBackground() {
        return getColorFromProperty("stateform.greenhouserenderer.background");
    }

    public Color getStateFormGreenhouseRendererForeground() {
        return getColorFromProperty("stateform.greenhouserenderer.foreground");
    }

    public Border getStateFormGreenhouseRendererBorder() {
        return getBorderFromProperty(null, "stateform.greenhouserenderer.border.type", "stateform.greenhouserenderer.border.width", "stateform.greenhouserenderer.border.color", "stateform.greenhouserenderer.margin", "stateform.greenhouserenderer.padding");
    }


    public Color getStateFormActivityListBackground() {
        return getColorFromProperty("stateform.activitylist.background", "listmanagerpanel.list.background");
    }

    public Color getStateFormActivityListForeground() {
        return getColorFromProperty("stateform.activitylist.foreground", "listmanagerpanel.list.foreground");
    }

    public Border getStateFormActivityListBorder() {
        return getBorderFromProperty(null, "stateform.activitylist.border.type", "stateform.activitylist.border.width", "stateform.activitylist.border.color",
                "stateform.activitylist.margin", "stateform.activitylist.padding", "listmanagerpanel.list.border.type",
                "listmanagerpanel.list.border.width", "listmanagerpanel.list.border.color", "listmanagerpanel.list.margin",
                "listmanagerpanel.list.padding");
    }

    public Color getStateFormActivityListToolbarBackground() {
        return getColorFromProperty("stateform.activitylist.toolbar.background", "listmanagerpanel.toolbar.background");
    }

    public Color getStateFormActivityListToolbarForeground() {
        return getColorFromProperty("stateform.activitylist.toolbar.foreground", "listmanagerpanel.toolbar.foreground");
    }

    public Border getStateFormActivityListToolbarBorder() {
        return getBorderFromProperty(null, "stateform.activitylist.toolbar.border.type", "stateform.activitylist.toolbar.border.width", "stateform.activitylist.toolbar.border.color",
                "stateform.activitylist.toolbar.margin", "stateform.activitylist.toolbar.padding", "listmanagerpanel.toolbar.border.type",
                "listmanagerpanel.toolbar.border.width", "listmanagerpanel.toolbar.border.color", "listmanagerpanel.toolbar.margin",
                "listmanagerpanel.toolbar.padding");
    }

    public Color getStateFormActivityListScrollBackground() {
        return getColorFromProperty("stateform.activitylist.scroll.background", "listmanagerpanel.list.scroll.background");
    }

    public Color getStateFormActivityListScrollForeground() {
        return getColorFromProperty("stateform.activitylist.scroll.foreground", "listmanagerpanel.list.scroll.foreground");
    }


    public Color getStateFormUtilitybarBackground() {
        return getColorFromProperty("stateform.utilitybar.background");
    }

    public Color getStateFormUtilitybarForeground() {
        return getColorFromProperty("stateform.utilitybar.foreground");
    }

    public Border getStateFormUtilitybarBorder() {
        return getBorderFromProperty(null, "stateform.utilitybar.border.type", "stateform.utilitybar.border.width", "stateform.utilitybar.border.color", "stateform.utilitybar.margin", "stateform.utilitybar.padding");
    }
}
