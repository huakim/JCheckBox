import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

class JCheckList<T> extends JList<T> {

    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public void setSelected(int index) {
        if (index != -1) {
            JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
            checkbox.setSelected(
                    !checkbox.isSelected());
            repaint();
        }
    }

    protected static class CellListener
            extends DefaultListModel
            implements ListDataListener {

        ListModel ls;

        public CellListener(ListModel ls) {
            ls.addListDataListener(this);
            int i = ls.getSize();
            for (int v = 0; v < i; v++) {
                var r = new JCheckBox();
                r.setText(ls.getElementAt(v).toString());
                this.addElement(r);
            }
            this.ls = ls;
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            int begin = e.getIndex0();
            int end = e.getIndex1();
            for (; begin <= end; begin++) {
                var r = new JCheckBox();
                r.setText(ls.getElementAt(begin).toString());
                this.add(begin, r);
            }
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            int begin = e.getIndex0();
            int end = e.getIndex1();
            for (; begin <= end; end--) {
                this.remove(begin);
            }
        }

        @Override
        public void contentsChanged(ListDataEvent e) {

        }
    }

    public JCheckList() {
        setCellRenderer(new CellRenderer());

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int index = locationToIndex(e.getPoint());

                setSelected(index);
            }
        }
        );
        
        addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    
                int index = JCheckList.this.getSelectedIndex();

                setSelected(index);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
                
        });
         
        
                
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void setModel(ListModel<T> d) {
        var r = new CellListener(d);
        d.addListDataListener(r);
        super.setModel(r);
    }

    protected class CellRenderer implements ListCellRenderer {

        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JCheckBox checkbox = (JCheckBox) value;
            checkbox.setBackground(isSelected
                    ? getSelectionBackground() : getBackground());
            checkbox.setForeground(isSelected
                    ? getSelectionForeground() : getForeground());
            checkbox.setEnabled(isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);
            checkbox.setBorder(isSelected
                    ? UIManager.getBorder(
                            "List.focusCellHighlightBorder") : noFocusBorder);
            return checkbox;
        }
    }
}
