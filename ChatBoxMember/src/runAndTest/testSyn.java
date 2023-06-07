package runAndTest;

public class testSyn {
	private static class ThreadMain extends Thread {
		private Object lock;
		ThreadCon thc;
		public ThreadMain() {
			this.lock = new Object();
		}
		@Override
		public void run() {
			thc = new ThreadCon();
			thc.start();
			
			try {
				sleep(4000);
				request();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		private void request() throws InterruptedException {
			System.out.println("truoc request");
			thc.stopThreadCon();
			sleep(8000);
			System.err.println("request thread main");
			runThreadCon();
			System.out.println("sau request");
		}

		public void runThreadCon() {
			lock.notify();
		}
		
		private class ThreadCon extends Thread{
			private Object lock;
			private int count = 0;
			public ThreadCon() {
				this.lock = new Object();
			}
			@Override
			public void run() {
				synchronized (lock) { 
					while(true) {
						try {
							sleep(2000);
							count++;
							System.out.println("Thread con : "+count);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			public void stopThreadCon() throws InterruptedException {
				synchronized (lock) {
					lock.wait();
				}
			}
		}
	}
	
    public static void main(String[] args) {
    	ThreadMain m = new ThreadMain();
    	m.start();
    }
}

