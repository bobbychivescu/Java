import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CategoryPane2<T> extends JPanel {

	DefaultListModel<T> lm;
	JList<T> list;
	ClientApp app;

	public CategoryPane2(DefaultListModel<T> lm, String title, ClientApp app){
		super();
		this.lm=lm;
		list = new JList<T>();
		this.app = app;
		init(title);
	}

	private void init(String title) {
		this.setLayout(new BorderLayout());
		JLabel label = new JLabel(title);
		label.setFont(new Font("Arial", Font.BOLD, 20));
		this.add(label, BorderLayout.NORTH);

		list.setModel(lm);

		JPanel buttons = makeButtons(title);

		this.add(buttons, BorderLayout.EAST);
		this.add(new JScrollPane(list), BorderLayout.CENTER);
	}

	private JPanel makeButtons(String title){
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		if(!title.equals("Current Order")){
			JButton view = new JButton("View");
			view.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					T el = list.getSelectedValue();
					if(el!=null)
						((Displayable)el).view();
				}
			});
			pane.add(view);
		}
		else{
			JButton po = new JButton("Place order");
			po.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					app.placeOrder();
					lm.removeAllElements();
				}
			});
			pane.add(po);
		}
		return pane;
	}
	
	public T getSelected(){
		return list.getSelectedValue();
	}
	
	public void addSouthPanel(JPanel p){
		this.add(p, BorderLayout.SOUTH);
	}
}
