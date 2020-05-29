package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea.TareaYaAsignadaException;
import frsf.isi.died.guia08.problema01.modelo.Tarea.TareaYaFinalizoException;

public class AppRRHHTest {
	
	
	
	private AppRRHH app;
	private Empleado e1,e2;

	
	

	@Before
	public void setUp(){
		app = new AppRRHH();
		e1 = new Empleado(2042679480L, "Esteban", Tipo.CONTRATADO, 13.0);
		
		}
	
	@Test
	public void agregarTareaTest() {
		Integer id = 1;
		app.agregarTarea(id, "", 10);
		assertTrue(app.getTareas().get(0) != null);
		Tarea t = app.getTareas().get(0);
		
		assertEquals(t.getId(), id);
	}
	
	@Test
	public void agregarEmpleadoContratadoTest() {
		Long cuil = 2042489480L;
		app.agregarEmpleadoContratado(cuil, "Esteban", 10.0);
		Empleado e1 = app.getEmpleados().get(0);
		assertEquals(Tipo.CONTRATADO, e1.getTipo());
		assertEquals(cuil, e1.getCuil());
		
	}
	
	@Test
	public void agregarEmpleadoEfectivoTest() {
		Long cuil = 2042489480L;
		app.agregarEmpleadoEfectivo(cuil, "Esteban", 10.0);
		Empleado e1 = app.getEmpleados().get(0);
		assertEquals(Tipo.EFECTIVO, e1.getTipo());
		assertEquals(cuil, e1.getCuil());
		
	}
	@Test
	public void asignarTareaTest() {
		Long cuil = 2042679480L;
		Integer id = 1;
		
		app.agregarEmpleadoContratado(cuil, "Esteban", 13.0);
		Empleado e2 = app.getEmpleados().get(0);
		app.asignarTarea(cuil, id, "", 6);
		
		Tarea t1 = e2.getTareasAsignadas().get(0);
		assertEquals(t1.getId(), id);
		
	}
	
	@Test
	public void empezarTareaTest() throws TareaYaAsignadaException, TareaYaFinalizoException {
		Long cuil = 2042679480L;
		Integer id = 1;
		
		app.agregarTarea(id, "", 12);
		app.agregarEmpleadoContratado(cuil, "Esteban", 15.0);
		
		Empleado e = app.getEmpleados().get(0);
		Tarea t = app.getTareas().get(0);
		e.asignarTarea(t);
		app.empezarTarea(cuil, id);
		
		assertTrue(e.getTareasAsignadas().contains(t));
		
	}
	
	@Test
	public void terminarTareaTest() throws TareaYaAsignadaException, TareaYaFinalizoException {
		Long cuil = 2042679480L;
		Integer id = 1;
		
		app.agregarTarea(id, "", 12);
		app.agregarEmpleadoContratado(cuil, "Esteban", 15.0);
		
		Empleado e = app.getEmpleados().get(0);
		Tarea t = app.getTareas().get(0);
		e.asignarTarea(t);
		app.empezarTarea(cuil, id);
		app.terminarTarea(cuil, id);
		assertTrue(t.getFechaFin()!=null);
		
	}
	
	@Test
	public void cargarEmpleadosContratadosCSVTest() {
		Long cuil1 = 20434712523L;
		Long cuil2 = 25465227323L;
		Double costoHora = 12.0;
		app.cargarEmpleadosContratadosCSV("empleadosContratados.csv");
		Empleado e1 = app.getEmpleados().get(0);
		Empleado e2 = app.getEmpleados().get(1);
		Empleado e3 = app.getEmpleados().get(2);
		
		assertEquals(e1.getCuil(), cuil1);
		assertEquals(e2.getCuil(), cuil2);
		assertEquals(e1.getNombre(), "Felipe");
		assertEquals(e3.getCostoHora(), costoHora);
	}
	
	@Test
	public void cargarEmpleadosEfectivosCSVTest() {
		Long cuil1 = 20434707323L;
		Long cuil2 = 20435227323L;
		Double costoHora = 16.0;
		app.cargarEmpleadosEfectivosCSV("empleadosEfectivos.csv");
		
		Empleado e1 = app.getEmpleados().get(0);
		Empleado e2 = app.getEmpleados().get(1);
		Empleado e3 = app.getEmpleados().get(2);
		
		assertEquals(e1.getCuil(), cuil1);
		assertEquals(e2.getCuil(), cuil2);
		assertEquals(e1.getNombre(), "Esteban");
		assertEquals(e3.getCostoHora(), costoHora);
	}
	
	@Test
	public void cargarTareasCSVTest() {
		app.cargarEmpleadosContratadosCSV("empleadosContratados.csv");
		app.cargarEmpleadosEfectivosCSV("empleadosEfectivos.csv");
		app.cargarTareasCSV("tareas.csv");
		
		List<Tarea> tareas = app.getTareas();
		Long cuil3 = 20434621223L;
		assertFalse(tareas.isEmpty());
		
		assertTrue(tareas.get(0).getId().equals(1));
		assertEquals(tareas.get(1).getDescripcion(), "bailar");
		assertEquals(tareas.get(2).getEmpleadoAsignado().getCuil(), cuil3);
	}
	
	@Test
	public void	guardarTareasTerminadasCSVTest() throws Exception {
		app.cargarEmpleadosContratadosCSV("empleadosContratados.csv");
		app.cargarEmpleadosEfectivosCSV("empleadosEfectivos.csv");
		app.cargarTareasCSV("tareas.csv");
		
		List<Tarea> tareas = app.getTareas();
		
		for(Tarea t: tareas) {
			t.setFechaInicio(LocalDateTime.now());
			t.setFechaFin(LocalDateTime.now().plusDays(1));
			
		}
		app.guardarTareasTerminadasCSV();
		
	}
	
	

}
