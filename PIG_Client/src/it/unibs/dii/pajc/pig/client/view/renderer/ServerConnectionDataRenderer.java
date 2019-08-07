package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.ServerConnectionData;
import it.unibs.dii.pajc.pig.client.utility.UtilityConstant;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ResourceBundle;

public class ServerConnectionDataRenderer extends JPanel implements ListCellRenderer<ServerConnectionData> {
    private JLabel address;
    private JLabel description;
    private JLabel data;

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/view/renderer/ServerConnectionDataRenderer");

    public ServerConnectionDataRenderer() {
        address = new JLabel();
        description = new JLabel();
        data = new JLabel();
        data.setHorizontalAlignment(JLabel.RIGHT);

        setBackground(Color.lightGray);
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK),
                        BorderFactory.createEmptyBorder(0, 5, 0, 5)
                )
        );
        setLayout(new GridLayout(0, 3));

        add(address);
        add(description);
        add(data);

        this.setToolTipText(localizationBundle.getString("tooltip")); //TODO: localize
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ServerConnectionData> jList, ServerConnectionData serverConnectionData, int index, boolean isSelected, boolean cellHasFocus) {

        address.setText(serverConnectionData.getAddress());
        description.setText(serverConnectionData.getDescription());
        data.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(serverConnectionData.getLastConnection()));

        URL iconURL;
        if (serverConnectionData.isFavorite()) {
            iconURL = getClass().getResource(UtilityConstant.RESOURCES_FAVORITE_SYMBOL);
        } else {
            iconURL = getClass().getResource(UtilityConstant.RESOURCES_UNMARKED_FAVORITE_SYMBOL);
        }
        try {
            address.setIcon(new ImageIcon(ImageIO.read(iconURL).getScaledInstance(address.getPreferredSize().height, address.getPreferredSize().height, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isSelected) {
            setBackground(jList.getSelectionBackground());
            setForeground(jList.getSelectionForeground());
        }
        else {
            setBackground(jList.getBackground());
            setForeground(jList.getForeground());
        }

        return this;
    }

}
