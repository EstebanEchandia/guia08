package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import frsf.isi.died.guia08.problema01.modelo.Tarea.TareaYaAsignadaException;
import frsf.isi.died.guia08.problema01.modelo.Tarea.TareaYaFinalizoException;

import java.time.temporal.ChronoUnit;

public class Empleado {


	
	private Long cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	private LocalDate fechaContratacion;
	
	private Function<Tarea, Double> calculoPagoPorTarea;		
	

	public List<Tarea> getTareasAsignadas() {
		return tareasAsignadas;
	}


	public Empleado(Long cuil, String nombre, Tipo tipo, Double costoHora) {
		super();
		this.cuil = cuil;
		this.nombre = nombre;
		this.tipo = tipo;
		this.costoHora = costoHora;
		this.tareasAsignadas = new ArrayList<Tarea>();
		
		switch(tipo) {
			
			case EFECTIVO: 
					
					calculoPagoPorTarea = (Tarea t)->{
						double costoTarea=0.0;
						int diasEntre = (int) ChronoUnit.DAYS.between(t.getFechaInicio(), t.getFechaFin());
						int horasTrabajoRealizado = diasEntre*4;
						
						if(horasTrabajoRealizado < t.getDuracionEstimada()) {
							costoTarea= t.getDuracionEstimada()*this.costoHora*1.2;
						}
						else costoTarea = t.getDuracionEstimada()*this.costoHora;
						return costoTarea;
					};
				break;
								
			case CONTRATADO: 
					
					calculoPagoPorTarea = (Tarea t)->{
						double costoTarea=0.0;
						int diasEntreCont = (int) ChronoUnit.DAYS.between(t.getFechaInicio(), t.getFechaFin());
						int horasTrabajoRealizadoCont = diasEntreCont*4;
						
						if(horasTrabajoRealizadoCont < t.getDuracionEstimada()) {
							costoTarea= t.getDuracionEstimada()*this.costoHora*1.3;
						}
						
						else if(horasTrabajoRealizadoCont > t.getDuracionEstimada()+8) {
							costoTarea= (t.getDuracionEstimada()*this.costoHora*0.75);
						}
						else costoTarea=t.getDuracionEstimada()*this.costoHora;
						return costoTarea;
					};
				break;
			
		}
		
	}
	
	//tested
	public Double salario() {
		
		
		double sum = this.tareasAsignadas.stream()
							.filter((t1) -> (t1.getFacturada() == false && t1.getFechaFin() != null))
							.peek(t1 -> t1.setFacturada(true))
							.mapToDouble(t1 -> this.costoTarea(t1))
							.sum();
		
		return sum;
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica caal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	
	//tested
	public Double costoTarea(Tarea t) {
		
		if(t.getFechaFin() != null) {
			return calculoPagoPorTarea.apply(t);
			
		}
		else {
			return (t.getDuracionEstimada() * this.costoHora);
	
		}
	}
		
	//tested
	public Boolean asignarTarea(Tarea t) throws TareaYaAsignadaException, TareaYaFinalizoException {
		
		switch(tipo) {
			case CONTRATADO: 
				int cantidadTareasAsignadas = (int) this.tareasAsignadas.stream()
															.filter((t1) -> (t1.getFechaFin() == null))
															.count();		
				if(cantidadTareasAsignadas >= 5) return false;
				break;
				
			case EFECTIVO:
				
				//int horasTotEstimadas = this.tareasAsignadas.stream()
				//						.mapToInt(t1 -> t.getDuracionEstimada())
				//						.sum();
				
				if((this.obtenerHorasTrabajo()+t.getDuracionEstimada()) >= 15)return false;
				break;
				
		}
		
		this.tareasAsignadas.add(t);
		t.asignarEmpleado(this);
		return true;
	}
	
	
	private Integer obtenerHorasTrabajo() {
        Integer totalHoras=0;
        for(Tarea unaTarea: tareasAsignadas) {
             if(unaTarea!=null) {
                 totalHoras=totalHoras+unaTarea.getDuracionEstimada();
             }
        }
        return totalHoras;
    }
	
	
	
	
	
	public void comenzar(Integer idTarea) throws TareaNoExisteException{
		
		Optional <Tarea> tarea =  this.tareasAsignadas.stream()
							.filter(t1 -> (idTarea == t1.getId()))
							.findFirst();
		
		if(tarea.isEmpty()) throw new TareaNoExisteException();
		Tarea t = tarea.get();
		t.setFechaInicio(LocalDateTime.now());
	
	}
	
	
	
	public void finalizar(Integer idTarea) throws TareaNoExisteException{
		
		Optional <Tarea> tarea =  this.tareasAsignadas.stream()
							.filter(t1 -> (t1.getId() == idTarea))
							.findFirst();
		
		if(tarea.isEmpty()) throw new TareaNoExisteException();
		Tarea t = tarea.get();
		t.setFechaFin(LocalDateTime.now());
	
	}

	public void comenzar(Integer idTarea,String fecha) throws TareaNoExisteException{
		
		Optional <Tarea> tarea =  this.tareasAsignadas.stream()
							.filter(t1 -> (t1.getId() == idTarea))
							.findFirst();
		
		if(tarea.isEmpty()) throw new TareaNoExisteException();
		Tarea t = tarea.get();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		LocalDateTime fechaInicio = LocalDateTime.parse(fecha, formatter);
		t.setFechaInicio(fechaInicio);
	}
	
	public void finalizar(Integer idTarea,String fecha) throws TareaNoExisteException{
		
		Optional <Tarea> tarea =  this.tareasAsignadas.stream()
							.filter(t1 -> (t1.getId() == idTarea))
							.findFirst();
		
		if(tarea.isEmpty()) throw new TareaNoExisteException();
		
		Tarea t = tarea.get();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		LocalDateTime fechaFin = LocalDateTime.parse(fecha, formatter);
		t.setFechaFin(fechaFin);
		
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}
	
	public class TareaNoExisteException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TareaNoExisteException() {
			super("La Tarea seleccionada no existe.");
		}
	}
	
	
	
	
	public class TareaYaFinalizadaException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TareaYaFinalizadaException() {
			super("La Tarea ya fue finalizada, seleccione otra.");
		}
	}
	public class EmpleadoYaAsignadoException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EmpleadoYaAsignadoException() {
			super("El Empleado ya esta asignado.");
		}
	}
	public Long getCuil() {
		return cuil;
	}


	public String guardarTarea() {
		
		return nombre+","+cuil;
	}


	public Tipo getTipo() {
		return tipo;
	}


	public String getNombre() {
		return nombre;
	}


	public Double getCostoHora() {
		return costoHora;
	}



	

}
