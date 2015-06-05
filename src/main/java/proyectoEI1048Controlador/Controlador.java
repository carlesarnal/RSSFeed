package proyectoEI1048Controlador;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jdom.JDOMException;

import com.sun.syndication.io.FeedException;

import proyectoEI1048Modelo.BDD;
import proyectoEI1048Modelo.BDDVistos;

public class Controlador {
	BDD bdd;
    BDDVistos bddvistos;
	public Controlador(){
		try {
			this.bdd = new BDD();
            this.bddvistos= new BDDVistos();
		} catch (IOException | JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String,Map<String,String>> getCarpetas(){
		return bdd.getCarpetas();
	}
    public void anyadirCarpeta(String nombre) throws IOException{
		bdd.addCarpeta(nombre);
	}
    public boolean removeCapeta(String nombre) throws IOException{
        return bdd.removeCarpeta(nombre);
    }
    public boolean existsCarpeta(String nombre){
        return bdd.existsCarpeta(nombre);
    }


	public int anyadirFuente(String nombre, String url,String nCarpeta) throws IOException {
		if(bdd.existsFuente(nombre, url))
            return -1;
        bdd.addFuente(nombre,url,nCarpeta);
        return 0;
	}
	public void removeFuente(String nombre, String url,String nCarpeta) throws IOException {
		bdd.removeFuente(nombre, url, nCarpeta);
	}
    public boolean existsFuente(String nombre, String url){
        return bdd.existsFuente(nombre, url);

    }

    public int cambiarFuenteCarpeta(String nFuente,String nCarpetaAnt, String nCarpetaNuev,String nUrl) throws IOException {
        return bdd.cambiarCarpeta(nFuente,nCarpetaAnt,nCarpetaNuev,nUrl);
    }
	public List<String> info(String nombre, String url){
		try {
            return bdd.info(url);
		} catch (IllegalArgumentException | IOException | FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


    public List<String> getEtiquetas(String nCarpeta,String nFuente){
        return bdd.getEtiquetas(nCarpeta,nFuente);
    }
    public boolean existsEtiqueta (String nCarpeta, String nFuente, String valor){
        return bdd.existsEtiqueta(nCarpeta,nFuente,valor);
    }
    public void addEtiqueta(String nCarpeta, String nFuente, String valor){
        bdd.addEtiqueta(nCarpeta, nFuente, valor);
    }
    public void deleteEtiqueta(String nCarpeta, String nFuente, String valor){
        bdd.eliminarEtiqueta(nCarpeta, nFuente, valor);
    }
    public  Map<String,String>  buscaEtiqueta(String valor){
        return bdd.busquedaPorEtiqueta(valor);
    }


    public boolean addLeido(String nombre) throws IOException {
        return bddvistos.addLeido(nombre);
    }
    public boolean fuenteLeida(String nombre){
        return bddvistos.fuenteLeida(nombre);
    }
}
