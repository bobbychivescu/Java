import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class BusinessAppView extends JFrame {

	private StockManagement sm;
	private BusinessApp app;
	
	public BusinessAppView(BusinessApp app){
		
		super("BusinessApp");
		this.setSize(1200, 800);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.app=app;
		this.sm=app.getSM();
		init();
		this.setVisible(true);
	}
	
	private void init(){
		
		//this will save the current state when we close the app
		this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                DataPersistence.saveState(app);
                e.getWindow().dispose();
            }
        });
		
		this.setLayout(new GridLayout(2, 3));
		this.add(new CategoryPane<Supplier>(sm.suppliers, "Suppliers", app));
		this.add(new CategoryPane<Ingredient>(sm.ingredients, "Ingredients", app));
		this.add(new CategoryPane<SushiDish>(sm.dishes, "Dishes", app));
		this.add(new CategoryPane<KitchenStaff>(app.staff, "Staff", app));
		this.add(new CategoryPane<Drone>(app.drones, "Drones", app));
		this.add(new CategoryPane(sm.waitingQ, "Orders", app));
	}
}
