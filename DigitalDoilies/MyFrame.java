import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyFrame extends JFrame {

	private DrawingPanel pane;
	private Gallery gallery;
	private Thumbnail currentTn;
	
	public MyFrame(){
		super("Digital Doilies");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}
	
	//adding all buttons, panels and their functionality
	private void init(){
		pane = new DrawingPanel();
		gallery = new Gallery();
		currentTn=null;
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				pane.clear();
			}
		});
		
		JButton undo = new JButton("Undo");
		undo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				pane.undo();
			}
		});
		
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Thumbnail tn = new Thumbnail(pane.save());
				tn.addMouseListener(new MouseAdapter(){
					@Override
					public void mouseClicked(MouseEvent e){
						if(currentTn!=null){
							currentTn.setSelected(false);
							currentTn.repaint();
						}
						currentTn=(Thumbnail)e.getSource();
						currentTn.setSelected(true);
						currentTn.repaint();
					}
				});
				gallery.addImage(tn);
				gallery.revalidate();
			}
		});
		
		Integer a[] = new Integer[20];
		for(int i =1; i<=20; ++i)a[i-1]=i;
		JComboBox sizes = new JComboBox(a);
		sizes.setSelectedItem(DrawingPanel.DEFAULT_SIZE);
		sizes.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Object item = ((JComboBox)e.getSource()).getSelectedItem();
				pane.setPointSize((Integer)item);
			}
		});
		
		a= new Integer[38];
		for(int i =3; i<=40; ++i)a[i-3]=i;
		JComboBox sectors = new JComboBox(a);
		sectors.setSelectedItem(DrawingPanel.DEFAULT_SECTORS);
		sectors.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Object item = ((JComboBox)e.getSource()).getSelectedItem();
				pane.setSectors((Integer)item);
			}
		});
		
		JCheckBox showLines = new JCheckBox("Show secor lines");
		showLines.setSelected(true);
		showLines.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
						pane.setVisibleLines(true);
					else
						pane.setVisibleLines(false);
			}
		});
		
		JCheckBox reflect = new JCheckBox("Reflect");
		reflect.setSelected(false);
		reflect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
						pane.setReflection(true);
					else
						pane.setReflection(false);
			}
		});
		
		JButton chooseColor = new JButton("Pen color");
		chooseColor.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				pane.setColor(JColorChooser.showDialog(new Canvas(), "Choose Color of Pen", Color.BLUE));	
			}
		});
		
		JButton delete = new JButton("Delete");
		delete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentTn!=null){
					gallery.remove(currentTn);
					currentTn=null;
					gallery.revalidate();
					gallery.repaint();
				}
			}
		});
		
		JButton load = new JButton("Load copy");
		load.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentTn!=null){
					pane.setLines(currentTn.getLines());
					pane.repaint();
				}
			}
		});
		
		JButton view = new JButton("View");
		view.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentTn!=null){
					currentTn.view();
				}
			}
		});
		
		JButton saveToPc= new JButton("Save to PC");
		saveToPc.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentTn!=null){
					try {
						currentTn.saveToPc();
					} catch (IOException e1) {
						System.out.println("Couldnt save");
					}
				}
			}
		});
		
		JPanel i1 = new JPanel();
		i1.setLayout(new FlowLayout());
		i1.add(clear);
		i1.add(undo);
		i1.add(save);
		JPanel i2 = new JPanel();
		i2.setLayout(new FlowLayout());
		i2.add(new JLabel("Sectors"));
		i2.add(sectors);
		i2.add(showLines);
		i2.add(reflect);
		JPanel i3 = new JPanel();
		i3.setLayout(new FlowLayout());
		i3.add(new JLabel("Pen size"));
		i3.add(sizes);
		i3.add(chooseColor);
		
		JPanel menu = new JPanel();
		menu.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
		menu.add(i1);
		menu.add(i2);
		menu.add(i3);
		
		JPanel imageMenu = new JPanel();
		imageMenu.setLayout(new GridLayout(4,1));
		imageMenu.add(delete);
		imageMenu.add(load);
		imageMenu.add(view);
		imageMenu.add(saveToPc);
		
		int labelHeight=40;
		
		this.setLayout(null);
		this.getContentPane().setPreferredSize(new Dimension(2*Thumbnail.DEFAULT_WIDTH + DrawingPanel.DEFAULT_WIDTH, 
				labelHeight + DrawingPanel.DEFAULT_HEIGHT));
		
		JLabel label = new JLabel("Gallery");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("Arial", 0, 20));
		label.setSize(2*Thumbnail.DEFAULT_WIDTH, labelHeight);
		label.setLocation(0, 0);
		this.add(label);
		imageMenu.setSize(2*Thumbnail.DEFAULT_WIDTH , 2*Thumbnail.DEFAULT_HEIGHT);
		imageMenu.setLocation(0, labelHeight + 6*Thumbnail.DEFAULT_HEIGHT);
		this.add(imageMenu);
		pane.setLocation(2*Thumbnail.DEFAULT_WIDTH , labelHeight);
		pane.setSize(DrawingPanel.DEFAULT_WIDTH, DrawingPanel.DEFAULT_HEIGHT);
		this.add(pane);
		menu.setLocation(2*Thumbnail.DEFAULT_WIDTH , 0);
		menu.setSize(DrawingPanel.DEFAULT_WIDTH, labelHeight);
		this.add(menu);
		gallery.setLocation(0, labelHeight);
		gallery.setSize(2*Thumbnail.DEFAULT_WIDTH , 6*Thumbnail.DEFAULT_HEIGHT);
		this.add(gallery);
	}
}
