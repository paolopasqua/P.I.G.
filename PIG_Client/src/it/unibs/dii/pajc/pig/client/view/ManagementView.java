package it.unibs.dii.pajc.pig.client.view;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;
import it.unibs.dii.pajc.pig.client.utility.LogicActionListener;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface ManagementView extends PIGView {

    void close();
    void setWaitForData(boolean waitForData);
    void setWaitForDataStatus(String status);

    void showAlert(String title, String message);
    boolean askUser(String title, String message);
    void setDeviceDataSource(ListModel<Device> datasource);
    void setSensorDataSource(ListModel<Sensor> datasource);
    void setActivityDataSource(ListModel<Activity> datasource);
    void setRuleDataSource(ListModel<Rule> datasource);
    void setServerInfo(String address, String info);

    void addHelpActionListener(ActionListener lst);
    void removeHelpActionListener(ActionListener lst);
    void addSettingActionListener(ActionListener lst);
    void removeSettingActionListener(ActionListener lst);
    void addDisconnectActionListener(ActionListener lst);
    void removeDisconnectActionListener(ActionListener lst);

    void addDeviceLogicActionListener(LogicActionListener<Device> lst);
    void removeDeviceLogicActionListener(LogicActionListener<Device> lst);
    void addSensorLogicActionListener(LogicActionListener<Sensor> lst);
    void removeSensorLogicActionListener(LogicActionListener<Sensor> lst);
    void addActivityLogicActionListener(LogicActionListener<Activity> lst);
    void removeActivityLogicActionListener(LogicActionListener<Activity> lst);
    void addRuleLogicActionListener(LogicActionListener<Rule> lst);
    void removeRuleLogicActionListener(LogicActionListener<Rule> lst);
}
