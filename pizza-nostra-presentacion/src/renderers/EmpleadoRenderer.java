package renderers;

import entidades.Empleado;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class EmpleadoRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Empleado) {
            Empleado empleado = (Empleado) value;
            setText(empleado.getNombre() + " " + empleado.getApellidos());
        }
        return this;
    }
}
