package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import died.guia06.Alumno;
import died.guia06.Curso;
import frsf.isi.died.guia08.problema01.modelo.Empleado.EmpleadoYaAsignadoException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.TareaNoExisteException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.TareaYaFinalizadaException;
import frsf.isi.died.guia08.problema01.modelo.Tarea.TareaYaAsignadaException;
import frsf.isi.died.guia08.problema01.modelo.Tarea.TareaYaFinalizoException;

public class EmpleadoTest {

	// IMPORTANTE
	// ESTA CLASE ESTA ANOTADA COMO @IGNORE por lo que no ejecutará ningun test
	// hasta que no borre esa anotación.
	
	public Empleado e1;
	public Empleado e2;
	public Empleado e3;
	
	public Tarea t1;
	public Tarea t2;
	public Tarea t3;
	public Tarea t4;
	public Tarea t5;
	public Tarea t6;
	public Tarea t7;
	public Tarea t8;
	
	@Before
	public void setUp() throws Exception {
		e1 = new Empleado(123, "Marcos Martini", Tipo.CONTRATADO, (double) 10);
		e2 = new Empleado(124, "Pedro Picapiedras", Tipo.EFECTIVO, (double) 13);
		e3 = new Empleado(125, "Esteban Echajbdus", Tipo.CONTRATADO, (double) 14);
		
		t1 = new Tarea(1, "planchar", 6);
		t2 = new Tarea(2, "lavar platos", 2);
		t3 = new Tarea(3, "cocinar", 10);
		t4 = new Tarea(4, "dormir", 6);
		t5 = new Tarea(5, "mirar tele", 2);
		t6 = new Tarea(6, "colgar ropa", 10);
		
		t7 = new Tarea(7, "Leer", 10);
		t8 = new Tarea(8, "caminar", 5);
		}
	
	
	@Test
	public void testSalario() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException{
		
		e1.asignarTarea(t1);
		e1.comenzar(1,"4-5-2020 08:00");
		e1.finalizar(1,"6-5-2020 16:00");
		Double res = e1.salario();
		Double esp =  60.0;
		boolean resF = (res == esp);
		assertTrue(resF);
		
	}

	@Test
	public void testCostoTareaNoFinalizada() throws TareaYaAsignadaException, TareaYaFinalizoException {
		e1.asignarTarea(t1);
		Double res = e1.costoTarea(t1);
		Double esp =  60.0;
		assertEquals(res,esp);
	}
	
	@Test
	public void testCostoTareaFinalizadaContratado() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		
		e1.asignarTarea(t1);
		e1.comenzar(1);
		t1.setFechaFin(LocalDateTime.now().plusDays(3));
		e1.finalizar(1);
		
		Double res = e1.costoTarea(t1);
		Double esp =  60.0;
		assertEquals(res,esp);
	}

	@Test
	public void testAsignarTareaContratado() throws TareaYaAsignadaException, TareaYaFinalizoException{
		e1.asignarTarea(t1);
		assertTrue(e1.getTareasAsignadas().contains(t1));
		e1.asignarTarea(t2);
		e1.asignarTarea(t3);
		e1.asignarTarea(t4);
		e1.asignarTarea(t5);
		boolean agregoTareaContratado = e1.asignarTarea(t6);
		assertFalse(agregoTareaContratado);
		assertFalse(e1.getTareasAsignadas().contains(t6));
		
	}
	
	@Test
	public void testAsignarTareaEfectivo() throws TareaYaAsignadaException, TareaYaFinalizoException {
		boolean agregoTareaEfectivo = e2.asignarTarea(t7);
		assertTrue(agregoTareaEfectivo);
		assertTrue(e2.getTareasAsignadas().contains(t7));
		e2.asignarTarea(t8);
		boolean noAgregoTareaHoras = e2.asignarTarea(t8);
		assertFalse(noAgregoTareaHoras);
		assertFalse(e2.getTareasAsignadas().contains(t8));
	}
	
	@Test(expected = TareaYaAsignadaException.class)
	public void testAsignarTareaExceptionYaAsignado() throws TareaYaAsignadaException, TareaYaFinalizoException{
		e1.asignarTarea(t1);
		e2.asignarTarea(t1);
	}
	
	@Test(expected = TareaYaFinalizoException.class)
	public void testAsignarTareaExceptionTareaFinalizada() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		e1.asignarTarea(t1);
		e1.comenzar(1);
		e1.finalizar(1);
		e2.asignarTarea(t1);
	}
	@Test(expected = TareaNoExisteException.class)
	public void testAsignarTareaExceptionTareaNoExiste() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		e1.comenzar(10000);
	}
	
	
	

	@Test
	public void testComenzarInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testFinalizarInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testComenzarIntegerString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFinalizarIntegerString() {
		fail("Not yet implemented");
	}

}
