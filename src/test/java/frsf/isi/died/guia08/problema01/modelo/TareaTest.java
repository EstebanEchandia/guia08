package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.TareaNoExisteException;
import frsf.isi.died.guia08.problema01.modelo.Tarea.TareaYaAsignadaException;
import frsf.isi.died.guia08.problema01.modelo.Tarea.TareaYaFinalizoException;

public class TareaTest {
	
	public Empleado e1;
	public Empleado e2;
	
	
	public Tarea t1;
	public Tarea t2;

	
	@Before
	public void setUp(){
		e1 = new Empleado(123L, "Marcos Martini", Tipo.CONTRATADO, (double) 10);
		e2 = new Empleado(124L, "Pedro Picapiedras", Tipo.EFECTIVO, (double) 13);
		
		t1 = new Tarea(1, "planchar", 6);
		t2 = new Tarea(2, "lavar platos", 2);
		
		}
	
	@Test
	public void asignarEmpleadoTest() throws TareaYaFinalizoException, TareaYaAsignadaException {
		t1.asignarEmpleado(e1);
		assertTrue(t1.getEmpleadoAsignado() == e1);
	}
	
	
	@Test(expected = TareaYaFinalizoException.class)
	public void asignarEmpleadoTareaYaFinalizoTest() throws TareaYaFinalizoException, TareaYaAsignadaException {
		t1.setFechaFin(LocalDateTime.now());
		t1.asignarEmpleado(e1);
	}
	
	@Test(expected = TareaYaAsignadaException.class)
	public void asignarEmpleadoTareaYaAsignadaTest() throws TareaYaFinalizoException, TareaYaAsignadaException {
		
		t1.asignarEmpleado(e2);
		t1.asignarEmpleado(e1);
		
		
	}

	

}
