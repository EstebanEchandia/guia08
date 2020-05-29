package frsf.isi.died.guia08.problema01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.EmpleadoYaAsignadoException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.TareaNoExisteException;
import frsf.isi.died.guia08.problema01.modelo.Empleado.TareaYaFinalizadaException;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.Tarea.TareaYaAsignadaException;
import frsf.isi.died.guia08.problema01.modelo.Tarea.TareaYaFinalizoException;
import frsf.isi.died.guia08.problema01.modelo.Tipo;
public class AppRRHH {
	
	private List<Empleado> empleados = new ArrayList<Empleado>();
	private List<Tarea> tareas = new ArrayList<Tarea>();
	
	
	
	public void agregarTarea(Integer id, String desc, Integer duracion) {
		Tarea tarea = new Tarea(id,desc,duracion);
		tareas.add(tarea);
		
	}
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
					System.out.println("La tarea fue asignada correctamente");
				} catch (TareaYaAsignadaException | TareaYaFinalizoException e1) {

					e1.printStackTrace();
				}
				System.out.println("La tarea fue asignada");
			
			
			
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
					e1.printStackTrace();
				}
				
			
		}
	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo){
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
		try (Reader fileReader = new FileReader (nombreArchivo)){
			try (BufferedReader in = new BufferedReader (fileReader)) {
				String linea = null;
				while( (linea=in.readLine()) != null) {
					String[] fila = linea.split(",");
					this.agregarEmpleadoContratado(Integer.parseInt(fila[0]), fila[1], Double.parseDouble(fila[2]));
					}				
				}	
			} catch (FileNotFoundException e) {
				System.out.println("El archivo no se encontro");
				e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error cargando los datos");
			e.printStackTrace();
		}
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado	
		try (Reader fileReader = new FileReader (nombreArchivo)){
			try (BufferedReader in = new BufferedReader (fileReader)) {
				String linea = null;
				while( (linea=in.readLine()) != null) {
					String[] fila = linea.split(",");
					this.agregarEmpleadoEfectivo(Integer.parseInt(fila[0]), fila[1], Double.parseDouble(fila[2]));
					}				
			}	
		} catch (FileNotFoundException e) {
			System.out.println("El archivo no se encontro");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error cargando los datos");
			e.printStackTrace();
		}
	}

	public void cargarTareasCSV(String nombreArchivo) {
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
		
		try (Reader fileReader = new FileReader (nombreArchivo)){
			try (BufferedReader in = new BufferedReader (fileReader)) {
				String linea = null;
				while( (linea=in.readLine()) != null) {
					String[] fila = linea.split(",");
					this.asignarTarea(Integer.parseInt(fila[3]), Integer.parseInt(fila[0]), fila[1], Integer.parseInt(fila[2]));	
					}				
			}	
		} catch (FileNotFoundException e) {
			System.out.println("El archivo no se encontro");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error cargando los datos");
			e.printStackTrace();
		}
		
	}
	
	private void guardarTareasTerminadasCSV() throws Exception {
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
		
		List<Tarea> tareasFinalizadas = tareas.stream()
									.filter(t -> t.getFacturada()==false && t.getFechaFin()!=null)
									.collect(Collectors.toList());
		
		if(tareasFinalizadas.isEmpty())System.out.println("No hay tareas finalizadas y no facturadas.");
		
		try(Writer fileWriter= new FileWriter("tareasTerminadas.csv",true)) {
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				for(Tarea t: tareasFinalizadas) {
					if(t!=null) {
						out.write(t.guardarTarea()+System.getProperty("line.separator"));
					}
				}
				
			}
		}
			
	}
	
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() {
		try {
			this.guardarTareasTerminadasCSV();
		} catch (Exception e1) {
			System.out.println("Error facturando");
			e1.printStackTrace();
		}
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}
