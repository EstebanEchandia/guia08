package frsf.isi.died.guia08.problema01;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.EmpleadoYaAsignadoException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.TareaNoExisteException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.TareaYaFinalizadaException;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.Tipo;
public class AppRRHH {
	
	private List<Empleado> empleados;
	
	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista
		if (empleados == null) empleados = new ArrayList<Empleado>();
		Empleado e1 = new Empleado(cuil, nombre, Tipo.CONTRATADO, costoHora);
		empleados.add(e1);
		
	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista		
		if (empleados == null) empleados = new ArrayList<Empleado>();
		Empleado e1 = new Empleado(cuil, nombre, Tipo.EFECTIVO, costoHora);
		empleados.add(e1);
	}
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) {
		// crear un empleado
		// con el método buscarEmpleado() de esta clase
		// agregarlo a la lista		
		
		Optional<Empleado> e = buscarEmpleado(emp -> emp.getCuil() == cuil);
		if(e.isPresent()) {
			Tarea t = new Tarea(idTarea, descripcion, duracionEstimada);

				try {
					e.get().asignarTarea(t);
					System.out.println("La tarea fue asignada");
				} catch (EmpleadoYaAsignadoException | TareaYaFinalizadaException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			
			
		}
	}
	
	public void empezarTarea(Integer cuil,Integer idTarea) {
		// busca el empleado por cuil en la lista de empleados
		// con el método buscarEmpleado() actual de esta clase
		// e invoca al método comenzar tarea
		Optional<Empleado> e = buscarEmpleado(emp -> emp.getCuil() == cuil);
		if(e.isPresent()) {
			try {
				e.get().comenzar(idTarea);
				System.out.println("El empleado comenzo la tarea");
			} catch (TareaNoExisteException e1) {
				e1.printStackTrace();
			}	
			
		}
	}
	
	
	public void terminarTarea(Integer cuil,Integer idTarea) {
		Optional<Empleado> e = buscarEmpleado(emp -> emp.getCuil() == cuil);
		if(e.isPresent()) {
				try {
					e.get().finalizar(idTarea);
					System.out.println("La tarea se finalizo correctamente");
				} catch (TareaNoExisteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			
		}
	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado		
	}

	public void cargarTareasCSV(String nombreArchivo) {
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
	}
	
	private void guardarTareasTerminadasCSV() {
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}
