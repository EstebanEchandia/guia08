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
import java.time.temporal.ChronoUnit;

public class Empleado {


	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	private LocalDate fechaContratacion;
	
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea;
	
	

	

	public Empleado(Integer cuil, String nombre, Tipo tipo, Double costoHora) {
		super();
		this.cuil = cuil;
		this.nombre = nombre;
		this.tipo = tipo;
		this.costoHora = costoHora;
		this.tareasAsignadas = new ArrayList<Tarea>();
		
		Function<Tarea, Double> calculoPagoPorTarea = (Tarea t)->{
			switch(tipo) {
				case EFECTIVO: 
					int diasEntre = (int) ChronoUnit.DAYS.between(t.getFechaInicio(), t.getFechaFin());
					int horasTrabajoRealizado = diasEntre*4;
					if(horasTrabajoRealizado < t.getDuracionEstimada()) {
						return t.getDuracionEstimada()*this.costoHora*1.2;
					}
					break;
						
				case CONTRATADO: 
					int diasEntre = (int) ChronoUnit.DAYS.between(t.getFechaInicio(), t.getFechaFin());
					int horasTrabajoRealizado = diasEntre*4;
					if(horasTrabajoRealizado < t.getDuracionEstimada()) {
						return t.getDuracionEstimada()*this.costoHora*1.3;
					}
					else if(horasTrabajoRealizado > t.getDuracionEstimada()+8) {
						return t.getDuracionEstimada()*this.costoHora*0.75;
					}
					break;
			};
		};
		
	}
	
	
	public Double salario() {
		
		return this.tareasAsignadas.stream()
							.filter((t1) -> (t1.getFacturada() == false && t1.getFechaFin() != null))
							.peek(t1 -> t1.setFacturada(true))
							.map(t1 -> this.costoTarea(t1))
							.reduce(0.0, (a,b) -> a+b);
		
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica caal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		
		if(t.getFechaFin() != null) {
			return calculoPagoPorTarea.apply(t);
			
		}
		else {
			return t.getDuracionEstimada() * this.costoHora;
	
		}
		
		
		return 0.0;
	}
		
	public Boolean asignarTarea(Tarea t) throws EmpleadoYaAsignadoException, TareaYaFinalizadaException {
		
		if(t.getEmpleadoAsignado() != null)
			throw new EmpleadoYaAsignadoException();
		else if(t.getFechaFin() != null) {
			throw new TareaYaFinalizadaException();
		}
		switch(tipo) {
			case CONTRATADO: 
				int cantidadTareasAsignadas = (int) this.tareasAsignadas.stream()
															.filter((t1) -> (t1.getFechaFin()!=null))
															.count();		
				if(cantidadTareasAsignadas >= 5) return false;
				break;
				
			case EFECTIVO:
				int horasTotEstimadas = this.tareasAsignadas.stream()
										.mapToInt(t1 -> t.getDuracionEstimada())
										.sum();
				if(horasTotEstimadas+t.getDuracionEstimada() >= 15)return false;
				break;
				
		}
		
		this.tareasAsignadas.add(t);
		return true;
	}
	
	public void comenzar(Integer idTarea) throws TareaNoExisteException{
		
		Optional <Tarea> tarea =  this.tareasAsignadas.stream()
							.filter(t1 -> (t1.getId() == idTarea))
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
	public Integer getCuil() {
		return cuil;
	}
	
	

}
