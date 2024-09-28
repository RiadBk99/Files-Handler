package ViewHelper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class CustomZoomSlider extends BasicSliderUI {
	
	    public CustomZoomSlider(JSlider slider) {
	        super(slider);
	    }

	    @Override
	    public void paintThumb(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setColor(Color.GRAY); // Soft blue
	        g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);  // A rounded thumb
	    }

	    @Override
	    public void paintTrack(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setColor(Color.LIGHT_GRAY);  // Custom track color

	        // Track background
	        g2d.fillRoundRect(trackRect.x, trackRect.y + (trackRect.height / 2) - 4, trackRect.width, 8, 10, 10);

	        // Highlighted track part
	        g2d.setColor(new Color(58, 134, 255)); // Soft blue
	        int fillLength = thumbRect.x - trackRect.x;
	        g2d.fillRoundRect(trackRect.x, trackRect.y + (trackRect.height / 2) - 4, fillLength, 8, 10, 10);
	    }

	    @Override
	    public void paintTicks(Graphics g) {
	        super.paintTicks(g);  // Keep the default tick drawing
	    }

	    @Override
	    public void paintLabels(Graphics g) {
	        super.paintLabels(g);  // Keep the default label drawing
	    }
	
}
