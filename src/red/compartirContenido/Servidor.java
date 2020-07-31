package red.compartirContenido;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import red.broadcast.BroadcastingIpControl;
import ventana.Ventana;

/**
 * Clase que extiende de {@link Thread}, crea hilos {@link OperacionServidor}
 * para conectar con varios {@link Cliente} y conforme terminan se crean nuevos.
 * 
 * @author: Pavon
 * @version: 17/05/2020
 * @since 1.0
 */

public class Servidor extends Thread {

	Ventana ventana;
	BroadcastingIpControl controlBroadcasting;
	public ServerSocket servidor = null;

	/**
	 * Inicia las variables
	 * 
	 * @param ventana             {@link Ventana}
	 * @param controlBroadcasting {@link controlBroadcasting} para detener la
	 *                            ejecucion del hilo en caso de que el hilo
	 *                            BroadcastingIpControl finalice.
	 */

	public Servidor(Ventana ventana, BroadcastingIpControl controlBroadcasting) {
		this.ventana = ventana;
		this.controlBroadcasting = controlBroadcasting;
	}

	/**
	 * crea hilos {@link OperacionServidor} para conectar con varios {@link Cliente}
	 * a la vez y conforme terminan se crean nuevos.
	 */

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		boolean reponer = false;
		int cont = 1, num = 3;
		ArrayList<OperacionServidor> operaciones = new ArrayList<OperacionServidor>();

		// puerto de nuestro servidor
		final int PUERTO = 5000;
		// Creamos el socket del servidor
		try {
			servidor = new ServerSocket(PUERTO);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (cont <= num && controlBroadcasting.seguir) {
			System.out.print("");
			// cuando el equipo es servidor
			if (controlBroadcasting.soyServidor && !controlBroadcasting.serServidor
					&& !controlBroadcasting.noSerServidor && !(ventana.contenido == null)) {
				operaciones.add(new OperacionServidor(servidor, this));
				operaciones.get(operaciones.size() - 1).start();
				cont++;
			}
			while (controlBroadcasting.seguir && controlBroadcasting.soyServidor && !controlBroadcasting.serServidor
					&& !controlBroadcasting.noSerServidor && !(ventana.contenido == null)) {
				if (reponer) {
					operaciones.add(new OperacionServidor(servidor, this));
					operaciones.get(operaciones.size() - 1).start();
					reponer = false;
				}
				for (int i = 0; i < operaciones.size(); i++) {
					if (!operaciones.get(i).conectado) {
						reponer = true;
						operaciones.remove(i);
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			cont = 1;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		try {
			if (servidor != null) {
				if (!servidor.isClosed()) {
					servidor.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}