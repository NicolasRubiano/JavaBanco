package Thread;

import java.util.concurrent.locks.*;


public class BancoSinSincronizar {
//------------------CLASE MAIN-----------------------------
	public static void main(String[] args) {
Banco b =new Banco();
for(int i=0;i<100;i++) {
	EjecucionTransferencias r =new EjecucionTransferencias(b,i,2000);
	Thread t=new Thread(r);

	t.start();
}
	}

}



//------------------CLASE BANCO----------------------------
class Banco{

	//----------------Constructor
	public Banco() {
		cuentas=new double[100];
		for(int i=0;i<cuentas.length;i++) {
			cuentas[i]=2000;
		}
		saldoSuficiente=cierreBanco.newCondition();//Bloqueo se establece en base a una condicon
	
	}
	
	//-----------------Metodo para transferencias
	public void transferencia(int cuentaOrigen,int cuentaDestino,double cantidad) {
		cierreBanco.lock();//Clase para bloquear que entren dos hilos a la vez instanciado en variables
		try {
			
		while(cuentas[cuentaOrigen]<cantidad) {//si cuenta tiene menos plata
			try {
				saldoSuficiente.await();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
		System.out.println(Thread.currentThread());//imprime el hilo
		
		cuentas[cuentaOrigen]-=cantidad;//dinero que sale de la cuenta de origen
		
		System.out.printf("%10.2f de %d para %d", cantidad,cuentaOrigen,cuentaDestino);//muestra la cantidad a transferir
		
		cuentas[cuentaDestino]+=cantidad;// suma dinero a la cuenta destino
	
		System.out.printf("Saldo total: %10.2f%n",getSaldoTotal());
		
		saldoSuficiente.signalAll();//Avisa al hilo que esta a la espera en el while que ya hizo una transferencia otro hilo
		}finally {
			cierreBanco.unlock();
		}
	}
	
	//-----------------Metodo dar saldo total
		public double getSaldoTotal() {
		double suma_cuentas=0;
		for(double a: cuentas) {
			suma_cuentas+=a;
			
		}
		return suma_cuentas;
		}
		
		
	                                                                                                                                                                                                                                                                                                                                             	
	
	//--------------------Variables
	private final double [] cuentas; 
	private Lock cierreBanco=new ReentrantLock();
	private Condition saldoSuficiente; 

}



//------------------CLASE EJECUCION TRANSFERENCIA--------------

class EjecucionTransferencias implements Runnable{

	private Banco banco;
	private int deLaCuenta;
	private double CantidadMax;
	public EjecucionTransferencias(Banco b,int de,double max){
		banco=b;
		deLaCuenta=de;
		CantidadMax=max;
	}
	
	
	@Override
	public void run() {
	
		while(true) {
		int paraLaCuenta=(int)(100*Math.random());
		double cantidad =(CantidadMax)*Math.random();
		banco .transferencia(deLaCuenta, paraLaCuenta, cantidad);
		
		try {
			Thread.sleep((int)Math.random()*10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
	}
	
	
	
	
}
