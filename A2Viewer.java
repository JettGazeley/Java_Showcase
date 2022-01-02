package a2;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class A2Viewer extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 124L;
	
	public static final String FLAG = "FLAG";
	public static final String COPY = "COPY";
	public static final String BORDER_1 = "BORDER_1";
	public static final String BORDER_5 = "BORDER_5";
	public static final String BORDER_10 = "BORDER_10";
	public static final String TO_GRAY = "TO_GRAY";
	public static final String TO_BINARY = "TO_BINARY";
	public static final String FLIP_HORIZONTAL = "FLIP_HORIZONTAL";
	public static final String FLIP_VERTICAL = "FLIP_VERTICAL";
	public static final String ROTATE_RIGHT = "ROTATE_RIGHT";
	public static final String RED_EYE = "RED_EYE";
	public static final String BLUR_1 = "BLUR_1";
	public static final String BLUR_3 = "BLUR_3";
	public static final String BLUR_5 = "BLUR_5";
	public static final String HALF_SIZE = "HALF_SIZE";
	public static final String EXIT = "EXIT";

	JLabel origImg;  // original image
	JLabel compImg;  // computed image
	ImageIcon noPictureIcon;
	
	JMenuBar menuBar;
	JMenu menu;
	
	public A2Viewer() {
		super("Assignment 2");
		
		this.makeMenu();
		this.setJMenuBar(this.menuBar);
		
		JPanel contentPanel = new JPanel();
		JPanel origPanel = new JPanel();
		JPanel compPanel = new JPanel();
		
		this.origImg = new JLabel();
		URL noImg = A2Viewer.class.getResource("No_Picture.jpg");
		this.noPictureIcon = new ImageIcon(noImg);
		
		this.origImg.setIcon(this.noPictureIcon);
		
		this.compImg = new JLabel();
		this.compImg.setIcon(this.noPictureIcon);
		
		origPanel.setLayout(new BoxLayout(origPanel, BoxLayout.PAGE_AXIS));
		origPanel.add(new JLabel("Original image"));
		origPanel.add(this.origImg);
		
		compPanel.setLayout(new BoxLayout(compPanel, BoxLayout.PAGE_AXIS));
		compPanel.add(new JLabel("Computed image"));
		compPanel.add(this.compImg);
		
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setPreferredSize(new Dimension(850, 450));
		contentPanel.add(origPanel);
		contentPanel.add(compPanel);
		this.setContentPane(contentPanel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void makeMenu() {
		this.menuBar = new JMenuBar();
		this.menu = new JMenu("Actions");
		this.menuBar.add(this.menu);
		
		this.addMenuItem("Flag", A2Viewer.FLAG);
		this.addMenuItem("Copy", A2Viewer.COPY);
		this.addMenuItem("Add border, width = 1", A2Viewer.BORDER_1);
		this.addMenuItem("Add border, width = 5", A2Viewer.BORDER_5);
		this.addMenuItem("Add border, width = 10", A2Viewer.BORDER_10);
		this.addMenuItem("Grayscale", A2Viewer.TO_GRAY);
		this.addMenuItem("Binary", A2Viewer.TO_BINARY);
		this.addMenuItem("Flip vertical", A2Viewer.FLIP_VERTICAL);
		this.addMenuItem("Rotate right", A2Viewer.ROTATE_RIGHT);
		this.addMenuItem("Remove red eye", A2Viewer.RED_EYE);
		this.addMenuItem("Blur, radius = 1", A2Viewer.BLUR_1);
		this.addMenuItem("Blur, radius = 3", A2Viewer.BLUR_3);
		this.addMenuItem("Blur, radius = 5", A2Viewer.BLUR_5);
		
		this.menu.addSeparator();
		
		this.addMenuItem("Exit", A2Viewer.EXIT);
		
	}
	
	private void addMenuItem(String label, String action) {
		JMenuItem item = new JMenuItem(label);
		item.setActionCommand(action);
		item.addActionListener(this);
		this.menu.add(item);
	}
	
	public void setOriginal(Picture p) {
		if (p == null) {
			this.origImg.setIcon(this.noPictureIcon);
			return;
		}
		JLabel label = p.getJLabel();
		this.origImg.setIcon(new ImageIcon(((ImageIcon) label.getIcon()).getImage()));
		this.pack();
	}
	
	public void setComputed(Picture p) {
		if (p == null) {
			this.compImg.setIcon(this.noPictureIcon);
			return;
		}
		JLabel label = p.getJLabel();
		this.compImg.setIcon(new ImageIcon(((ImageIcon) label.getIcon()).getImage()));
		this.pack();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(A2Viewer.EXIT)) {
			this.dispose();
			System.exit(0);
		}
		A2.processImage(cmd);
	}
}
