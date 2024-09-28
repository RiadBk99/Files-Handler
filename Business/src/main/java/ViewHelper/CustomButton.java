package ViewHelper;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class CustomButton extends JButton {
    private static final long serialVersionUID = 1L;
    private int cornerRadius; // Radius for rounded corners

    public CustomButton(String text, int width, int height, int cornerRadius) {
        super(text);
        this.cornerRadius = cornerRadius; // Set the corner radius

        // Set button properties
        setFont(new Font("Arial", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setBackground(new Color(58, 134, 255)); // Soft blue
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false); // Do not fill the content area to draw a custom shape
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(width, height)); // Set preferred size for the button

        // Add hover effect
        addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(71, 156, 255)); // Lighter blue on hover
                repaint(); // Repaint to apply color change
            }

			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(58, 134, 255)); // Original color
                repaint(); // Repaint to revert color
            }
        });
    }

	@Override
    public Dimension getPreferredSize() {
        // Use the specified button size for the preferred size
        return super.getPreferredSize();
    }

	@Override
    protected void paintComponent(Graphics g) {
        // Enable anti-aliasing for smoother graphics
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the button's rounded rectangle shape
        int width = getWidth();
        int height = getHeight();

        // Set the color for the button's background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);

        // Draw the button's border
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);

        // Draw the button's text
        g2.setColor(Color.BLACK);
        g2.setFont(getFont());
        FontMetrics metrics = g2.getFontMetrics(getFont());
        int stringWidth = metrics.stringWidth(getText());
        int stringHeight = metrics.getHeight();
        g2.drawString(getText(), (width - stringWidth) / 2, (height + stringHeight) / 2 - 4);
    }
}
