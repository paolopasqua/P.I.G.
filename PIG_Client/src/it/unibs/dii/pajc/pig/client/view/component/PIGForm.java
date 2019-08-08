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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PIGForm extends JFrame {

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/view/component/PIGForm");

    private static final int TOOLBAR_HEIGHT = UtilityConstant.ICON_DIMENSION_18;
    private static final String PRE_TITLE_FORM = "Project IoT Greenhouse - ";
    private static final String TOOLBAR_INFO = "GNU General Public License v3.0 - https://github.com/paolopasqua/P.I.G.";

    private EventListenerList helpListeners;
    private IconButton helpButton;

    private JPanel backgroundPanel;
    private JToolBar genericToolBar;
    private JLabel infoText;
    private JPanel bottomPanel;

    public PIGForm() throws HeadlessException {
        this(null);
    }

    public PIGForm(String title) throws HeadlessException {
        super();
        setTitle(title);
        initComponent();
    }

    private void initComponent() {
        helpListeners = new EventListenerList();

        /***** ICON SETUP ******/
        try {
            URL iconURl = getClass().getResource(UtilityConstant.RESOURCES_LOGO);
            BufferedImage image = ImageIO.read(iconURl);
            ArrayList<Image> icons = new ArrayList<>();

            icons.add(image.getScaledInstance(UtilityConstant.ICON_DIMENSION_16, UtilityConstant.ICON_DIMENSION_16, Image.SCALE_SMOOTH));
            icons.add(image.getScaledInstance(UtilityConstant.ICON_DIMENSION_32, UtilityConstant.ICON_DIMENSION_32, Image.SCALE_SMOOTH));
            icons.add(image.getScaledInstance(UtilityConstant.ICON_DIMENSION_48, UtilityConstant.ICON_DIMENSION_48, Image.SCALE_SMOOTH));
            icons.add(image.getScaledInstance(UtilityConstant.ICON_DIMENSION_64, UtilityConstant.ICON_DIMENSION_64, Image.SCALE_SMOOTH));
            icons.add(image.getScaledInstance(UtilityConstant.ICON_DIMENSION_128, UtilityConstant.ICON_DIMENSION_128, Image.SCALE_SMOOTH));
            icons.add(image.getScaledInstance(UtilityConstant.ICON_DIMENSION_256, UtilityConstant.ICON_DIMENSION_256, Image.SCALE_SMOOTH));
            icons.add(image);

            this.setIconImages(icons);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        /***** FRAME SETUP ******/
        super.setContentPane(backgroundPanel);
    }

    public void setGenericToolBarVisible(boolean visible) {
        genericToolBar.setVisible(visible);
    }

    public boolean isGenericToolBarVisible() {
        return genericToolBar.isVisible();
    }

    public void setHelpButtonVisible(boolean visible) {
        helpButton.setVisible(visible);
    }

    public boolean isHelpButtonVisible() {
        return helpButton.isVisible();
    }

    @Override
    public void setContentPane(Container contentPane) {
        BorderLayout layout = (BorderLayout) backgroundPanel.getLayout();
        Component content = layout.getLayoutComponent(BorderLayout.CENTER);
        if (content != null)
            backgroundPanel.remove(content);
        backgroundPanel.add(contentPane, BorderLayout.CENTER);
    }

    @Override
    public void setTitle(String title) {
        setTitle(title, false);
    }

    public void setTitle(String title, boolean noPreTitle) {
        if (!noPreTitle)
            title = PRE_TITLE_FORM + title;
        super.setTitle(title);
    }

    public void addHelpActionListener(ActionListener listener) {
        helpListeners.add(ActionListener.class, listener);
    }

    public void removeHelpActionListener(ActionListener listener) {
        helpListeners.remove(ActionListener.class, listener);
    }

    public void fireHelpActionListener(ActionEvent evt) {
        ActionListener[] listeners = helpListeners.getListeners(ActionListener.class);

        if (listeners != null && listeners.length != 0)
            for (ActionListener l : listeners)
                l.actionPerformed(evt);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BorderLayout(0, 0));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(0, 0));
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);
        genericToolBar = new JToolBar();
        genericToolBar.setFloatable(false);
        genericToolBar.setPreferredSize(new Dimension(0, 25));
        bottomPanel.add(genericToolBar, BorderLayout.CENTER);
        infoText = new JLabel();
        infoText.setText("Label");
        bottomPanel.add(infoText, BorderLayout.WEST);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return backgroundPanel;
    }

}
