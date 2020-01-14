package it.unibs.dii.pajc.pig.client.view;


import it.unibs.dii.pajc.pig.client.bean.ServerConnectionData;
import it.unibs.dii.pajc.pig.client.model.ThemeDataSource;
import it.unibs.dii.pajc.pig.client.utility.UtilityConstant;
import it.unibs.dii.pajc.pig.client.view.component.InputServerDataPanel;
import it.unibs.dii.pajc.pig.client.view.component.PIGForm;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.IconButton;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.ListManagerPanel;
import it.unibs.dii.pajc.pig.client.view.renderer.ServerConnectionDataRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ConnectionForm implements ChoiceView<ServerConnectionData> {

    private PIGForm frame;
    private EventListenerList connectListeners, markFavoriteListeners, deleteListeners;
    private ArrayList<ServerConnectionData> selectionQueue;

    private JPanel backgroundPanel;
    private InputServerDataPanel serverSearchPanel;
    private InputServerDataPanel serverInsertPanel;
    private ListManagerPanel<ServerConnectionData> serverListPanel;

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/view/ConnectionForm");

    public final PIGForm getFrame() {
        return frame;
    }

    @Override
    public void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(getFrame(), message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public boolean askUser(String title, String message) {
        return JOptionPane.showConfirmDialog(getFrame(), message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @Override
    public void show() {
        getFrame().setVisible(true);
    }

    //public void toFront() { getFrame().toFront(); }

    //public void hide() { getFrame().setVisible(false); }

    //public void close() { getFrame().dispatchEvent(new WindowEvent(getFrame(), WindowEvent.WINDOW_CLOSING)); }

    public ConnectionForm() {
        initComponent();
    }

    private void initComponent() {
        connectListeners = new EventListenerList();
        markFavoriteListeners = new EventListenerList();
        deleteListeners = new EventListenerList();
        selectionQueue = new ArrayList<>();

        /***** FRAME SETUP ******/
        frame = new PIGForm(localizationBundle.getString("form.title"));
        frame.setContentPane(this.backgroundPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        /***** TOP SERVER DATA PANEL SETUP ******/
        serverInsertPanel.setTitle(localizationBundle.getString("insert.server.title"));
        serverInsertPanel.setButton(localizationBundle.getString("insert.server.button"));
        serverInsertPanel.addButtonActionListener(this::callConnectActionListener);

        /***** CENTER LIST MANAGER PANEL SETUP ******/
        serverListPanel.setTitle(localizationBundle.getString("list.server.title"));
        serverListPanel.setRenderer(new ServerConnectionDataRenderer());
        serverListPanel.addDoubleClickActionListener(this::callConnectActionListener);

        IconButton connectButton = new IconButton(UtilityConstant.RESOURCES_CONNECTION_SYMBOL, "C", ListManagerPanel.TOOLBAR_ICON_HEIGHT);
        connectButton.setToolTipText(localizationBundle.getString("list.server.button.connect.tooltip"));
        connectButton.addActionListener(this::callConnectActionListener);
        serverListPanel.addToToolbar(connectButton);

        IconButton markFavoriteButton = new IconButton(UtilityConstant.RESOURCES_FAVORITE_SYMBOL, "F", ListManagerPanel.TOOLBAR_ICON_HEIGHT);
        markFavoriteButton.setToolTipText(localizationBundle.getString("list.server.button.favorite.tooltip"));
        markFavoriteButton.addActionListener(this::callMarkFavoriteActionListener);
        serverListPanel.addToToolbar(markFavoriteButton);

        IconButton deleteButton = new IconButton(UtilityConstant.RESOURCES_TRASH_SYMBOL, "D", ListManagerPanel.TOOLBAR_ICON_HEIGHT);
        deleteButton.setToolTipText(localizationBundle.getString("list.server.button.delete.tooltip"));
        deleteButton.addActionListener(this::callDeleteActionListener);
        serverListPanel.addToToolbar(deleteButton);

        /***** BOTTOM SERVER DATA PANEL SETUP ******/
        serverSearchPanel.setTitle(localizationBundle.getString("search.server.title"));
        serverSearchPanel.setButton(localizationBundle.getString("search.server.button"));
        serverSearchPanel.addButtonActionListener(this::searchListener);


        /***** THEME SETUP *****/
        ThemeDataSource theme = ThemeDataSource.getInstance();

        Color c = theme.getConnectionFormBackground();
        if (c != null) {
            backgroundPanel.setBackground(c);
            serverListPanel.setPanelBackground(c);
        }

        c = theme.getConnectionFormForeground();
        if (c != null) {
            backgroundPanel.setForeground(c);
            serverListPanel.setPanelForeground(c);
        }

        Border b = theme.getConnectionFormBorder();
        backgroundPanel.setBorder(b);

        c = theme.getConnectionFormNewServerBackground();
        serverInsertPanel.setPanelBackground(c);

        c = theme.getConnectionFormNewServerForeground();
        serverInsertPanel.setPanelForeground(c);

        b = theme.getConnectionFormNewServerBorder();
        serverInsertPanel.setBorder(b);

        c = theme.getConnectionFormNewServerButtonBackground();
        serverInsertPanel.setActionBackground(c);

        c = theme.getConnectionFormNewServerButtonForeground();
        serverInsertPanel.setActionForeground(c);

        b = theme.getConnectionFormNewServerButtonBorder();
        serverInsertPanel.setActionBorder(b);

        c = theme.getConnectionFormListBackground();
        serverListPanel.setListBackground(c);

        c = theme.getConnectionFormListForeground();
        serverListPanel.setListForeground(c);

        b = theme.getConnectionFormListBorder();
        serverListPanel.setListBorder(b);

        c = theme.getConnectionFormListToolbarBackground();
        serverListPanel.setToolbarBackground(c);

        c = theme.getConnectionFormListToolbarForeground();
        serverListPanel.setToolbarForeground(c);

        b = theme.getConnectionFormListToolbarBorder();
        serverListPanel.setToolbarBorder(b);

        c = theme.getConnectionFormListScrollBackground();
        serverListPanel.setScrollBackground(c);

        c = theme.getConnectionFormListScrollForeground();
        serverListPanel.setScrollForeground(c);

        c = theme.getConnectionFormSearchServerBackground();
        serverSearchPanel.setPanelBackground(c);

        c = theme.getConnectionFormSearchServerForeground();
        serverSearchPanel.setPanelForeground(c);

        b = theme.getConnectionFormSearchServerBorder();
        serverSearchPanel.setBorder(b);

        c = theme.getConnectionFormSearchServerButtonBackground();
        serverSearchPanel.setActionBackground(c);

        c = theme.getConnectionFormSearchServerButtonForeground();
        serverSearchPanel.setActionForeground(c);

        b = theme.getConnectionFormSearchServerButtonBorder();
        serverSearchPanel.setActionBorder(b);
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
        backgroundPanel.setPreferredSize(new Dimension(600, 800));
        serverInsertPanel = new InputServerDataPanel();
        backgroundPanel.add(serverInsertPanel, BorderLayout.NORTH);
        serverListPanel = new ListManagerPanel();
        backgroundPanel.add(serverListPanel, BorderLayout.CENTER);
        serverSearchPanel = new InputServerDataPanel();
        backgroundPanel.add(serverSearchPanel, BorderLayout.SOUTH);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return backgroundPanel;
    }

    private void createUIComponents() {
        // place custom component creation code here
    }

    private boolean populateSelectionQueueByList() {
        selectionQueue.clear();
        serverListPanel.getSelectedItemsList().forEach(this::addServerToQueue);
        return !selectionQueue.isEmpty();
    }

    private void callActionListeners(ActionEvent evt, EventListenerList listeners) {
        ActionListener[] lst = listeners.getListeners(ActionListener.class);

        evt.setSource(this);

        if (lst != null && lst.length != 0)
            for (ActionListener l : lst) {
                l.actionPerformed(evt);
            }
    }

    private void callConnectActionListener(ActionEvent evt) {
        if (evt.getSource() instanceof InputServerDataPanel) {
            InputServerDataPanel isdp = (InputServerDataPanel) evt.getSource();
            addServerToQueue(isdp.getAddress(), isdp.getDescription());
        } else {
            populateSelectionQueueByList();
        }

        callActionListeners(evt, connectListeners);
    }

    private void callMarkFavoriteActionListener(ActionEvent evt) {
        populateSelectionQueueByList();
        callActionListeners(evt, markFavoriteListeners);
    }

    private void callDeleteActionListener(ActionEvent evt) {
        populateSelectionQueueByList();
        callActionListeners(evt, deleteListeners);
    }

    private void searchListener(ActionEvent evt) {
        InputServerDataPanel p = (InputServerDataPanel) evt.getSource();
        boolean okAddress = false, okDescription = false;

        okAddress = p.getAddress() != null && !p.getAddress().trim().isEmpty();
        okDescription = p.getDescription() != null && !p.getDescription().trim().isEmpty();

        if (okAddress || okDescription) {
            ServerConnectionData item = new ServerConnectionData(p.getAddress(), p.getDescription());
            List<Integer> indices;

            indices = serverListPanel.searchItem(item, (o, t1) -> {
                ServerConnectionData elem, search;
                int res = 0;

                if (o.getLastConnection() == null) {
                    search = o;
                    elem = t1;
                } else {
                    search = t1;
                    elem = o;
                }

                //Check address
                if (search.getAddress() != null && !search.getAddress().trim().isEmpty()) {
                    res = elem.getAddress().matches(search.getAddress()) ? 1 : -1;
                }

                //Check description
                if (search.getDescription() != null && !search.getDescription().trim().isEmpty() && res >= 0) {
                    res = elem.getDescription().matches(".*" + search.getDescription() + ".*") ? 1 : -1;
                }

                return res <= 0 ? 0 : 1;
            });

            if (!indices.isEmpty()) {
                serverListPanel.clearSelection();
                indices.forEach(integer -> serverListPanel.addSelected(integer));
            } else {
                showAlert(localizationBundle.getString("dialog.search.unsuccessful.title"), localizationBundle.getString("dialog.search.unsuccessful.message"));
            }
        }
    }

    @Override
    public void addWindowListener(WindowListener lst) {
        getFrame().addWindowListener(lst);
    }

    @Override
    public void removeWindowListener(WindowListener lst) {
        getFrame().removeWindowListener(lst);
    }

    @Override
    public void addHelpActionListener(ActionListener listener) {
        getFrame().addHelpActionListener(listener);
    }

    @Override
    public void removeHelpActionListener(ActionListener listener) {
        getFrame().removeHelpActionListener(listener);
    }

    @Override
    public void addConnectActionListener(ActionListener listener) {
        connectListeners.add(ActionListener.class, listener);
    }

    @Override
    public void removeConnectActionListener(ActionListener listener) {
        connectListeners.remove(ActionListener.class, listener);
    }

    @Override
    public void addMarkFavoriteActionListener(ActionListener listener) {
        markFavoriteListeners.add(ActionListener.class, listener);
    }

    @Override
    public void removeMarkFavoriteActionListener(ActionListener listener) {
        markFavoriteListeners.remove(ActionListener.class, listener);
    }

    @Override
    public void addDeleteActionListener(ActionListener listener) {
        deleteListeners.add(ActionListener.class, listener);
    }

    @Override
    public void removeDeleteActionListener(ActionListener listener) {
        deleteListeners.remove(ActionListener.class, listener);
    }

    @Override
    public List<ServerConnectionData> getSelection() {
        return popSelectionQueue();
    }

    public ArrayList<ServerConnectionData> getSelectionQueue() {
        return selectionQueue;
    }

    public ArrayList<ServerConnectionData> popSelectionQueue() {
        ArrayList<ServerConnectionData> ret = (ArrayList<ServerConnectionData>) selectionQueue.clone();
        selectionQueue.clear();
        return ret;
    }

    public void addServerToQueue(ServerConnectionData selectedServer) {
        this.selectionQueue.add(selectedServer);
    }

    public void addServerToQueue(String address, String description, Date lastConnection) {
        this.selectionQueue.add(new ServerConnectionData(address, description, lastConnection));
    }

    public void addServerToQueue(String address, String description) {
        addServerToQueue(address, description, null);
    }

    public void addServerToQueue(String address) {
        addServerToQueue(address, null);
    }

    @Override
    public void setDatasource(ListModel<ServerConnectionData> datasource) {
        serverListPanel.setModel(datasource);
    }
}
