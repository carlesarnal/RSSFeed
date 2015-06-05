package EI1048.ProyectoEI1048;

import junit.framework.TestCase;
import org.jdom.JDOMException;
import org.junit.Test;
import proyectoEI1048Modelo.BDD;

import java.io.IOException;

public class H05Test extends TestCase {
	@Test
	public void testAñadirEtiquetaCorrectamenteEnFuenteExistente () throws IOException, JDOMException {
        BDD bdd = new BDD();
        bdd.addCarpeta("Carpeta Prueba");
        bdd.addFuente("Fuente Prueba","URL prueba","Carpeta Prueba");
        assertTrue(bdd.addEtiqueta("Carpeta Prueba","Fuente Prueba", "Ola K Ase"));
        bdd.removeFuente("Fuente Prueba","URL prueba","Carpeta Prueba");
        bdd.removeCarpeta("Carpeta Prueba");
	}
    @Test
    public void testAñadirEtiquetaRepetida () throws IOException, JDOMException {
        BDD bdd = new BDD();
        bdd.addCarpeta("Carpeta Prueba");
        bdd.addFuente("Fuente Prueba","URL prueba","Carpeta Prueba");
        bdd.addEtiqueta("Carpeta Prueba","Fuente Prueba", "Ola K Ase");
        assertFalse(bdd.addEtiqueta("Carpeta Prueba","Fuente Prueba", "Ola K Ase"));
        bdd.removeFuente("Fuente Prueba","URL prueba","Carpeta Prueba");
        bdd.removeCarpeta("Carpeta Prueba");

    }
	@Test
	public void AñadirEtiquetaEnUnaFuenteInexistente () throws IOException, JDOMException {
        BDD bdd = new BDD();
        bdd.addCarpeta("Carpeta Prueba");
        assertFalse(bdd.addEtiqueta("Carpeta Prueba","Fuente Inexistente", "Ola K Ase"));
        bdd.removeCarpeta("Carpeta Prueba");
	}
}