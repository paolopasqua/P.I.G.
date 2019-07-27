package it.unibs.dii.pajc.pig.client.view.component;

import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.LabeledTextbox;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.RegexTextInputVerifier;

import javax.swing.*;
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
    private LabeledTextbox description;
    private LabeledTextbox address;
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


        descriptionText = new JTextField();
        description = new LabeledTextbox(descriptionText, localization.getString("description"), LabeledTextbox.LABEL_ORIENTATION.NORTH);
        this.add(description);

        addressText = new JFormattedTextField();
        addressText.setToolTipText("###.###.###.###");

        RegexTextInputVerifier rtiv = new RegexTextInputVerifier(RegexTextInputVerifier.IPV4_REGEX_VERIFIER);
        rtiv.setLockOnFailure(false);
        rtiv.setFailureAction(this::execFailureVerification);
        rtiv.setSuccessAction(this::execSuccessVerification);
        addressText.setInputVerifier(rtiv);


        address = new LabeledTextbox(addressText, localization.getString("address"), LabeledTextbox.LABEL_ORIENTATION.NORTH);
        this.add(address);

        action = new JButton();
        action.setVisible(false);
        action.addActionListener(this::callButtonListeners);
        this.add(action);
        listenerList = new EventListenerList();
    }

    private void updateTitleOnBorder(String title) {
        titleBorder.setTitle(title);
    }

    private void execSuccessVerification(JComponent input) {
        input.setForeground(Color.BLACK);
    }

    private void execFailureVerification(JComponent input) {
        Toolkit.getDefaultToolkit().beep();
        input.setForeground(Color.RED);
    }

    private void callButtonListeners(ActionEvent evt) {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);

        evt.setSource(this);

        for(ActionListener lst : listeners) {
            lst.actionPerformed(evt);
        }
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
