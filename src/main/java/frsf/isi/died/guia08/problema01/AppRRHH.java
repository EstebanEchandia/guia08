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
	
	private List<Empleado> empleados;
	private List<Tarea> tareas;
	
	public AppRRHH() {
		super();
		empleados = new ArrayList<Empleado>();
		tareas = new ArrayList<Tarea>();

	}
	//tested
	public void agregarTarea(Integer id, String desc, Integer duracion) {
		Tarea tarea = new Tarea(id,desc,duracion);
		tareas.add(tarea);
		
	}
	//tested
	public void agregarEmpleadoContratado(Long cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista
		
		Empleado e1 = new Empleado(cuil, nombre, Tipo.CONTRATADO, costoHora);
		empleados.add(e1);
		
	}
	//tested
	public void agregarEmpleadoEfectivo(Long cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista		
		if (empleados == null) empleados = new ArrayList<Empleado>();
		Empleado e1 = new Empleado(cuil, nombre, Tipo.EFECTIVO, costoHora);
		empleados.add(e1);
	}
	
	
	
	public void asignarTarea(Long cuil,Integer idTarea,String descripcion,Integer duracionEstimada) {
		// crear un empleado
		// con el método buscarEmpleado() de esta clase
		// agregarlo a la lista		
		
		Optional<Empleado> e = buscarEmpleado(emp -> emp.getCuil().equals(cuil));
		if(e.isPresent()) {
			Tarea t = new Tarea(idTarea, descripcion, duracionEstimada);
			tareas.add(t);
			System.out.println("La tarea se agrego correctamente");

				try {
					
					Empleado e1 = e.get();
					e1.asignarTarea(t);
					
					System.out.println("La tarea fue asignada correctamente");
				} catch (TareaYaAsignadaException | TareaYaFinalizoException e1) {
					e1.printStackTrace();
				}
		}
		else System.out.println("Las tareas no fueron agregadas");
	}
	
	public void empezarTarea(Long cuil,Integer idTarea) {
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
	
	
	public void terminarTarea(Long cuil,Integer idTarea) {
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
					this.agregarEmpleadoContratado(Long.parseLong(fila[0]), fila[1], Double.parseDouble(fila[2]));
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
					this.agregarEmpleadoEfectivo(Long.parseLong(fila[0]), fila[1], Double.parseDouble(fila[2]));
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
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(",");
					Long cuil = Long.parseLong(fila[3]);
					Integer id = Integer.parseInt(fila[0]);
					String desc = fila[1];
					Integer duracionEstimada = Integer.parseInt(fila[2]);
					this.asignarTarea(cuil, id, desc, duracionEstimada);	
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
	
	public void guardarTareasTerminadasCSV() throws Exception {
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
	public List<Empleado> getEmpleados() {
		return empleados;
	}
	public List<Tarea> getTareas() {
		return tareas;
	}
	
	
}
