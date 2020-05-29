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
	public void setUp(){
		e1 = new Empleado(123L, "Marcos Martini", Tipo.CONTRATADO, (double) 10);
		e2 = new Empleado(124L, "Pedro Picapiedras", Tipo.EFECTIVO, (double) 13);
		e3 = new Empleado(125L, "Esteban Echajbdus", Tipo.CONTRATADO, (double) 14);
		
		t1 = new Tarea(1, "planchar", 6);
		t2 = new Tarea(2, "lavar platos", 2);
		t3 = new Tarea(3, "cocinar", 10);
		t4 = new Tarea(4, "dormir", 6);
		t5 = new Tarea(5, "mirar tele", 2);
		t6 = new Tarea(6, "colgar ropa", 10);
		t7 = new Tarea(7, "Lsaleer", 10);
		t8 = new Tarea(8, "caminar", 5);
		}
	
	
	@Test
	public void testSalarioEmpleadoContratado() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException{
		
		e1.asignarTarea(t1);
		
		e1.comenzar(1);
		e1.finalizar(1);
		t1.setFechaFin(LocalDateTime.now().plusDays(1));
		
		Double res = e1.salario();
		Double esp =  78.0;
	
		assertEquals(res,esp);
		
	}
	
	@Test
	public void testSalarioEmpleadoEfectivo() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException{
		
		e2.asignarTarea(t1);
		
		e2.comenzar(1);
		e2.finalizar(1);
		t1.setFechaFin(LocalDateTime.now().plusDays(3));
		
		Double res = e2.salario();
		Double esp =  78.0;
	
		assertEquals(res,esp);
		
	}

	@Test
	public void testCostoTareaNoFinalizada() throws TareaYaAsignadaException, TareaYaFinalizoException {
		e1.asignarTarea(t1);
		Double res = e1.costoTarea(t1);
		Double esp =  60.0;
		assertEquals(res,esp);
	}
	
	@Test
	public void testCostoTareaFinalizadaNormalContratado() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		
		e1.asignarTarea(t1);
		e1.comenzar(1);
		e1.finalizar(1);
		t1.setFechaFin(LocalDateTime.now().plusDays(3));
		
		Double res = e1.costoTarea(t1);
		Double esp =  60.0;
		
		assertEquals(res,esp);
	}
	
	@Test
	public void testCostoTareaFinalizadaRapidoContratado() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		
		e1.asignarTarea(t1);
		e1.comenzar(1);
		e1.finalizar(1);
		t1.setFechaFin(LocalDateTime.now().plusDays(1));
		
		Double res = e1.costoTarea(t1);
		Double esp =  78.0;
		
		assertEquals(res,esp);
	}
	
	@Test
	public void testCostoTareaFinalizadaLentoContratado() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		
		e1.asignarTarea(t1);
		e1.comenzar(1);
		e1.finalizar(1);
		t1.setFechaFin(LocalDateTime.now().plusDays(8));
		
		Double res = e1.costoTarea(t1);
		Double esp =  45.0;
		
		assertEquals(res,esp);
	}
	
	@Test
	public void testCostoTareaFinalizadaNormalEfectivo() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		
		e2.asignarTarea(t3);
		e2.comenzar(3);
		e2.finalizar(3);
		t3.setFechaFin(LocalDateTime.now().plusDays(3));
		
		Double res = e2.costoTarea(t3);
		Double esp =  130.0;
		
		assertEquals(res,esp);
	}
	
	@Test
	public void testCostoTareaFinalizadaRapidoEfectivo() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		
		e2.asignarTarea(t3);
		e2.comenzar(3);
		e2.finalizar(3);
		t3.setFechaFin(LocalDateTime.now().plusDays(1));
		
		Double res = e2.costoTarea(t3);
		Double esp =  156.0;
		
		assertEquals(res,esp);
	}
	
	@Test
	public void testCostoTareaFinalizadaLentoEfectivo() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		
		e2.asignarTarea(t3);
		e2.comenzar(3);
		e2.finalizar(3);
		t3.setFechaFin(LocalDateTime.now().plusDays(10));
		
		Double res = e2.costoTarea(t3);
		Double esp =  130.0;
		
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
	public void testComenzarInteger() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		
		e1.asignarTarea(t1);
		e1.comenzar(1);
		assertTrue(t1.getFechaInicio()!=null);
		
	}
	
	@Test(expected = TareaYaFinalizoException.class)
	public void testComenzarTareaYaFinalizo() throws TareaYaFinalizoException, TareaYaAsignadaException, TareaNoExisteException{
		e2.asignarTarea(t1);
		e2.comenzar(1);
		e2.finalizar(1);
		
		e1.asignarTarea(t1);
		e1.comenzar(1);
	}

	@Test
	public void testFinalizarInteger() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		e1.asignarTarea(t1);
		e1.comenzar(1);
		e1.finalizar(1);
		assertTrue(t1.getFechaFin()!=null);
	}

	@Test
	public void testComenzarIntegerString() throws TareaYaAsignadaException, TareaYaFinalizoException, TareaNoExisteException {
		e1.asignarTarea(t1);
		e1.comenzar(1, "04-05-2020 08:00");
		assertEquals(t1.getFechaInicio().getDayOfYear(),LocalDateTime.of(2020, 05, 04, 8, 00).getDayOfYear());
	}

	@Test
	public void testFinalizarIntegerString() throws TareaNoExisteException, TareaYaAsignadaException, TareaYaFinalizoException {
		e1.asignarTarea(t1);
		e1.comenzar(1);
		e1.finalizar(1, "04-05-2020 08:00");
		assertEquals(t1.getFechaFin().getDayOfYear(),LocalDateTime.of(2020, 05, 04, 8, 00).getDayOfYear());
	}

}
