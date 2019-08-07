package it.unibs.dii.pajc.pig.client.view.component.generalpurpouse;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ListManagerPanel<E> extends JPanel {
    public static final int TOOLBAR_ICON_HEIGHT = 25;

    private EventListenerList doubleClickListeners;

    private JPanel topPanel;
    private JLabel titleLabel;
    private JToolBar utilityBar;
    private JScrollPane scrollPane;
    private JList<E> list;

    public ListManagerPanel() {
        this(null);
    }

    public ListManagerPanel(String title) {
        initComponent();

        setTitle(title);
    }

    private void initComponent() {
        doubleClickListeners = new EventListenerList();

        this.setLayout(new BorderLayout());
        this.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1,2,0,2),
                BorderFactory.createLineBorder(Color.BLACK)
            )
        );

        /********************** TOP PANEL ***********************/
        topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(0,2,0,2),
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)
                )
        );

        titleLabel = new JLabel("");
        topPanel.add(titleLabel, BorderLayout.WEST);

        utilityBar = new JToolBar();
        utilityBar.setFloatable(false);
        utilityBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        utilityBar.setBorder(BorderFactory.createEmptyBorder());
        utilityBar.setPreferredSize(new Dimension(0, TOOLBAR_ICON_HEIGHT));
        utilityBar.setBorderPainted(false);
        utilityBar.setOpaque(false);
        topPanel.add(utilityBar, BorderLayout.CENTER);

        this.add(topPanel, BorderLayout.NORTH);

        /********************** CENTER PANEL ***********************/
        list = new JList<E>();
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !list.isSelectionEmpty()) {
                    ActionEvent actionEvent = new ActionEvent(this, e.getID(), null, e.getWhen(), 0);
                    for (ActionListener l : doubleClickListeners.getListeners(ActionListener.class)) {
                        l.actionPerformed(actionEvent);
                    }
                }
            }
        });

        scrollPane = new JScrollPane(list);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    public String getTitle() {
        return titleLabel.getText();
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public Icon getIcon() {
        return titleLabel.getIcon();
    }

    public void setIcon(Icon icon) {
        titleLabel.setIcon(icon);
    }

    public void addToToolbar(Component comp) {
        utilityBar.add(comp);
    }

    public void removeFromToolbar(Component comp) {
        utilityBar.remove(comp);
    }

    public void removeFromToolbar(int index) {
        utilityBar.remove(index);
    }

    public void setModel(ListModel<E> model) {
        list.setModel(model);
    }

    public ListModel<E> getModel() {
        return list.getModel();
    }

    public java.util.List<Integer> searchItem(E item, Comparator<E> comparator) {
        ArrayList<Integer> indices = new ArrayList<>();
        ListModel<E> model = getModel();

        for (int i = 0; i < model.getSize(); i++) {
            if (comparator.compare(model.getElementAt(i), item) != 0) {
                indices.add(i);
            }
        }

        return indices;
    }

    public void setRenderer(ListCellRenderer<E> renderer) {
        list.setCellRenderer(renderer);
    }

    public void clearSelection() {
        list.clearSelection();
    }

    public boolean setSelected(E item) {
        list.setSelectedValue(item, true);
        return (list.getSelectedIndex() != -1);
    }

    public boolean setSelected(int index) {
        list.setSelectedIndex(index);
        return Arrays.asList(list.getSelectedIndex()).contains(index);
    }

    public boolean setSelected(int[] indices) {
        list.setSelectedIndices(indices);
        return list.getSelectedIndices().equals(indices);
    }

    public boolean setSelected(int anchor, int lead) {
        list.setSelectionInterval(anchor, lead);
        int[] selected = list.getSelectedIndices();
        return selected[0] == anchor && selected[selected.length-1] == lead;
    }

    public boolean addSelected(int index) {
        list.addSelectionInterval(index, index);
        return Arrays.asList(list.getSelectedIndex()).contains(index);
    }

    public E getSelected() {
        return (E)list.getSelectedValue();
    }

    public java.util.List<E> getSelectedItemsList() {
        return list.getSelectedValuesList();
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public int[] getSelectedIndices() {
        return list.getSelectedIndices();
    }

    public void addDoubleClickActionListener(ActionListener lst) {
        doubleClickListeners.add(ActionListener.class, lst);
    }

    public void removeDoubliClickActionListener(ActionListener lst) {
        doubleClickListeners.remove(ActionListener.class, lst);
    }

}
