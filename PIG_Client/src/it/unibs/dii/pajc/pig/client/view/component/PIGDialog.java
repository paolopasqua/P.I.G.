package it.unibs.dii.pajc.pig.client.view.component;

import it.unibs.dii.pajc.pig.client.model.ThemeDataSource;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class PIGDialog extends JDialog {
    private EventListenerList onClosingListeners;

    private boolean disposeOnClosing;
    private JPanel contentPanel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private PIGBackgroundPanel backgroundPanel;
    private JPanel buttonsPanel;

    public PIGDialog() {
        this(null);
    }

    public PIGDialog(Frame owner) {
        this(owner, "");
    }

    public PIGDialog(Frame owner, boolean modal) {
        this(owner, "", modal);
    }

    public PIGDialog(Frame owner, String title) {
        this(owner, title, true);
    }

    public PIGDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);

        disposeOnClosing = true;

        super.setContentPane(backgroundPanel);
        getRootPane().setDefaultButton(buttonOK);
        onClosingListeners = new EventListenerList();

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel(e);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel(new ActionEvent(e.getSource(), e.getID(), null, new Date().getTime(), 0));
            }
        });

        // call onCancel() on ESCAPE
        contentPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel(e);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        /***** THEME SETUP *****/
        ThemeDataSource theme = ThemeDataSource.getInstance();

        Color c = theme.getDialogBackground();
        if (c != null)
            contentPanel.setBackground(c);

        c = theme.getDialogForeground();
        if (c != null)
            contentPanel.setForeground(c);

        Border b = theme.getDialogBorder();
        contentPanel.setBorder(b);

        c = theme.getDialogButtonPanelBackground();
        if (c != null)
            buttonsPanel.setBackground(c);

        c = theme.getDialogButtonPanelForeground();
        if (c != null)
            buttonsPanel.setForeground(c);

        b = theme.getDialogButtonPanelBorder();
        buttonsPanel.setBorder(b);

        c = theme.getDialogButtonBackground();
        if (c != null) {
            buttonOK.setBackground(c);
            buttonCancel.setBackground(c);
        }

        c = theme.getDialogButtonForeground();
        if (c != null) {
            buttonOK.setForeground(c);
            buttonCancel.setForeground(c);
        }

        b = theme.getDialogButtonBorder();
        buttonOK.setBorder(b);
        buttonCancel.setBorder(b);
    }

    private void onCancel(ActionEvent evt) {
        ActionListener[] listeners = onClosingListeners.getListeners(ActionListener.class);

        if (listeners != null && listeners.length != 0)
            for (ActionListener l : listeners)
                l.actionPerformed(evt);

        if (disposeOnClosing)
            dispose();
    }

    @Override
    public void setContentPane(Container contentPane) {
        BorderLayout layout = (BorderLayout) contentPanel.getLayout();
        Component content = layout.getLayoutComponent(BorderLayout.CENTER);
        if (content != null)
            contentPanel.remove(content);
        contentPanel.add(contentPane, BorderLayout.CENTER);
    }

    public boolean isDisposeOnClosing() {
        return disposeOnClosing;
    }

    public void setDisposeOnClosing(boolean disposeOnClosing) {
        this.disposeOnClosing = disposeOnClosing;
    }

    public void setGenericToolBarVisible(boolean visible) {
        backgroundPanel.setGenericToolBarVisible(visible);
    }

    public boolean isGenericToolBarVisible() {
        return backgroundPanel.isGenericToolBarVisible();
    }

    public void setHelpButtonVisible(boolean visible) {
        backgroundPanel.setHelpButtonVisible(visible);
    }

    public boolean isHelpButtonVisible() {
        return backgroundPanel.isHelpButtonVisible();
    }

    public void addHelpActionListener(ActionListener listener) {
        backgroundPanel.addHelpActionListener(listener);
    }

    public void removeHelpActionListener(ActionListener listener) {
        backgroundPanel.removeHelpActionListener(listener);
    }

    public void fireHelpActionListener(ActionEvent evt) {
        backgroundPanel.fireHelpActionListener(evt);
    }

    public void setButtonOKText(String text) {
        buttonOK.setText(text);
    }

    public void setButtonOKTooltip(String tooltip) {
        buttonOK.setToolTipText(tooltip);
    }

    public void setButtonOKVisible(boolean visible) {
        buttonOK.setVisible(visible);
    }

    public boolean isButtonOKVisible() {
        return buttonOK.isVisible();
    }

    public void addButtonOKActionListener(ActionListener lst) {
        buttonOK.addActionListener(lst);
    }

    public void removeButtonOKActionListener(ActionListener lst) {
        buttonOK.removeActionListener(lst);
    }

    public void setButtonCancelText(String text) {
        buttonCancel.setText(text);
    }

    public void setButtonCancelTooltip(String tooltip) {
        buttonCancel.setToolTipText(tooltip);
    }

    public void setButtonCancelVisible(boolean visible) {
        buttonCancel.setVisible(visible);
    }

    public boolean isButtonCancelVisible() {
        return buttonCancel.isVisible();
    }

    public void addOnClosingActionListener(ActionListener lst) {
        onClosingListeners.add(ActionListener.class, lst);
    }

    public void removeOnClosingActionListener(ActionListener lst) {
        onClosingListeners.remove(ActionListener.class, lst);
    }

    public void setButtonsPanelVisible(boolean visible) {
        buttonsPanel.setVisible(visible);
    }

    public boolean isButtonsPanelVisible() {
        return buttonsPanel.isVisible();
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        backgroundPanel = new PIGBackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout(0, 0));
        backgroundPanel.setMinimumSize(new Dimension(300, 200));
        backgroundPanel.setPreferredSize(new Dimension(400, 300));
        panel1.add(backgroundPanel, BorderLayout.CENTER);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);
        buttonOK = new JButton();
        buttonOK.setText("OK");
        buttonsPanel.add(buttonOK);
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        buttonsPanel.add(buttonCancel);
    }
}
