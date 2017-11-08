import java.util.HashSet;

public class WaitingOrder implements Runnable {

	StockManagement sm;

	public WaitingOrder(StockManagement s) {
		this.sm = s;
	}

	@Override
	public void run() {
		SushiDish d;
		int q;
		HashSet<SushiDish> h;
		while(true){
			synchronized (sm.lockOrder){
				try {
					//wait for orders to come or for dishes to be prepared
					sm.lockOrder.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//will use this to complete the first order of every dish
			h=new HashSet<SushiDish>();
			//break down big orders
			for(BigOrder bo: sm.waitingQ)
				if(bo.isComplete()){
					continue;	
				}
				else{
					for(Order o: bo.getList()){
						d=o.getDish();
						if(!o.isComplete() && !h.contains(d)){
							if(bo.getStatus().equals("Queued"))
								bo.changeStatus("Preparing");
							q=o.getQuantity();
							if(q<=d.getQuantity()){
								d.dispatch(q);
								o.complete();
								System.out.println("order of "+q+" "+ d.getName()+ " complete");
							}else{
								h.add(d);
							}
							//either we don't have enough or we just dispatched
							sm.checkRestock(d);
						}
					}
					if(bo.isComplete()){
						sm.deliveryQ.add(bo);
						bo.changeStatus("Ready to be dispatched");
						synchronized(sm.lockDrone){
							sm.lockDrone.notifyAll();
						}	
					}
				}
		}
	}

}
