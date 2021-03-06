package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

public class JListRenderer extends JPanel implements ListCellRenderer<Object> {

	private HashMap<String, byte[]> pictures;

	public JListRenderer() {
		setOpaque(true);
		setLayout(new BorderLayout());
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		this.removeAll();
		// according to Oracle the index parameter is unsafe. Nonetheless the
		// Oracle Tutorial teaches it this way.
		String cellText = (String) value;

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		if (pictures == null) {
			// when pictures from the server are available
			byte[] b = pictures.get(cellText);
			ImageIcon icon = new ImageIcon(b);
			JLabel textLabel = new JLabel(cellText);
			JLabel iconLabel = new JLabel(icon);

			textLabel.setFont(list.getFont());
			textLabel.setHorizontalAlignment(SwingConstants.CENTER);
			add(textLabel, BorderLayout.CENTER);
			add(iconLabel, BorderLayout.WEST);

		}
		return this;
	}

	public void setPictures(HashMap<String, byte[]> m) {
		pictures = m;
	}
}
