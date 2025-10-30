import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

class JTableButtonRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        return (Component) value;
    }
}

class JTableButtonEditor extends DefaultCellEditor {
    protected JButton button;

    public JTableButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        button.setText(((JButton) value).getText());
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
        for (ActionListener al : ((JButton) value).getActionListeners()) {
            button.addActionListener(al);
        }
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }
}
