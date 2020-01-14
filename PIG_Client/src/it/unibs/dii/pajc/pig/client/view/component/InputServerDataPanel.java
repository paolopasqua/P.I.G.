package it.unibs.dii.pajc.pig.client.view.component;

import it.unibs.dii.pajc.pig.client.model.ThemeDataSource;
import it.unibs.dii.pajc.pig.client.utility.UtilityConstant;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.LabeledComponent;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.RegexTextInputVerifier;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Component for input data of a server connection (ip address and description).
 * It has a button than can be set visible adding a label with setLabel and you can personalize the action adding an ActionListener.
 */
public class InputServerDataPanel extends JPanel {
    private String title;
    private TitledBorder titleBorder;
    private EventListenerList listenerList;

    private JTextField descriptionText;
    private JFormattedTextField addressText;
    private LabeledComponent description;
    private LabeledComponent address;
    private JButton action;

    private static ResourceBundle localization = ResourceBundle.getBundle("localization/view/component/InputServerDataPanel");

    public InputServerDataPanel() {
        this(null);
    }

    public InputServerDataPanel(String title) {
        this.title = title;

        initComponent();
    }

    private void initComponent() {
        this.setLayout(new GridLayout(0, 3, 10, 10));

        /*
        titleBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                title
        );
        this.setBorder(
            BorderFactory.createCompoundBorder(
                titleBorder,
                BorderFactory.createEmptyBorder(2,4,2,4)
            )
        );
        */

        descriptionText = new JTextField();
        description = new LabeledComponent(descriptionText, localization.getString("description"), LabeledComponent.LABEL_ORIENTATION.NORTH);
        this.add(description);

        addressText = new JFormattedTextField();
        addressText.setToolTipText("###.###.###.###");

        RegexTextInputVerifier rtiv = new RegexTextInputVerifier(UtilityConstant.IPV4_REGEX_VERIFIER);
        rtiv.setLockOnFailure(false);
        rtiv.setFailureAction(this::execFailureVerification);
        rtiv.setSuccessAction(this::execSuccessVerification);
        addressText.setInputVerifier(rtiv);


        address = new LabeledComponent(addressText, localization.getString("address"), LabeledComponent.LABEL_ORIENTATION.NORTH);
        this.add(address);

        action = new JButton();
        action.setVisible(false);
        action.addActionListener(this::callButtonListeners);
        this.add(action);
        listenerList = new EventListenerList();


        /***** THEME SETUP *****/
        ThemeDataSource theme = ThemeDataSource.getInstance();

        Color c = theme.getInputServerDataPanelBackground();
        setPanelBackground(c);

        c = theme.getInputServerDataPanelForeground();
        setPanelForeground(c);

        Border b = theme.getInputServerDataPanelBorder(title);
        this.setBorder(b);

        c = theme.getInputServerDataPanelButtonBackground();
        setActionBackground(c);

        c = theme.getInputServerDataPanelButtonForeground();
        setActionForeground(c);

        b = theme.getInputServerDataPanelButtonBorder();
        setActionBorder(b);
    }

    private void setBackgroundRecursivly(Component comp, Color c) {
        if (comp instanceof Container) {
            Container container = (Container)comp;
            container.setBackground(c);
            for (Component component : container.getComponents())
                setBackgroundRecursivly(component, c);
        }
        else {
            comp.setBackground(c);
        }
    }

    private void setForegroundRecursivly(Component comp, Color c) {
        if (comp instanceof Container) {
            Container container = (Container)comp;
            container.setForeground(c);
            for (Component component : container.getComponents())
                setForegroundRecursivly(component, c);
        }
        else {
            comp.setForeground(c);
        }
    }

    private void updateTitleOnBorder(String title) {
        //titleBorder.setTitle(title);
        this.setBorder(ThemeDataSource.getInstance().getInputServerDataPanelBorder(title));
    }

    private void execSuccessVerification(JComponent input) {
        input.setForeground(Color.BLACK);
    }

    private void execFailureVerification(JComponent input) {
        Toolkit.getDefaultToolkit().beep(); //TODO: make it beep
        input.setForeground(Color.RED);
    }

    private void callButtonListeners(ActionEvent evt) {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);

        evt.setSource(this);

        for(ActionListener lst : listeners) {
            lst.actionPerformed(evt);
        }
    }

    public void setPanelBackground(Color bg) {
        if (bg != null)
            setBackgroundRecursivly(this, bg);
    }
    public void setPanelForeground(Color bg) {
        if (bg != null)
            setForegroundRecursivly(this, bg);
    }
    public void setActionBackground(Color bg) {
        if (bg != null)
            action.setBackground(bg);
    }
    public void setActionForeground(Color bg) {
        if (bg != null)
            action.setForeground(bg);
    }
    public void setActionBorder(Border b) {
        action.setBorder(b);
    }

    public Color getPanelBackground() {
        return this.getBackground();
    }
    public Color getPanelForeground() {
        return this.getForeground();
    }
    public Color getActionBackground() {
        return action.getBackground();
    }
    public Color getActionForeground() {
        return action.getForeground();
    }
    public Border getActionBorder() {
        return action.getBorder();
    }

    public String getDescription() {
        return descriptionText.getText();
    }

    public String getAddress() {
        return addressText.getText();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateTitleOnBorder(title);
    }

    public void setButton(String label) {
        setButton(label, null);
    }

    public void setButton(String label, ActionListener listener) {
        if (label != null) {
            action.setText(label);
            action.setVisible(true);

            if (listener != null)
                addButtonActionListener(listener);
        }
        else {
            action.setVisible(false);
        }
    }

    public void addButtonActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeButtonActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

}
