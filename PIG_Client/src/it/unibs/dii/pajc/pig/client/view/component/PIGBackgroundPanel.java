package it.unibs.dii.pajc.pig.client.view.component;

import it.unibs.dii.pajc.pig.client.utility.UtilityConstant;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.IconButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PIGBackgroundPanel extends JPanel {

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/view/component/PIGBackgroundPanel");

    private static final int TOOLBAR_HEIGHT = UtilityConstant.ICON_DIMENSION_18;
    private static final String TOOLBAR_INFO = "GNU General Public License v3.0 - https://github.com/paolopasqua/P.I.G.";

    private EventListenerList helpListeners;
    private IconButton helpButton;

    private JToolBar genericToolBar;
    private JLabel infoText;
    private JPanel bottomPanel;

    public PIGBackgroundPanel() {
        super();

        initComponent();
    }

    private void initComponent() {
        helpListeners = new EventListenerList();

        /***** PANEL SETUP ******/
        super.setLayout(new BorderLayout(0, 0));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(0, 0));
        this.add(bottomPanel, BorderLayout.SOUTH);
        genericToolBar = new JToolBar();
        genericToolBar.setFloatable(false);
        genericToolBar.setPreferredSize(new Dimension(0, 25));
        bottomPanel.add(genericToolBar, BorderLayout.CENTER);
        infoText = new JLabel();
        infoText.setText("Label");
        bottomPanel.add(infoText, BorderLayout.WEST);

        /***** TEXT SETUP ******/
        infoText.setOpaque(false);
        infoText.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        infoText.setFont(new Font("Arial", Font.PLAIN, 10));
        infoText.setText(TOOLBAR_INFO);
        try {
            URL iconURl = getClass().getResource(UtilityConstant.RESOURCES_COPYLEFT_SYMBOL);
            BufferedImage image = ImageIO.read(iconURl);

            infoText.setIcon(new ImageIcon(image.getScaledInstance(UtilityConstant.ICON_DIMENSION_12, UtilityConstant.ICON_DIMENSION_12, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /***** TOOLBAR SETUP ******/
        FlowLayout utBarLayout = new FlowLayout(FlowLayout.RIGHT);
        utBarLayout.setVgap(0);
        genericToolBar.setOpaque(false);
        genericToolBar.setLayout(utBarLayout);
        genericToolBar.setBorder(BorderFactory.createEmptyBorder());
        genericToolBar.setPreferredSize(new Dimension(0, TOOLBAR_HEIGHT));

        helpButton = new IconButton(UtilityConstant.RESOURCES_HELP_SYMBOL, localizationBundle.getString("toolbar.help.alternative"), TOOLBAR_HEIGHT);
        helpButton.setToolTipText(localizationBundle.getString("toolbar.help.tooltip"));
        helpButton.addActionListener(this::fireHelpActionListener);
        genericToolBar.add(helpButton, BorderLayout.CENTER);
    }

    public void setGenericToolBarVisible(boolean visible) {
        bottomPanel.setVisible(visible);
    }

    public boolean isGenericToolBarVisible() {
        return bottomPanel.isVisible();
    }

    public void setButtonsVisible(boolean visible) {
        genericToolBar.setVisible(visible);
    }

    public boolean isButtonsVisible() {
        return genericToolBar.isVisible();
    }

    public void setHelpButtonVisible(boolean visible) {
        helpButton.setVisible(visible);
    }

    public boolean isHelpButtonVisible() {
        return helpButton.isVisible();
    }

    public void addHelpActionListener(ActionListener listener) {
        helpListeners.add(ActionListener.class, listener);
    }

    public void removeHelpActionListener(ActionListener listener) {
        helpListeners.remove(ActionListener.class, listener);
    }

    public void fireHelpActionListener(ActionEvent evt) {
        ActionListener[] listeners = helpListeners.getListeners(ActionListener.class);

        evt.setSource(getParent() == null ? this : getParent());

        if (listeners != null && listeners.length != 0)
            for (ActionListener l : listeners)
                l.actionPerformed(evt);
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        //DO NOTHING
    }
}
