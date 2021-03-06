package it.unibs.dii.pajc.pig.client.controller;

import it.unibs.dii.pajc.pig.client.model.ChoiceModel;
import it.unibs.dii.pajc.pig.client.bean.ServerConnectionData;
import it.unibs.dii.pajc.pig.client.view.ChoiceView;
import it.unibs.dii.pajc.pig.client.view.HelpView;
import it.unibs.dii.pajc.pig.client.view.StateForm;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ChoiceManager implements PIGController, ManagementObserver<ManagementController> {

    private EventListenerList closingListener;
    private ChoiceModel<ServerConnectionData> model;
    private ChoiceView<ServerConnectionData> form;
    private HelpView help;
    private DefaultListModel<ServerConnectionData> datasource;
    private ArrayList<ManagementController> connections;

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/controller/ChoiceManager");

    public ChoiceManager() {
        closingListener = new EventListenerList();
        datasource = new DefaultListModel<>();
        connections = new ArrayList<>();

        this.addClosingActionListener(this::closeManagementConnections);
    }

    @Override
    public void closingAlert(ManagementController source) {
        connections.remove(source);
    }

    private void addManagentConnection(ManagementController mngctrl) {
        connections.add(mngctrl);
    }

    private void closeManagementConnections(ActionEvent evt) {
        while(!connections.isEmpty()) {
            ManagementController m = connections.get(0);
            m.close();
        }
    }

    private void validateModel(ChoiceModel<ServerConnectionData> model, String location) throws IllegalArgumentException {
        if (model == null)
            throw new IllegalArgumentException(location + ": Model can't be null.");
    }

    private void validateView(ChoiceView<ServerConnectionData> view, String location) throws IllegalArgumentException {
        if (view == null)
            throw new IllegalArgumentException(location + ": View can't be null.");
    }

    private int compareModelElements(ServerConnectionData e1, ServerConnectionData e2) {
        if (e1.isFavorite() && !e2.isFavorite())
            return -1;
        else if (!e1.isFavorite() && e2.isFavorite())
            return 1;
        else if (e1.getLastConnection().before(e2.getLastConnection()))
            return 1;
        else if (e1.getLastConnection().after(e2.getLastConnection()))
            return -1;
        else
            return 0;
    }

    private void initDatasource() {
        datasource.clear();
        model.getElements()
                .stream()
                .sorted(this::compareModelElements)
                .forEach(serverConnectionData -> datasource.addElement(serverConnectionData));
    }

    private void initModel() {
        ListDataListener lst = new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent listDataEvent) {
                //if something added to model it's a new connection and it can be added immediately after connection marked as favorite
                int i = 0;
                boolean isFavorite = true;
                ServerConnectionData elem = model.getElementAt(listDataEvent.getIndex0());

                for (; i < datasource.getSize() && isFavorite; i++)
                    isFavorite = datasource.get(i).isFavorite();

                if(i >= datasource.getSize())
                    datasource.addElement(elem);
                else
                    datasource.insertElementAt(elem, i);
            }

            @Override
            public void intervalRemoved(ListDataEvent listDataEvent) {} //manually managed to optimize the operation on datasource sorting

            @Override
            public void contentsChanged(ListDataEvent listDataEvent) {} //manually managed to optimize the operation on datasource sorting
        };

        model.addListDataListener(lst);
    }

    private void initView() {
        initDatasource();
        form.setDatasource(datasource);

        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null, new Date().getTime(), 0);
                fireClosingActionListeners(evt);
            }
        });
        form.addConnectActionListener(actionEvent -> {
            ChoiceView v = (ChoiceView) actionEvent.getSource();
            List<ServerConnectionData> selection = v.getSelection();

            if (selection.isEmpty())
                v.showAlert(localizationBundle.getString("advice.selection.emptylist.title"), localizationBundle.getString("advice.selection.emptylist.message"));
            else {
                if (selection.get(0).getLastConnection() == null) {
                    //New connection case
                    ServerConnectionData data = selection.get(0);

                    data.setLastConnection(new Date());

                    if (model.validateData(data)) {
                        //connect
                        performConnection(data);
                        model.addElement(data);
                    } else {
                        v.showAlert(localizationBundle.getString("advice.connect.datavalidation.unsuccess.title"), localizationBundle.getString("advice.connect.datavalidation.unsuccess.message"));
                    }
                } else {
                    //Connection from list case
                    selection
                        .forEach(serverConnectionData -> {
                            int index = model.getIndex(serverConnectionData);
                            serverConnectionData.setLastConnection(new Date());
                            performConnection(serverConnectionData);
                            model.updateElementAt(serverConnectionData, index);
                        });
                    initDatasource(); //updating datasource of view
                }
            }
        });
        form.addHelpActionListener(actionEvent -> {
            if (help != null) {
                help.focus(actionEvent.getSource());
                help.show();
            } else {
                form.showAlert(localizationBundle.getString("advice.help.opening.title"), localizationBundle.getString("advice.help.opening.message"));
            }
        });
        form.addMarkFavoriteActionListener(actionEvent -> {
            ChoiceView v = (ChoiceView) actionEvent.getSource();
            List<ServerConnectionData> selection = v.getSelection();

            if (selection.isEmpty())
                v.showAlert(localizationBundle.getString("advice.selection.emptylist.title"), localizationBundle.getString("advice.selection.emptylist.message"));
            else {
                selection
                        .forEach(serverConnectionData -> {
                            int index = model.getIndex(serverConnectionData);
                            serverConnectionData.setFavorite(!serverConnectionData.isFavorite());
                            model.updateElementAt(serverConnectionData, index);
                        });

                initDatasource(); //updating datasource of view
            }
        });
        form.addDeleteActionListener(actionEvent -> {
            ChoiceView v = (ChoiceView) actionEvent.getSource();
            List<ServerConnectionData> selection = v.getSelection();

            if (selection.isEmpty()) {
                //no selection --> delete all except favorite
                for (int i = 0; i < model.getSize(); ) {
                    if (!model.getElementAt(i).isFavorite()) {
                        model.removeElementAt(i);
                    } else {
                        i++;
                    }
                }
            } else {
                selection
                        .forEach(o -> {
                            if (!o.isFavorite())
                                model.removeElementAt(model.getIndex(o));
                        });
            }

            initDatasource(); //updating datasource of view
        });
    }

    private void performConnection(ServerConnectionData data) {
        ChoiceManager instance = this;

        Runnable r = () -> {
            StateForm stateForm = new StateForm();
            ConnectionManager connectionManager = null;
            try {
                connectionManager = new ConnectionManager(data);
                ManagementManager managementController = new ManagementManager();

                managementController.setView(stateForm);
                managementController.setHelpView(help);
                managementController.setConnectionController(connectionManager);

                managementController.attachObserver(instance);
                addManagentConnection(managementController);

                managementController.start();
            } catch (IOException e) {
                form.showAlert(localizationBundle.getString("advice.connection.title"), e.getMessage());
                e.printStackTrace();
            }
        };

        new Thread(r).start();
    }

    public void fireClosingActionListeners(ActionEvent evt) {
        if(closingListener.getListenerCount(ActionListener.class) != 0) {
            ActionListener[] lst = closingListener.getListeners(ActionListener.class);

            for (ActionListener l : lst) {
                l.actionPerformed(evt);
            }
        }
    }

    public void addClosingActionListener(ActionListener lst) {
        closingListener.add(ActionListener.class, lst);
    }

    public void removeClosingActionListener(ActionListener lst) {
        closingListener.remove(ActionListener.class, lst);
    }

    public void setHelpView(HelpView view) {
        help = view;
    }

    public void removeHelpView() {
        help = null;
    }

    public void setView(ChoiceView<ServerConnectionData> view) throws IllegalArgumentException {
        validateView(view, "ChoiceManager.attachView"); //throws on failure

        form = view;
    }

    public void removeView() {
        form = null;
    }

    public void setModel(ChoiceModel<ServerConnectionData> model) throws IllegalArgumentException {
        validateModel(model, "ChoiceManager.attachModel"); //Throws on failure

        this.model = model;
    }

    public void removeModel() {
        this.model = null;
    }

    @Override
    public void start() {
        validateView(form, "ChoiceManager.start"); //throws on failure
        validateModel(model, "ChoiceManager.start"); //Throws on failure

        initModel();
        initView();
        form.show();
    }
}
