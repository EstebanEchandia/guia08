package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDateTime;

public class Tarea {

	private Integer id;
	private String descripcion;
	private Integer duracionEstimada;
	private Empleado empleadoAsignado;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Boolean facturada;

	
	
	public Tarea(Integer id, String descripcion, Integer duracionEstimada) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.duracionEstimada = duracionEstimada;
		this.facturada = false;
	}



	public void asignarEmpleado(Empleado e) throws TareaYaFinalizoException,TareaYaAsignadaException{
		if(fechaFin != null) throw new TareaYaFinalizoException();
		else if(this.empleadoAsignado != null) throw new TareaYaAsignadaException();
		else {
			this.empleadoAsignado = e;
		}
		// si la tarea ya tiene un empleado asignado
		// y tiene fecha de finalizado debe lanzar una excepcion
	}
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(Integer duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getFacturada() {
		return facturada;
	}

	public void setFacturada(Boolean facturada) {
		this.facturada = facturada;
	}

	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}
	
	public class TareaYaFinalizoException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TareaYaFinalizoException () {
			super("La Tarea seleccionada no existe.");
		}
	}
	public class TareaYaAsignadaException extends Exception {
			/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	
			public TareaYaAsignadaException () {
				super("La Tarea seleccionada no existe.");
			}
		}
	public String guardarTarea() {
		return id+","+descripcion+","+empleadoAsignado.guardarTarea();
	}
		
}
