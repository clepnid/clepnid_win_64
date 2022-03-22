package http;

import java.io.File;
import java.io.IOException;

import ventana.Configuracion;

public class OpcionesModulosHttp {
	public enum Tipo {
		Premium, noPremium
	}

	public enum FileSizeMedida {
		Kilobyte, Megabyte, Gigabyte, None
	}

	private static Tipo tipoPremium = Tipo.Premium;
	private static long fileSizeNumber;
	private static FileSizeMedida fileSizeMedida;

	public static boolean esTipoPremium() {
		return tipoPremium.equals(Tipo.Premium);
	}

	private static void comprobarFileSize() {
		Configuracion config = null;
		config = null;
		try {
			config = Configuracion.deserializar();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileSizeNumber = config.filesizenumber;
		fileSizeMedida = config.filesizemedida;
	}

	public static boolean comprobarSize(File fichero) {
		return getFileSize() > fichero.length();
	}

	public static boolean esCorrecto(File fichero) {
		return comprobarSize(fichero);
	}

	/*
	 * retorna dependiendo si la cuenta es premium o no el limite de los ficheros de
	 * subida a la web
	 */
	public static long getFileSize() {
		comprobarFileSize();
		if (!esTipoPremium()) {
			if (fileSizeNumber <= 0) {
				return 2 * 1024 * 1024 * 1024;
			}
			switch (fileSizeMedida) {
			case Kilobyte:
				if (fileSizeNumber > 1024) {
					fileSizeMedida = FileSizeMedida.Megabyte;
					return getFileSize();
				}
				return fileSizeNumber * 1024;
			case Megabyte:
				if (fileSizeNumber > 1024) {
					fileSizeMedida = FileSizeMedida.Gigabyte;
					return getFileSize();
				}
				return fileSizeNumber * 1024 * 1024;
			case Gigabyte:
				if (fileSizeNumber > 2) {
					return 2 * 1024 * 1024 * 1024;
				}
				return fileSizeNumber * 1024 * 1024 * 1024;
			case None:
				return 2 * 1024 * 1024 * 1024;
			default:
				return -1;
			}
		}

		if (fileSizeNumber <= 0) {
			return Long.MAX_VALUE;
		}
		switch (fileSizeMedida) {
		case Kilobyte:
			if (fileSizeNumber > 1024) {
				fileSizeMedida = FileSizeMedida.Megabyte;
				return getFileSize();
			}
			return fileSizeNumber * 1024;
		case Megabyte:
			if (fileSizeNumber > 1024) {
				fileSizeMedida = FileSizeMedida.Gigabyte;
				return getFileSize();
			}
			return fileSizeNumber * 1024 * 1024;
		case Gigabyte:
			if (fileSizeNumber > 1024) {
				return Long.MAX_VALUE;
			}
			return fileSizeNumber * 1024 * 1024 * 1024;
		case None:
			return Long.MAX_VALUE;
		default:
			return -1;
		}
	}

	public static FileSizeMedida getFileSizeMedida() {
		return fileSizeMedida;
	}

	public static void setFileSizeMedida(FileSizeMedida fileSizeMedida) {
		OpcionesModulosHttp.fileSizeMedida = fileSizeMedida;
	}

}
